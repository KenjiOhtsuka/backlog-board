package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.presentation.common.LayoutJsonView

object IssueJsonView {
    fun show(issue: Issue): Map<String, Any?> {
        return LayoutJsonView.success(mapOf(
                "issue_list" to arrayOf(
                        issueMap(issue)
                )
        ))
    }

    fun issueMap(issue: Issue): Map<String, Any?> {
        return mapOf(
                "id" to issue.id,
                "title" to issue.title,
                "detail" to issue.detail,
                "estimated_hour" to issue.estimatedHour,
                "actual_hour" to issue.actualHour,
                "category_list" to
                        CategoryJsonView.categoryListMap(issue.categoryList),
                "milestone_list" to
                        MilestoneJsonView.milestonListMap(issue.milestoneList)
        )
    }
}