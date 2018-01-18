package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.domain.backlog.factory.MilestoneFactory
import com.improve_future.backlog_board.domain.backlog.model.Milestone
import org.springframework.stereotype.Component

@Component
class MilestoneRepository: AbstractBacklogRepository() {
    fun findAll(spaceKey: String, apiKey: String, projectKey: String): List<Milestone> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getMilestones(projectKey).map {
            MilestoneFactory.createFromBacklogMilestone(spaceKey, it)
        }
    }
}