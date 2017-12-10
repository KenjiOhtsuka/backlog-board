package com.improve_future.backlog_board.domain.backlog.master

import com.nulabinc.backlog4j.Issue

object IssuePriorityMaster {
    fun valueOf(backlogPriorityType: Issue.PriorityType?): IssuePriority? {
        IssuePriority.values().forEach {
            if (it.backlogPriorityType == backlogPriorityType)
                return it
        }
        return null
    }
}