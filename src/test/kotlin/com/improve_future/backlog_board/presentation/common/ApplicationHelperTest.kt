package com.improve_future.backlog_board.presentation.common

import kotlinx.html.FlowContent
import kotlinx.html.TagConsumer
import kotlinx.html.body
import kotlinx.html.dom.create
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.document
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import org.junit.Test
import java.io.StringWriter
import javax.management.Query.div
import kotlin.test.assertEquals


class ApplicationHelperTest {
    @Test
    fun testNl2Br() {
        createHTMLDocument().html {
            body {
                assertEquals(
                        "<br>abc<br>def<br>",
                        nl2Br("\nabc\ndef\n"))
            }
        }
    }
}