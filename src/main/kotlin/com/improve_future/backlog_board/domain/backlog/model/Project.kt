package com.improve_future.backlog_board.domain.backlog.model

class Project: AbstractBacklog() {
    var id: Long? = null
    var name: String? = null
    var key: String? = null
    var orderNumber: Long? = null

    val urlString: String by lazy {
        "https://$spaceKey.backlog.jp/projects/$key"
    }
}