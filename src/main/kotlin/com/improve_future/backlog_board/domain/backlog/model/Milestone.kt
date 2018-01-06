package com.improve_future.backlog_board.domain.backlog.model

import java.util.*

class Milestone: AbstractBacklog() {
    var id: Long? = null
    var name: String? = null
    var detail: String? = null
    var releaseDate: Date? = null
    var startDate: Date? = null
    var projectId: Long? = null
    var isArchived: Boolean = false
}