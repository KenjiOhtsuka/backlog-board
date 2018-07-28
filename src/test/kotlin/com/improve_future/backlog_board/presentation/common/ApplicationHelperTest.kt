package com.improve_future.backlog_board.presentation.common

import kotlinx.html.body
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.html
import org.junit.jupiter.api.Test
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