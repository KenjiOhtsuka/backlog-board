package com.improve_future.backlog_board.domain.backlog.factory

import com.improve_future.backlog_board.domain.backlog.model.IssueTypeColor
import com.nulabinc.backlog4j.Project.IssueTypeColor as BacklogIssueTypeColor

object IssueTypeColorFactory {
    fun createFromBacklogIssueTypeColor(
            spaceKey: String, backlogIssueTypeColor: BacklogIssueTypeColor): IssueTypeColor {
        val issueTypeColor = IssueTypeColor()
        issueTypeColor.name = backlogIssueTypeColor.name
        return issueTypeColor
    }
}