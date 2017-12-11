package com.improve_future.backlog_board.presentation.common

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer
import com.improve_future.backlog_board.presentation.core.MessageMap
import com.improve_future.backlog_board.presentation.core.ModelMap
import com.improve_future.backlog_board.presentation.core.RedirectAttributesHandler
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.springframework.ui.Model
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.StringWriter

object LayoutView: RedirectAttributesHandler() {
    fun HEADER.topBar(): Unit = {
        fun menuGroup(title: String, links: Map<String, String>) = section("col-sm-6 col-md-4") {
            h3 { +title }
            ul {
                links.forEach {
                    li { a(it.value) { +it.key } }
                }
            }
        }
        div("pos-f-t") {
            div("collapse") {
                id = "navbar_toggle_external_content"
                nav {
                    menuGroup(
                            "Issue",
                            mapOf(
                                    "Project List" to "/project",
                                    "Not Yet Closed Issiess" to
                                            "/backlog",
                                    "Kanban" to
                                            "/backlog/board"
                            ))
                }
            }
            nav("navbar navbar-togglable-md navbar-light bg-faded") {
                a("/") {
                    classes = setOf("navbar-brand")
                    +"Backlog Board"
                }
                button {
                    attributes["data-toggle"] = "collapse"
                    attributes["data-target"] = "#navbar_toggle_external_content"
                    attributes["aria-controls"] = "navbar_toggle_external_content"
                    attributes["aria-expanded"] = "false"
                    attributes["aria-label"] = "Toggle navigation"
                    classes = setOf("navbar-toggler", "navbar-toggler-right")
                    type = ButtonType.button
                    span("navbar-toggler-icon")
                    +" Menu"
                }
            }
        }
    }()

    fun HEAD.metaTags(): Unit = {
        meta { attributes["charset"] = "UTF-8" }
        meta(name = "robots", content = "noindex,nofollow,noarchive")
        meta(name = "viewport", content = "width=device-width,initial-scale=1.0")
    }()

    fun HEAD.stylesheetTags(): Unit = {
        styleLink("/css/base.css")
        styleLink("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css")
    }()

    fun HEAD.scriptTags(): Unit = {
        script(src = "https://code.jquery.com/jquery-3.2.1.min.js") {}
    }()

    fun default(
            model: Model,
            pageTitle: String = "",
            styleLinkArray: Array<String> = arrayOf(),
            block: FlowContent.() -> Unit) =
            StringWriter().
                    appendln("<!DOCTYPE html>").
                    appendHTML().html {
                val modelMap = ModelMap(model)
                head {
                    title {
                        +pageTitle
                    }
                    metaTags()
                    scriptTags()
                    stylesheetTags()
                    styleLinkArray.forEach {
                        styleLink(it)
                    }
                }
                body {
                    id = "main"
                    header("mb10") {
                        topBar()
                    }

                    div("container-fluid") {
                        modelMap.getErrorMessageList().forEach {
                            div("alert alert-danger") {
                                role = "alert"
                                +it.text
                            }
                        }
                        modelMap.getSuccessMessageList().forEach {
                            div("alert alert-success") {
                                role = "alert"
                                +it.text
                            }
                        }
                    }

                    div("container-fluid") {
                        block()
                    }
                    script(src = "https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js") {}
                    script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js") {}
                }
            }.toString()

    fun plain(block: DIV.() -> Unit) =
            StringWriter().appendln("<!DOCTYPE html>").
                    appendHTML().html {
                head {
                    title {}
                    metaTags()
                    scriptTags()
                    stylesheetTags()
                }
                body {
                    id = "main"
                    div("container-fluid mt10") {
                        block()
                    }
                    script(src = "https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js") {}
                    script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js") {}
                }
            }.toString()

    fun popup(pageTitle: String = "", block: FlowContent.() -> Unit) =
            StringWriter().
                    appendln("<!DOCTYPE html>").
                    appendHTML().html {
                head {
                    title {
                        +pageTitle
                    }
                    metaTags()
                    scriptTags()
                    stylesheetTags()
                }
                body("mb10") {
                    id = "main"
                    div("container-fluid") {
                        block()
                    }
                    script(src = "https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js") {}
                    script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js") {}
                }
            }.toString()
}