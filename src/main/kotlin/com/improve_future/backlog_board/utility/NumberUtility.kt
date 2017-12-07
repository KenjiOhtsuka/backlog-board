package com.improve_future.backlog_board

import java.text.NumberFormat
import java.util.*

object NumberUtility {
    fun format(number: Number, locale: Locale = Locale.US): String {
        return NumberFormat.getNumberInstance(locale).format(number)
    }
}