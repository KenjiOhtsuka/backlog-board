package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Milestone

object MilestoneJsonView {
    fun milestoneMap(milestone: Milestone): Map<String, Any?> {
        return mapOf(
                "id" to milestone.id,
                "name" to milestone.name,
                "start_date" to milestone.startDate,
                "release_date" to milestone.releaseDate,
                "is_archived" to milestone.isArchived
        )
    }

    fun milestonListMap(milestoneList: Collection<Milestone>): Map<String, Any?> {
        return mapOf(
                "milestone_list" to milestoneList.map { milestoneMap(it) }
        )
    }
}