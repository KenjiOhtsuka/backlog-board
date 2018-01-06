package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Category

object CategoryJsonView {
    fun categoryMap(category: Category): Map<String, Any?> {
        return mapOf(
                "id" to category.id,
                "name" to category.name
        )
    }

    fun categoryListMap(categoryList: Collection<Category>): Map<String, Any?> {
        return mapOf(
                "category_list" to categoryList.map { categoryMap(it) }
        )
    }
}