package com.improve_future.backlog_board.domain.backlog.factory

import com.improve_future.backlog_board.domain.backlog.model.Milestone
import com.nulabinc.backlog4j.Milestone as BacklogMilestone

object MilestoneFactory {
    fun createFromBacklogMilestone(spaceKey: String, backlogMilestone: BacklogMilestone): Milestone {
        val milestone = Milestone()
        milestone.id = backlogMilestone.id
        milestone.projectId = backlogMilestone.projectId
        milestone.isArchived = backlogMilestone.archived
        milestone.detail = backlogMilestone.description
        milestone.name = backlogMilestone.name
        milestone.releaseDate = backlogMilestone.releaseDueDate
        milestone.startDate = backlogMilestone.startDate
        return milestone
    }
}