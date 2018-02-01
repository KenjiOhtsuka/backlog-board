package com.improve_future.backlog_board.domain.backlog.factory

import com.improve_future.backlog_board.domain.backlog.model.IssueType
import com.nulabinc.backlog4j.IssueType as BacklogIssueType

object IssueTypeFactory {
    fun createFromBacklogIssueType(
            spaceKey: String, backlogIssueType: BacklogIssueType): IssueType {
        val issueType = IssueType()
        issueType.id = backlogIssueType.id
        issueType.color =
                IssueTypeColorFactory.createFromBacklogIssueTypeColor(
                    spaceKey, backlogIssueType.color)
        issueType.name = backlogIssueType.name
        backlogIssueType.projectId
        return issueType
    }
}