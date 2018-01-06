package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.*
import com.improve_future.backlog_board.presentation.common.LayoutView
import com.improve_future.backlog_board.presentation.core.col
import com.improve_future.backlog_board.presentation.core.popUp
import com.improve_future.backlog_board.presentation.core.row
import com.improve_future.backlog_board.utility.DateUtility
import kotlinx.html.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import com.nulabinc.backlog4j.Issue as BacklogIssue

object BacklogView {
    fun FlowContent.messageTag(message: String?, isOverDue: Boolean, url: String? = null) {
        val content: () -> Unit =
                if (isOverDue) {
                    { strong { +(message ?: "") } }
                } else {
                    { +(message ?: "") }
                }
        if (url == null) content()
        else a(url, "_blank") { content() }
    }

    fun index(
            redirectAttributes: RedirectAttributes,
            projectKey: String,
            issues: List<Issue>) =
            LayoutView.default(
                    redirectAttributes,
                    styleLinkArray = arrayOf("/backlog/css/issue.css")) {
                navigation(projectKey)
                table {
                    thead {
                        tr {
                            th {
                                colSpan = "3"
                                +"Parent Issue"
                            }
                            th {
                                colSpan = "3"
                                +"Child Issue"
                            }
                        }
                        tr {
                            for (i in 1..2) {
                                th { +"Title" }
                                th { +"Due Date" }
                                th { +"Assignee" }
                            }
                        }
                    }
                    tbody {
                        issues.forEach {
                            for (i in 1..maxOf(1, it.childIssues.count()))
                                tr {
                                    td {
                                        if (i == 1)
                                            messageTag(it.title, it.isOverDue(), it.url)
                                    }
                                    td {
                                        if (i == 1)
                                            messageTag(
                                                    DateUtility.formatToNullableHyphenSeparatedDate(it.dueDate), it.isOverDue())
                                    }
                                    td {
                                        if (i == 1)
                                            if (it.assignee != null)
                                                userIcon(it.assignee!!)
                                    }
                                    val childIssue = if (it.childIssues.count() >= i)
                                        it.childIssues[i - 1] else null
                                    val isOverDue = childIssue?.isOverDue() == true
                                    td { messageTag(childIssue?.title, isOverDue, childIssue?.url) }
                                    td { messageTag(DateUtility.formatToNullableHyphenSeparatedDate(childIssue?.dueDate), isOverDue) }
                                    td {
                                        if (childIssue?.assignee != null) {
                                            userIcon(childIssue.assignee!!)
                                        }
                                    }
                                }
                        }
                    }
                }
            }

    fun board(
            redirectAttributes: RedirectAttributes,
            projectKey: String,
            milestoneId: Long?,
            milestoneList: List<Milestone>,
            categoryId: Long?,
            categoryList: List<Category>,
            parentIssues: List<Issue>, unitIssues: List<Issue>) = LayoutView.default(redirectAttributes, "Kanban Board",
            arrayOf(
                    "/backlog/css/issue.css",
                    "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css")) {

        navigation(projectKey)

        fun issueTile(issue: Issue) = div {
            classes = setOf(
                    "issue_tile",
                    when (issue.priorityType) {
                        BacklogIssue.PriorityType.High ->
                            "priority_high"
                        BacklogIssue.PriorityType.Low ->
                            "priority_low"
                        else ->
                            "priority_normal"
                    }
            )
            attributes["data-id"] = issue.id.toString()
            messageTag(issue.title, issue.isOverDue(), issue.url)
            messageTag(
                    DateUtility.formatToNullableHyphenSeparatedDate(issue.dueDate),
                    issue.isOverDue())
            if (issue.assignee != null) div { userIcon(issue.assignee!!) }
            +"Est. "
            span("estimated_hour") { +issue.estimatedHour.toString() }
            +" / "
            +"Act. "
            span("actual_hour") { +issue.actualHour.toString() }
        }

        fun issuesOfThePhase(issues: List<Issue>, status: com.nulabinc.backlog4j.Issue.StatusType) {
            val filteredIssues = issues.filter { it.status!!.id == status.intValue }
            filteredIssues.forEach { issueTile(it) }
        }

        getForm() {
            classes = setOf("form-inline")
            select {
                name = "milestone_id"
                option { }
                milestoneList.forEach {
                    option {
                        value = it.id.toString()
                        if (milestoneId == it.id) selected = true
                        +it.name.orEmpty()
                    }
                }
            }
            select {
                name = "category_id"
                option { }
                categoryList.forEach {
                    option {
                        value = it.id.toString()
                        if (categoryId == it.id) selected = true
                        +it.name.orEmpty()
                    }
                }
            }
            button { +"Search" }
        }

        fun boardColumn(title: String, issueList: List<Issue>, statusType: BacklogIssue.StatusType, isFixed: Boolean = false) {
            section("col board_column") {
                h2 { +title }
                div {
                    +"Est. "
                    span("estimated_hour") { }
                    +" / Act. "
                    span("actual_hour") { }
                }
                var bodyClass = "board_column_body"
                if (!isFixed) bodyClass += " sortable"
                div(bodyClass) {
                    attributes["data-status-id"] = statusType.intValue.toString()
                    issuesOfThePhase(issueList, statusType)
                }
            }
        }

        row {
            section("col board_column") {
                h2 { +"Group" }
                div {
                    +"Est. "
                    span("estimated_hour") { }
                    +" / Act. "
                    span("actual_hour") { }
                }
                div("board_column_body") {
                    parentIssues.forEach {
                        issueTile(it)
                    }
                    unitIssues.forEach {
                        issueTile(it)
                    }
                }
            }
            val issueList = parentIssues + unitIssues
            boardColumn("To Do", issueList, BacklogIssue.StatusType.Open)
            boardColumn("In Progress", issueList, BacklogIssue.StatusType.InProgress)
            boardColumn("Review", issueList, BacklogIssue.StatusType.Resolved)
            boardColumn("Close", issueList, BacklogIssue.StatusType.Closed)
        }

        popUp() {
            p { id = "modal_detail" }
            div {
                +"Est. "
                span { id = "modal_estimated_hour" }
                +" / Act. "
                span { id = "modal_actual_hour" }
            }
        }

        script(src = "https://code.jquery.com/ui/1.12.1/jquery-ui.min.js") {}
        script(src = "/backlog/js/issue.js", type = ScriptType.textJavaScript) {
            attributes["charset"] = "UTF-8"
        }
    }

