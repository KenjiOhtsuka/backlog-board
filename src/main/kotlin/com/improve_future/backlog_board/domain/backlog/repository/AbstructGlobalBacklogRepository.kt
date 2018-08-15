package com.improve_future.backlog_board.domain.backlog.repository

import com.nulabinc.backlog4j.BacklogClient
import com.nulabinc.backlog4j.BacklogClientFactory
import com.nulabinc.backlog4j.conf.BacklogConfigure
import com.nulabinc.backlog4j.conf.BacklogJpConfigure

abstract class AbstructGlobalBacklogRepository {
    protected fun buildClient(spaceId: String, apiKey: String): BacklogClient {
        return BacklogClientFactory(
            BacklogJpConfigure(spaceId).apiKey(apiKey)).newClient()
    }
}