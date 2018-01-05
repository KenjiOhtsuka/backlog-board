package com.improve_future.backlog_board.presentation.core

import com.improve_future.backlog_board.base.HumanSelectInterface
import com.improve_future.backlog_board.presentation.common.CommonView
import com.improve_future.backlog_board.presentation.lang.CommonLang
import kotlinx.html.*


fun FlowContent.row(classes : String? = null, block : DIV.() -> Unit = {}) =
        divWithClass("row", classes, block)

fun FlowContent.col(classes : String? = null, block : DIV.() -> Unit = {}) =
        divWithClass("col", classes, block)

private fun FlowContent.divWithClass(defaultClass: String, classes : String? = null, block : DIV.() -> Unit = {}): Unit {
    val _classes: String
    if (classes.isNullOrBlank()) _classes = defaultClass
    else _classes = classes + " " + defaultClass
    return div(_classes, block)
}

fun FlowContent.aTel(
        telephoneNumber: String,
        target : String? = null,
        classes : String? = null,
        block : A.() -> Unit = {}): Unit =
        a(
                "tel:" + telephoneNumber,
                target, classes, block)

fun FlowContent.aMailTo(
        emailAddress: String,
        target : String? = null,
        classes : String? = null,
        block : A.() -> Unit = {}): Unit =
        a(
            "mailto:" + emailAddress,
            target, classes, block)

fun FlowContent.preparedTextArea(
        rows : Int = 5,
        cols : Int = 20,
        classes : String? = null,
        block : TEXTAREA.() -> Unit = {}): Unit =
        textArea(
                rows.toString(),
                cols.toString(),
                TextAreaWrap.soft,
                classes,
                block)

fun <T> FlowContent.pageNavigation(pager: Pager<T>, width: Long = 2, edge: Long = 2) = ul("pagination") {
    val pageListByPage = fun (page: Long): Unit {
        val link = pager.linkByPage(page)
        if (link != null) {
            li("page-item") {
                a(link.composePath()) {
                    classes = setOf("page-link")
                    attributes["aria-label"] = "Page " + page.toString()
                    +page.toString()
                }
            }
        }
    }
    role = "navigation"
    attributes["aria-label"] = "Pagination"
    // previous
    li {
        if (pager.hasPrevious())
            classes = setOf("page-item")
        else
            classes = setOf("page-item", "disabled")
        a(pager.previousLink()?.composePath() ?: "#") {
            classes = setOf("page-link")
            attributes["aria-label"] = "Previous Page"
            +"Previous"
            span("sr-only") { +"page" }
        }
    }
    // former
    for (i in 1..minOf(pager.formerCount() - width, edge)) {
        pageListByPage(i)
    }
    if (pager.formerCount() - width > edge) {
        // elipses
        li("page-item disabled") {
            a("#") {
                classes = setOf("page-link")
                +"..."
            }
        }
    }
    for (i in -width..-1) {
        pageListByPage(pager.page + i)
    }
    //current
    li("page-item active") {
        a("#") {
            classes = setOf("page-link")
            span("sr-only") { +"You're on page" }
            +pager.page.toString()
        }
    }
    // latter
    for (i in 1..width) {
        pageListByPage(pager.page + i)
    }
    if (pager.latterCount() - width > edge) {
        // elipses
        li("page-item disabled") {
            a("#") {
                classes = setOf("page-link")
                +"..."
            }
        }
    }
    for (i in (maxOf(pager.page + width, pager.pageCount - edge) + 1)..pager.pageCount) {
        pageListByPage(i)
    }
    // next
    li {
        if (pager.hasNext())
            classes = setOf("page-item")
        else
            classes = setOf("page-item", "disabled")
        a(pager.nextLink()?.composePath() ?: "#") {
            classes = setOf("page-link")
            attributes["aria-label"] = "Next Page"
            +"Next"
            span("sr-only") { +"page" }
        }
    }
    // last
}

