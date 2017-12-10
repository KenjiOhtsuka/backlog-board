package com.improve_future.backlog_board.base

class RecordNavigationSet<T>(
        val list: List<T>,
        val itemCountAPage: Int,
        val page: Long,
        val recordCount: Long) {

    val pageCount: Long

    init {
        pageCount = (recordCount - 1) / itemCountAPage + 1
    }
}