package com.improve_future.backlog_board.presentation.core

import com.improve_future.backlog_board.base.RecordNavigationSet
import javax.servlet.http.HttpServletRequest

data class Pager<T>(
        val path: String,
        var condition: MutableMap<String, Any?>,
        val pageCount: Long,
        val page: Long,
        val recordCount: Long) {
    constructor(
            request: HttpServletRequest,
            navigation: RecordNavigationSet<T>
    ): this(
            request.requestURI,
            request.parameterMap.toMutableMap(),
            navigation.pageCount,
            navigation.page,
            navigation.recordCount
    )

    constructor(
            path: String, conditionMap: MutableMap<String, Any?>,
            recordNavigation: RecordNavigationSet<T>
    ): this(
            path, conditionMap,
            recordNavigation.pageCount,
            recordNavigation.page,
            recordNavigation.recordCount)

    fun linkByPage(page: Long): Link? {
        if (page <= 0 || page > pageCount) return null
        condition["page"] = page
        return Link(path, condition)
    }

    fun linkByOffset(offset: Long = 0): Link? {
        return linkByPage(page + offset)
    }

    fun firstLink(): Link {
        return linkByPage(1)!!
    }

    fun lastLink(): Link {
        return linkByPage(pageCount)!!
    }

    fun previousLink(): Link? {
        return linkByPage(page - 1)
    }

    fun hasPrevious(): Boolean {
        return 1 < page
    }

    fun nextLink(): Link? {
        return linkByPage(page + 1)
    }

    fun hasNext(): Boolean {
        return page < pageCount
    }

    fun formerCount(): Long {
        return page - 1
    }

    fun latterCount(): Long {
        return pageCount - page
    }
}