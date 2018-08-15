package com.improve_future.backlog_board.domain.backlog.repository

import com.nulabinc.backlog4j.BacklogClient
import com.nulabinc.backlog4j.BacklogClientFactory
import com.nulabinc.backlog4j.conf.BacklogConfigure
import com.nulabinc.backlog4j.conf.BacklogJpConfigure

abstract class AbstractBacklogRepository {
    protected fun buildBacklogClient(
            spaceKey: String, apiKey: String): BacklogClient {
        val configure: BacklogConfigure =
                BacklogJpConfigure(spaceKey).apiKey(apiKey)
        return BacklogClientFactory(configure).newClient()
    }
}