package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.domain.backlog.factory.IssueFactory
import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.nulabinc.backlog4j.api.option.UpdateIssueParams
import org.springframework.stereotype.Component
import com.nulabinc.backlog4j.Issue as BacklogIssue

@Component
object IssueRepository: AbstractBacklogRepository() {
    fun findOne(id: Long): Issue {
        return IssueFactory.createFromBacklogIssue(
                backlogConfig.spaceId, backlogGateway.getIssue(id))
    }

    fun update(issueParam: Map<String, Any?>): Issue {
        val param = UpdateIssueParams(issueParam["id"] as Long)
        if (issueParam.containsKey("status_id"))
            param.status(BacklogIssue.StatusType.valueOf(issueParam["status_id"] as Int))
        if (issueParam.containsKey("actual_hour"))
            param.actualHours((issueParam["actual_hour"] as Number).toFloat())
        return IssueFactory.createFromBacklogIssue(
                backlogConfig.spaceId,
                backlogGateway.updateIssue(param))
    }
}