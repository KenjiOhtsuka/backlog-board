package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.config.BacklogConfig
import com.improve_future.backlog_board.domain.backlog.factory.IssueFactory
import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.gateway.WebGateway
import com.nulabinc.backlog4j.BacklogClient
import com.nulabinc.backlog4j.BacklogClientFactory
import com.nulabinc.backlog4j.CustomField as BacklogCustomField
import com.nulabinc.backlog4j.Issue as BacklogIssue
import com.nulabinc.backlog4j.Project
import com.nulabinc.backlog4j.PullRequest
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
    private val backlogProject: Project by lazy {
        backlogGateway.getProject(backlogConfig.projectKey)
    }

    fun findAllIssues(): List<Issue> {
        val issuesMap = findAllIssuesMap()
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

    private fun buildUserIconUrl(userId: Long): URL {
        return URL(
                    "https://${backlogConfig.spaceId}.backlog.jp/api/v2/users/$userId/icon?apiKey=${backlogConfig.apiKey}")
    }

    /**
     * Find and return Id to Issue map
     */
    private fun findAllIssuesMap(): Map<Long, BacklogIssue> {
        val issues = mutableMapOf<Long, BacklogIssue>()
        do {
            val issueChunks = this.findAllIssuesPartly(issues.count())
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
                PullRequest.StatusType.Closed,
                PullRequest.StatusType.Merged))
        return prParam
    }

    /**
     * Find and return Issue List
     */
    private fun findAllIssuesPartly(offset: Number = 0): List<com.nulabinc.backlog4j.Issue> {
        val issueParam: GetIssuesParams = GetIssuesParams(
                mutableListOf(backlogProject.id))
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