/*
fun <T> FlowContent.pageNavigation(page: Page<T>, width: Long = 2, edge: Long = 2) = ul("pagination") {
    val pageListByPage = fun (page: Long): Unit {
        val link = pager.linkByPage(page)
        if (link != null) {
            li("page-item") {
                a(link.composePath()) {
                    classes = setOf("page-link")
                    attributes["aria-label"] = "Page " + page.toString()
                    +page.toString()
                }
            }
        }
    }
    role = "navigation"
    attributes["aria-label"] = "Pagination"
    // previous
    li {
        if (!page.isFirst)
            classes = setOf("page-item")
        else
            classes = setOf("page-item", "disabled")
        a(pager.previousLink()?.composePath() ?: "#") {
            classes = setOf("page-link")
            attributes["aria-label"] = "Previous Page"
            +"Previous"
            span("sr-only") { +"page" }
        }
    }
    // former
    for (i in 1..minOf(pager.formerCount() - width, edge)) {
        pageListByPage(i)
    }
    if (pager.formerCount() - width > edge) {
        // elipses
        li("page-item disabled") {
            a("#") {
                classes = setOf("page-link")
                +"..."
            }
        }
    }
    for (i in -width..-1) {
        pageListByPage(pager.page + i)
    }
    //current
    li("page-item active") {
        a("#") {
            classes = setOf("page-link")
            span("sr-only") { +"You're on page" }
            +pager.page.toString()
        }
    }
    // latter
    for (i in 1..width) {
        pageListByPage(pager.page + i)
    }
    if (pager.latterCount() - width > edge) {
        // elipses
        li("page-item disabled") {
            a("#") {
                classes = setOf("page-link")
                +"..."
            }
        }
    }
    for (i in (maxOf(pager.page + width, pager.pageCount - edge) + 1)..pager.pageCount) {
        pageListByPage(i)
    }
    // next
    li {
        if (!page.isLast)
            classes = setOf("page-item")
        else
            classes = setOf("page-item", "disabled")

        a(pager.nextLink()?.composePath() ?: "#") {
            classes = setOf("page-link")
            attributes["aria-label"] = "Next Page"
            +"Next"
            span("sr-only") { +"page" }
        }
    }
}    */

fun FlowContent.deleteConfirmationModal() = div("modal delete") {
    val lang = CommonView.lang()
    attributes["aria-labelledby"] = "smallDeleteLabel"
    attributes["aria-hidden"] = "true"
    tabIndex = (-1).toString()
    role = "dialog"
    div("modal-dialog modal-sm") {
        div("modal-content") {
            div("modal-header") {
                h5("modal-title") { +CommonLang.Confirmation.text(lang) }
            }
            div("modal-body") {
                row {
                    div("col-12") {
                        +com.improve_future.backlog_board.presentation.lang.CommonLang.IsItOkToDeleteTheRecord.text(lang)
                    }
                }
            }
            div("modal-footer") {
                postForm {
                    hiddenInput {
                        name = "_method"
                        value = "DELETE"
                    }
                    row("form-group") {
                        div("col-12") {
                            button {
                                attributes["data-dismiss"] = "modal"
                                classes = kotlin.collections.setOf("btn")
                                +com.improve_future.backlog_board.presentation.lang.CommonLang.ButtonTextCancel.text(lang)
                            }
                            +" "
                            button {
                                classes = kotlin.collections.setOf("btn btn-danger")
                                type = kotlinx.html.ButtonType.submit
                                +com.improve_future.backlog_board.presentation.lang.CommonLang.ButtonTextDelete.text(lang)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.idSelectTag(
        classes: String? = null,
        options: List<HumanSelectInterface> = listOf(),
        selectedValue: Long? = null,
        containBlank: Boolean = true,
        lang: String = "en",
        block : SELECT.() -> Unit = {}) =
        select(classes) {
            block()
            if (containBlank) option {}
            options.forEach {
                option() {
                    if (selectedValue == it.humanSelectId)
                        attributes["selected"] = "selected"
                    value = it.humanSelectId.toString()
                    +(it.humanSelectName(lang) ?: "")
                }
            }
        }

fun FlowContent.rubyWithBrace(
        mainContent: RUBY.() -> Unit = {},
        rubyText: RT.() -> Unit = {},
        classes : String? = null,
        block : RUBY.() -> Unit = {}) =
    ruby(classes) {
        block()
        mainContent()
        rp { +"(" }
        rt { rubyText() }
        rp { +")"}
    }