    fun oldBoard(
            redirectAttributes: RedirectAttributes,
            projectKey: String,
            milestoneId: Long?,
            milestoneList: List<Milestone>,
            categoryId: Long?,
            categoryList: List<Category>,
            parentIssues: List<Issue>, unitIssues: List<Issue>) = LayoutView.default(redirectAttributes, "Kanban Board",
            arrayOf(
                    "/backlog/css/issue.css",
                    "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css")) {

        navigation(projectKey)

        fun issueTile(issue: Issue) = div {
            classes = setOf(
                    "issue_tile",
                    "old",
                    when (issue.priorityType) {
                        BacklogIssue.PriorityType.High ->
                            "priority_high"
                        BacklogIssue.PriorityType.Low ->
                            "priority_low"
                        else ->
                            "priority_normal"
                    }
            )
            attributes["data-id"] = issue.id.toString()
            messageTag(issue.title, issue.isOverDue(), issue.url)
            messageTag(
                    DateUtility.formatToNullableHyphenSeparatedDate(issue.dueDate),
                    issue.isOverDue())
            if (issue.assignee != null) div { userIcon(issue.assignee!!) }
            +"Est. "
            span("estimated_hour") { +issue.estimatedHour.toString() }
            +" / "
            +"Act. "
            span("actual_hour") { +issue.actualHour.toString() }
        }

        fun issuesOfThePhase(issues: List<Issue>, status: com.nulabinc.backlog4j.Issue.StatusType) {
            val filteredIssues = issues.filter { it.status!!.id == status.intValue }
            filteredIssues.forEach { issueTile(it) }
        }

        getForm() {
            classes = setOf("form-inline")
            select {
                name = "milestone_id"
                option { }
                milestoneList.forEach {
                    option {
                        value = it.id.toString()
                        if (milestoneId == it.id) selected = true
                        +it.name.orEmpty()
                    }
                }
            }
            select {
                name = "category_id"
                option { }
                categoryList.forEach {
                    option {
                        value = it.id.toString()
                        if (categoryId == it.id) selected = true
                        +it.name.orEmpty()
                    }
                }
            }
            button { +"Search" }
        }

        table {
            thead {
                tr {
                    th { }
                    th {
                        +"ToDo"
                    }
                    th {
                        +"Doing"
                    }
                    th {
                        +"Review"
                    }
                }
            }
            tbody {
                parentIssues.forEach {
                    tr {
                        td { issueTile(it) }
                        td("sortable") {
                            attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.Open.intValue.toString()
                            issuesOfThePhase(it.childIssues, com.nulabinc.backlog4j.Issue.StatusType.Open)
                        }
                        td("sortable") {
                            attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.InProgress.intValue.toString()
                            issuesOfThePhase(it.childIssues, com.nulabinc.backlog4j.Issue.StatusType.InProgress)
                        }
                        td("sortable") {
                            attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.Resolved.intValue.toString()
                            issuesOfThePhase(it.childIssues, com.nulabinc.backlog4j.Issue.StatusType.Resolved)
                        }
                    }
                    tr {
                        td { }
                        td("sortable") {
                            attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.Open.intValue.toString()
                            issuesOfThePhase(unitIssues, com.nulabinc.backlog4j.Issue.StatusType.Open)
                        }
                        td("sortable") {
                            attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.InProgress.intValue.toString()
                            issuesOfThePhase(unitIssues, com.nulabinc.backlog4j.Issue.StatusType.InProgress)
                        }
                        td("sortable") {
                            attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.Resolved.intValue.toString()
                            issuesOfThePhase(unitIssues, com.nulabinc.backlog4j.Issue.StatusType.Resolved)
                        }
                    }
                }
            }

            popUp() {
                p { id = "modal_detail" }
                div {
                    +"Est. "
                    span { id = "modal_estimated_hour" }
                    +" / Act. "
                    span { id = "modal_actual_hour" }
                }
            }

            script(src = "https://code.jquery.com/ui/1.12.1/jquery-ui.min.js") {}
            script(src = "/backlog/js/issue.js", type = ScriptType.textJavaScript) {
                attributes["charset"] = "UTF-8"
            }
        }
    }
}