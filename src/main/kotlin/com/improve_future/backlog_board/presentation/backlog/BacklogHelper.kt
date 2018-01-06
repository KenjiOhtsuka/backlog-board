package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.User
import kotlinx.html.*

fun FlowContent.navigation(projectKey: String) {
    nav("row") {
        ul("nav") {
            val item = { path: String, title: String ->
                li("nav-item") {
                    a(path) {
                        classes = setOf("nav-link")
                        +title
                    }
                }
            }
            item("/project/$projectKey/board", "Kanban Board")
            item("/project/$projectKey/unclosed_board", "Unclosed Board")
            item("/project/$projectKey/issue_list", "Issues")
            item("/project/$projectKey/gantt", "Gantt Chart")
        }
    }
}

fun HtmlContent.userIcon(user: User) {
    a(user.urlString, "_blank") {
        img(src = "/user/${user.id}/icon") {
            classes = kotlin.collections.setOf("user_icon")
            alt = user.name ?: ""
            title = alt
        }
    }
}
