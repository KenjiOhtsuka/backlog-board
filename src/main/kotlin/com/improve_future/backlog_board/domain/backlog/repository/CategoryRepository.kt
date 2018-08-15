package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.domain.backlog.factory.CategoryFactory
import com.improve_future.backlog_board.domain.backlog.model.Category
import org.springframework.stereotype.Component

@Component
class CategoryRepository: AbstractBacklogRepository() {
    fun findAllCategory(
            spaceKey: String, apiKey: String, projectKey: String): List<Category> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getCategories(projectKey).map {
            CategoryFactory.createFromBacklogCategory(
                    spaceKey, it)
        }
    }
}