package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.model.Project
import com.improve_future.backlog_board.presentation.common.LayoutView
import com.improve_future.backlog_board.presentation.core.row
import com.improve_future.backlog_board.utility.DateUtility
import kotlinx.html.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap
import java.util.*

object ProjectView {
    fun index(
            redirectAttributes: RedirectAttributes,
            projectList: List<Project>) = LayoutView.default(
            redirectAttributes,
            styleLinkArray = arrayOf("/backlog/css/issue.css")) {
        projectList.forEach {
            row {
                div("col-sm-1") {
                    a("/project/${it.key}/board") {
                        img(src = "/project/${it.key}/icon") {
                            classes = setOf("project_icon")
                        }
                    }
                }
                div("col-sm-11 align-middle") {
                    a("/project/${it.key}/board") {
                        +(it.name ?: "")
                    }
                }
            }
        }
    }

    fun gantt(
            modelMap: RedirectAttributesModelMap,
            projectKey: String,
            issueList: List<Issue>) =
            LayoutView.default(
                    modelMap,
                    styleLinkArray = arrayOf(
                            "/backlog/css/issue.css",
                            "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css")) {
                navigation(projectKey
                )
                table {
                    val count = 30 * 3
                    val cellWidth = 15L
                    val width = cellWidth * count
                    val today = Date()
                    thead {
                        tr {
                            th {
                                colSpan = "2"
                                rowSpan = "2"
                                +"Issue"
                            }
                            th {
                                rowSpan = "2"
                            }
                            for (i in 1..count) {
                                th {
                                    attributes["style"] = "width:${cellWidth}px;"
                                    +DateUtility.getMonth(DateUtility.addDate(today, i)).toString()
                                }
                            }
                        }
                        tr {
                            for (i in 1..count) {
                                th {
                                    attributes["style"] = "width:${cellWidth}px;"
                                    +DateUtility.getDay(DateUtility.addDate(today, i)).toString()
                                }
                            }
                        }
                    }
                    tbody {
                        val issueRow = { issue: Issue ->
                            tr {
                                if (!issue.isChild()) {
                                    td {
                                        colSpan = "2"
                                        a(issue.url) {
                                            target = "_blank"
                                            +issue.title
                                        }
                                    }
                                } else {
                                    td { +"-" }
                                    td {
                                        a(issue.url) {
                                            target = "_blank"
                                            +issue.title
                                        }
                                    }
                                }
                                td {
                                    if (issue.assignee != null) userIcon(issue.assignee!!)
                                }

                                td {
                                    attributes["style"] = "width:${width}px;"
                                    colSpan = count.toString()
                                    var termCellCount = 0L
                                    var paddingCellCount = 0L
                                    if (issue.startDate == null) {
                                        paddingCellCount = 0
                                        if (issue.dueDate == null) {
                                            termCellCount = count.toLong()
                                        } else {
                                            if (issue.dueDate!! > today)
                                                termCellCount = Math.min(
                                                        DateUtility.getDifferenceInDay(today, issue.dueDate!!) + 1,
                                                        count.toLong())
                                        }
                                    } else {
                                        if (issue.startDate!! > today) {
                                            val diff = DateUtility.getDifferenceInDay(today, issue.startDate!!) + 1
                                            if (diff < count) paddingCellCount = diff
                                        }

                                        if (issue.dueDate == null) {
                                            termCellCount =
                                                    count - paddingCellCount
                                        } else {
                                            val diff = DateUtility.getDifferenceInDay(issue.startDate!!, issue.dueDate!!) + 1
                                            termCellCount = Math.min(count - paddingCellCount, diff)
                                        }
                                    }
                                    div("issue_term") {
                                        attributes["style"] = "width:${termCellCount * cellWidth}px;left:${paddingCellCount * cellWidth}px;position:relative;"
                                    }
                                }
                            }
                        }

                        issueList.forEach {
                            issueRow(it)
                            for (childIssue in it.childIssues) {
                                issueRow(childIssue)
                            }
                        }
                    }
                }
                script(src = "https://code.jquery.com/ui/1.12.1/jquery-ui.min.js") {}
                script("/backlog/js/gantt.js") { }
            }
}