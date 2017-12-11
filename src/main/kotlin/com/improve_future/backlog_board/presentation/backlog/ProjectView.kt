package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Project
import com.improve_future.backlog_board.presentation.common.LayoutView
import com.improve_future.backlog_board.presentation.core.row
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.img
import org.springframework.web.servlet.mvc.support.RedirectAttributes

object ProjectView {
    fun projectIndex(
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
}