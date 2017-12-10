package com.improve_future.backlog_board.domain.backlog.service

import com.improve_future.backlog_board.domain.backlog.repository.BacklogRepository
import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class BacklogService {
    @Autowired
    lateinit private var backlogRepository: BacklogRepository

    fun findAllIssue(projectKey: String): List<Issue> {
        return backlogRepository.findAllIssues(projectKey)
    }

    fun findAllProject(): List<Project> {
        return backlogRepository.findAllProjects()
    }

    fun updateStatus(id: Long, statusId: Long) {
        backlogRepository.updateStatus(id, statusId)
    }

    fun retrieveUserIcon(userId: Long): ByteArray {
        return backlogRepository.retrieveUserIcon(userId)
    }

    fun retrieveProjectIcon(projectKey: String): ByteArray {
        return backlogRepository.retrieveProjectIcon(projectKey)
    }
}