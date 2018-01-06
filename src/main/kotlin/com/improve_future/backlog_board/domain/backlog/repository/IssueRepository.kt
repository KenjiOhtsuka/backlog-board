package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.domain.backlog.factory.IssueFactory
import com.improve_future.backlog_board.domain.backlog.model.Issue
import org.springframework.stereotype.Component

@Component
object IssueRepository: AbstractBacklogRepository() {
    fun findOne(id: Long): Issue {
        return IssueFactory.createFromBacklogIssue(
                backlogConfig.spaceId, backlogGateway.getIssue(id))
    }
}