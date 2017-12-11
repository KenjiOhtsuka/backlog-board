package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.model.Project
import com.improve_future.backlog_board.domain.backlog.model.User
import com.improve_future.backlog_board.presentation.common.LayoutView
import com.improve_future.backlog_board.presentation.core.row
import com.improve_future.backlog_board.utility.DateUtility
import kotlinx.html.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

object BacklogView {
    fun FlowContent.messageTag(message: String?, isOverDue: Boolean, url: String? = null) {
        val content: () -> Unit =
            if (isOverDue) { { strong { +(message ?: "") } } }
            else { { +(message?: "") } }
        if (url == null) content()
        else a(url, "_blank") { content() }
    }

    fun FlowContent.navigation(projectKey: String) {
        nav("row") {
            ul("nav") {
                li("nav-item") {
                    a("/project/$projectKey/board") {
                        classes = setOf("nav-link")
                        +"Kanban Board"
                    }
                }
                li("nav-item") {
                    a("/project/$projectKey/issue_list") {
                        classes = setOf("nav-link")
                        +"Issues"
                    }
                }
            }
        }
    }

    fun HtmlContent.userIcon(user: User) {
        a(user.urlString, "_blank") {
            img(src = "/user/${user.id}/icon") {
                classes = setOf("user_icon")
                alt = user.name ?: ""
                title = alt
            }
        }
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
            parentIssues: List<Issue>, unitIssues: List<Issue>) = LayoutView.default(redirectAttributes, "Kanban Board",
            arrayOf(
                    "/backlog/css/issue.css",
                    "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css")) {

        navigation(projectKey)

        fun issueTile(issue: Issue) = div {
            classes = setOf(
                    "issue_tile",
                    when (issue.priorityType) {
                        com.nulabinc.backlog4j.Issue.PriorityType.High ->
                                "priority_high"
                        com.nulabinc.backlog4j.Issue.PriorityType.Low ->
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
        }

        fun issuesOfThePhase(issues: List<Issue>, status: com.nulabinc.backlog4j.Issue.StatusType) {
            val filteredIssues = issues.filter { it.status!!.id == status.intValue }
            filteredIssues.forEach { issueTile(it) }
        }

        table {
            thead {
                tr {
                    th { }
                    th { +"ToDo" }
                    th { +"Doing" }
                    th { +"Review" }
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
                }
                tr {
                    td { }
                    td("sortable") {
                        attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.Open.intValue.toString()
                        issuesOfThePhase(unitIssues, com.nulabinc.backlog4j.Issue.StatusType.Open) }
                    td("sortable") {
                        attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.InProgress.intValue.toString()
                        issuesOfThePhase(unitIssues, com.nulabinc.backlog4j.Issue.StatusType.InProgress) }
                    td("sortable") {
                        attributes["data-status-id"] = com.nulabinc.backlog4j.Issue.StatusType.Resolved.intValue.toString()
                        issuesOfThePhase(unitIssues, com.nulabinc.backlog4j.Issue.StatusType.Resolved) }
                }
            }
        }
        script(src = "https://code.jquery.com/ui/1.12.1/jquery-ui.min.js") {}
        script(src = "/backlog/js/issue.js" ,type = ScriptType.textJavaScript) {}
    }
}