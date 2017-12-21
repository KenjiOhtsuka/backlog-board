package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.config.BacklogConfig
import com.improve_future.backlog_board.domain.backlog.factory.MilestoneFactory
import com.improve_future.backlog_board.domain.backlog.model.Milestone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MilestoneRepository: AbstractBacklogRepository() {
    fun findAll(projectKey: String): List<Milestone> {
        return backlogGateway.getMilestones(projectKey).map {
            MilestoneFactory.createFromBacklogMilestone(backlogConfig.spaceId, it)
        }
    }
}