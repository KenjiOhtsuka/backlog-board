package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.config.BacklogConfig
import com.improve_future.backlog_board.domain.backlog.factory.IssueFactory
import com.improve_future.backlog_board.domain.backlog.factory.ProjectFactory
import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.model.Project
import com.improve_future.backlog_board.gateway.WebGateway
import com.nulabinc.backlog4j.BacklogClient
import com.nulabinc.backlog4j.BacklogClientFactory
import com.nulabinc.backlog4j.CustomField as BacklogCustomField
import com.nulabinc.backlog4j.Issue as BacklogIssue
import com.nulabinc.backlog4j.Project as BacklogProject
import com.nulabinc.backlog4j.PullRequest as BacklogPullRequest
import com.nulabinc.backlog4j.api.option.GetIssuesParams
import com.nulabinc.backlog4j.api.option.PullRequestQueryParams
import com.nulabinc.backlog4j.api.option.UpdateIssueParams
import com.nulabinc.backlog4j.conf.BacklogConfigure
import com.nulabinc.backlog4j.conf.BacklogJpConfigure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL

@Component
class BacklogRepository: AbstractBacklogRepository() {
    fun findAllProjects(): List<Project> {
        return backlogGateway.projects.map {
            ProjectFactory.createFromBacklogProject(
                    backlogConfig.spaceId, it) }
    }

    fun findProject(key: String): Project {
        return ProjectFactory.createFromBacklogProject(
                backlogConfig.spaceId,
                backlogGateway.getProject(key))
    }

    fun findAllIssues(projectKey: String): List<Issue> {
        val project = findProject(projectKey)
        val issueParam = GetIssueParam(listOf(project.id!!))
        issueParam.statuses(
                listOf(
                        BacklogIssue.StatusType.Open,
                        BacklogIssue.StatusType.InProgress,
                        BacklogIssue.StatusType.Resolved))
        issueParam.sort(GetIssuesParams.SortKey.DueDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.count(100)

        return findAllIssueMap(issueParam).values.toList()
    }

    fun findAllIssuesInStartOrder(projectKey: String): List<Issue> {
        val project = findProject(projectKey)
        val issueParam = GetIssueParam(listOf(project.id!!))
        issueParam.statuses(
                listOf(
                        BacklogIssue.StatusType.Open,
                        BacklogIssue.StatusType.InProgress,
                        BacklogIssue.StatusType.Resolved))
        issueParam.sort(GetIssuesParams.SortKey.StartDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.sort(GetIssuesParams.SortKey.DueDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.count(100)

        return findAllIssueMap(issueParam).values.toList()
    }

    fun updateStatus(id: Long, statusId: Long) {
        var params = UpdateIssueParams(id.toString())
        params.status(com.nulabinc.backlog4j.Issue.StatusType.valueOf(statusId.toInt()))
        backlogGateway.updateIssue(params)
    }

    fun retrieveUserIcon(userId: Long): ByteArray {
        return WebGateway.getImage(
                buildUserIconUrl(userId),
                mapOf(
                        "apiKey" to backlogConfig.apiKey
                ))
    }

    fun retrieveProjectIcon(projectKey: String): ByteArray {
        return WebGateway.getImage(
                buildProjectIconUrl(projectKey),
                mapOf(
                        "apiKey" to backlogConfig.apiKey
                )
        )
    }

    private fun buildUserIconUrl(userId: Long): URL {
        return URL(
                    "https://${backlogConfig.spaceId}.backlog.jp/api/v2/users/$userId/icon?apiKey=${backlogConfig.apiKey}")
    }

    private fun buildProjectIconUrl(projectKey: String): URL {
        return URL( "https://${backlogConfig.spaceId}.backlog.jp/api/v2/projects/$projectKey/image?apiKey=${backlogConfig.apiKey}")
    }

    private fun findAllIssueMap(condition: GetIssueParam): Map<Long, Issue> {
        val issueList = mutableListOf<Issue>()
        val parentIssuesMap = mutableMapOf<Long, Issue>()
        do {
            val param = condition.clone()
            param.offset(issueList.count().toLong())
            val issueChunks = this.findIssues(param)
            issueList += issueChunks
        } while (issueChunks.count() > 0)
        issueList.forEach {
            if (!it.isChild())
                parentIssuesMap.put(it.id!!, it)
        }
        issueList.forEach {
            if (it.isChild())
                parentIssuesMap[it.parentIssueId!!]?.addChild(it) ?:
                        parentIssuesMap.put(it.id!!, it)
        }
        return parentIssuesMap
    }

    private fun buildClosedIssueCondition(projectId: Long):
            GetIssuesParams {
        val issueParam: GetIssuesParams =
                GetIssuesParams(mutableListOf(projectId))
        issueParam.statuses(listOf(BacklogIssue.StatusType.Closed))
        issueParam.resolutions(listOf(
                BacklogIssue.ResolutionType.CannotReproduce,
                BacklogIssue.ResolutionType.Fixed,
                BacklogIssue.ResolutionType.NotSet,
                BacklogIssue.ResolutionType.WontFix))
        return issueParam
    }

    private fun buildFinishedPrCondition(): PullRequestQueryParams {
        var prParam = PullRequestQueryParams()
        prParam.statusType(listOf(
                BacklogPullRequest.StatusType.Closed,
                BacklogPullRequest.StatusType.Merged))
        return prParam
    }

    /**
     * Find and return Issue List
     */
    private fun findIssues(condition: GetIssueParam): List<Issue> {
        return backlogGateway.getIssues(condition).
                map { IssueFactory.createFromBacklogIssue(backlogConfig.spaceId, it) }
    }

    private fun createIssueFromBacklogIssue(backlogIssue: BacklogIssue): Issue {
        return IssueFactory.createFromBacklogIssue(
                backlogConfig.spaceId, backlogIssue)
    }
}