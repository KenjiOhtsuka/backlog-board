package com.improve_future.backlog_board.presentation.account

import com.improve_future.backlog_board.presentation.common.LayoutView
import com.improve_future.backlog_board.presentation.core.row
import kotlinx.html.*
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import javax.servlet.http.HttpServletRequest

object AccountView {
    fun loginForm(request: HttpServletRequest) = LayoutView.plain() {
        postForm(action = "/account/login") {
            val csrfToken = HttpSessionCsrfTokenRepository().generateToken(request)
            hiddenInput(name = csrfToken.parameterName) { value = csrfToken.token }
            row("form-group") {
                label("col-form-label col-4 col-sm-3 col-md-2") {
                    htmlFor = "space_id"
                    +"Space ID"
                }
                div("col-8 col-sm-9 col-md-10") {
                    input(type = InputType.text, name = "space_id", classes = "form-control")
                }
            }
            row("form-group") {
                label("col-form-label col-4 col-sm-3 col-md-2") {
                    htmlFor = "api_key"
                    +"API Key"
                }
                div("col-8 col-sm-9 col-md-10") {
                    input(type = InputType.password, name = "api_key", classes = "form-control")
                }
            }
            row("form-group") {
                div("offset-4 offset-sm-3 offset-md-2 col-4 col-sm-6 col-md-8") {
                    button {
                        classes = setOf("btn", "btn-primary", "btn-block")
                        +"Login"
                    }
                }
            }
        }
    }
}