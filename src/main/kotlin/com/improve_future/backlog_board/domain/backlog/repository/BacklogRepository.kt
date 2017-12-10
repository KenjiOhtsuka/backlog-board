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
class BacklogRepository {
    @Autowired
    lateinit private var backlogConfig: BacklogConfig

    private val backlogGateway: BacklogClient by lazy {
        val configure: BacklogConfigure =
                BacklogJpConfigure(backlogConfig.spaceId).
                        apiKey(backlogConfig.apiKey)
        BacklogClientFactory(configure).newClient()
    }

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
        val issuesMap = findAllIssuesMap(project.id!!)
        val parentIssuesMap = mutableMapOf<Long, Issue>()
        issuesMap.forEach {
            if (it.value.parentIssueId == 0L)
                parentIssuesMap.put(
                        it.key,
                        IssueFactory.createFromBacklogIssue(
                                backlogConfig.spaceId, it.value))
        }
        issuesMap.forEach {
            if (it.value.parentIssueId != 0L)
                parentIssuesMap[it.value.parentIssueId]?.addChild(
                        IssueFactory.createFromBacklogIssue(backlogConfig.spaceId, it.value)) ?:
                        parentIssuesMap.put(
                                it.key,
                                IssueFactory.createFromBacklogIssue(backlogConfig.spaceId, it.value))
        }
        parentIssuesMap.forEach{
            it.value.childIssues.sortBy { it.dueDate }}
        return parentIssuesMap.values.toList().sortedBy { it.dueDate }
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

    private fun findAllIssuesMap(projectId: Long): Map<Long, BacklogIssue> {
        val issues = mutableMapOf<Long, BacklogIssue>()
        do {
            val issueChunks = this.findAllIssuesPartly(projectId, issues.count())
            issueChunks.forEach { issues.put(it.id, it) }
        } while (issueChunks.count() > 0)
        return issues
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
    private fun findAllIssuesPartly(
            projectId: Long, offset: Number = 0): List<com.nulabinc.backlog4j.Issue> {
        val issueParam: GetIssuesParams =
                GetIssuesParams(mutableListOf(projectId))
        issueParam.statuses(
            listOf(
                BacklogIssue.StatusType.Open,
                BacklogIssue.StatusType.InProgress,
                BacklogIssue.StatusType.Resolved))
        issueParam.sort(GetIssuesParams.SortKey.DueDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.offset(offset.toLong())
        issueParam.count(100)
        return backlogGateway.getIssues(issueParam)
    }

    private fun createIssueFromBacklogIssue(backlogIssue: BacklogIssue): Issue {
        return IssueFactory.createFromBacklogIssue(
                backlogConfig.spaceId, backlogIssue)
    }
}