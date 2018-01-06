package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.domain.backlog.factory.CategoryFactory
import com.improve_future.backlog_board.domain.backlog.model.Category
import org.springframework.stereotype.Component

@Component
class CategoryRepository: AbstractBacklogRepository() {
    fun findAllCategory(projectKey: String): List<Category> {
        return backlogGateway.getCategories(projectKey).map {
            CategoryFactory.createFromBacklogCategory(
                    backlogConfig.spaceId, it)
        }
    }
}