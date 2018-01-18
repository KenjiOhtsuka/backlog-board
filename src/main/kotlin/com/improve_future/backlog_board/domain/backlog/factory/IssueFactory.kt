package com.improve_future.backlog_board.domain.backlog.factory

import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.model.User
import com.nulabinc.backlog4j.IssueType
import com.nulabinc.backlog4j.internal.json.customFields.DateCustomField
import com.nulabinc.backlog4j.internal.json.customFields.NumericCustomField
import com.nulabinc.backlog4j.Issue as BacklogIssue
import com.nulabinc.backlog4j.CustomField as BacklogCustomField

object IssueFactory {
    fun createFromBacklogIssue(spaceKey: String, backlogIssue: BacklogIssue): Issue {
        val issue = Issue()
        issue.spaceKey = spaceKey
        issue.id = backlogIssue.id
        issue.key = backlogIssue.issueKey
        issue.parentIssueId = backlogIssue.parentIssueId
        issue.title = backlogIssue.summary
        issue.detail = backlogIssue.description
        issue.dueDate = backlogIssue.dueDate
        issue.startDate = backlogIssue.startDate
        issue.status = backlogIssue.status
        issue.assignee =
                if (backlogIssue.assignee == null) null
                else UserFactory.createFromBacklogUser(
                        spaceKey, backlogIssue.assignee)
        issue.priorityId = backlogIssue.priority.id
        issue.creator = UserFactory.createFromBacklogUser(
                spaceKey, backlogIssue.createdUser)
        issue.estimatedHour = backlogIssue.estimatedHours
        issue.actualHour = backlogIssue.actualHours
        issue.categoryList = backlogIssue.category.map {
            CategoryFactory.createFromBacklogCategory(spaceKey, it)
        }
        issue.milestoneList = backlogIssue.milestone.map {
            MilestoneFactory.createFromBacklogMilestone(spaceKey, it)
        }
        issue.type = backlogIssue.issueType

        return issue
    }

    private fun customNumericFieldFloatValue(
            field: BacklogCustomField,
            default: Float): Float {
        return customNumericFieldFloatValue(
                field as NumericCustomField, default)
    }

    private fun customNumericFieldFloatValue(
            field: NumericCustomField,
            default: Float): Float {
        return field.value?.toFloat() ?: default
    }
}