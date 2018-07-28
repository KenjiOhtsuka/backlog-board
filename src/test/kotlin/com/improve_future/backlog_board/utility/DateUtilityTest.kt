package com.improve_future.backlog_board.utility

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals


class DateUtilityTest {
    @Test
    fun testCreateDate() {
        val date = DateUtility.createDate(2000, 1, 2)
        val calendar = Calendar.getInstance()
        calendar.time = date
        assertEquals(2000, calendar.get(Calendar.YEAR))
        assertEquals(0, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun testFormatToSlashSeparatedDate() {
        val date = DateUtility.createDate(2000, 1, 2)
        assertEquals(
                "2000/01/02",
                DateUtility.formatToSlashSeparatedDate(date))
        val localDate = DateUtility.createYearMonth(2017, 1).atDay(1)
        assertEquals(
                "2017/01/01",
            DateUtility.formatToSlashSeparatedDate(localDate))
    }
}