package com.improve_future.backlog_board.domain.backlog.repository

import com.improve_future.backlog_board.config.BacklogConfig
import com.nulabinc.backlog4j.BacklogClient
import com.nulabinc.backlog4j.BacklogClientFactory
import com.nulabinc.backlog4j.conf.BacklogConfigure
import com.nulabinc.backlog4j.conf.BacklogJpConfigure
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractBacklogRepository {
    @Autowired
    lateinit protected var backlogConfig: BacklogConfig

    protected val backlogGateway: BacklogClient by lazy {
        val configure: BacklogConfigure =
                BacklogJpConfigure(backlogConfig.spaceId).
                        apiKey(backlogConfig.apiKey)
        BacklogClientFactory(configure).newClient()
    }

    protected fun buildBacklogClient(
            spaceKey: String, apiKey: String): BacklogClient {
        val configure: BacklogConfigure =
                BacklogJpConfigure(spaceKey).apiKey(apiKey)
        return BacklogClientFactory(configure).newClient()
    }
}