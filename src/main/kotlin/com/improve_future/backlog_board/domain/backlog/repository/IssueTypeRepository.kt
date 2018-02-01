package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.domain.backlog.factory.IssueTypeFactory
import com.improve_future.backlog_board.domain.backlog.model.IssueType

object IssueTypeRepository: AbstractBacklogRepository() {
    fun findAll(spaceKey: String, apiKey: String, projectKey: String): List<IssueType> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getIssueTypes(projectKey).
                map { IssueTypeFactory.createFromBacklogIssueType(
                        spaceKey, it) }
    }
}