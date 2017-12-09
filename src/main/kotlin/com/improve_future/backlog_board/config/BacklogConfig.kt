package com.improve_future.backlog_board.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BacklogConfig {
    @Value("\${my.backlog.space_id}")
    lateinit var spaceId: String

    @Value("\${my.backlog.api_key}")
    lateinit var apiKey: String

    @Value("\${my.backlog.project_key}")
    lateinit var projectKey: String
}