package com.improve_future.backlog_board.presentation.common

import kotlinx.html.FlowContent
import kotlinx.html.HtmlContent
import kotlinx.html.br

fun staticFile(path: String): String {
    return "/static/" + path
}

fun HtmlContent.nl2Br(text: String): String {
    // ToDo: Use kotlinx.html for br tag
    return text.replace("\n", "<br>")
}