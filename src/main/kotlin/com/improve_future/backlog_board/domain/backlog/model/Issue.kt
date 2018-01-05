package com.improve_future.backlog_board.domain.backlog.model

import com.improve_future.backlog_board.domain.backlog.master.IssuePriority
import com.improve_future.backlog_board.domain.backlog.master.IssuePriorityMaster
import com.nulabinc.backlog4j.Status
import java.math.BigDecimal
import com.nulabinc.backlog4j.CustomField as BacklogCustomField
import com.nulabinc.backlog4j.Issue as BacklogIssue
import java.util.*

class Issue: AbstractBacklog() {
    var parentIssue: Issue? = null
    var childIssues = mutableListOf<Issue>()

    var id: Long? = null
    var key: String? = null
    var parentIssueId: Long? = null
    set(value) {
        if (value == 0L) return
        field = value
    }
    var title: String = ""
    var assignee: User? = null
    var creator: User? = null
    var dueDate: Date? = null
    var startDate: Date? = null
    var status: Status? = null
    var estimatedHour: BigDecimal? = null
    var actualHour: BigDecimal? = null
    var priorityId: Long? = null
    var priorityType: com.nulabinc.backlog4j.Issue.PriorityType?
    get() {
        priorityId ?: return null
        return com.nulabinc.backlog4j.Issue.PriorityType.valueOf(priorityId!!.toInt())
    }
    set(value) {
        priorityId = value?.intValue?.toLong()
    }
    var priority: IssuePriority?
    get() {
        return IssuePriorityMaster.valueOf(priorityType)
    }
    set (value) {
        priorityId = value?.backlogPriorityType?.intValue?.toLong()
    }

    val url by lazy {
        "https://$spaceKey.backlog.jp/view/" + this.key
    }

    fun addChild(issue: Issue): Issue {
        this.childIssues.add(issue)
        return this
    }

    fun addParent(issue: Issue): Issue {
        this.parentIssue = issue
        return this
    }

    fun isOverDue(): Boolean {
        return this.dueDate?.before(Date()) ?: false
    }

    fun isParent(): Boolean {
        return !this.childIssues.isEmpty()
    }

    fun isChild(): Boolean {
        return this.parentIssueId != null
    }
}