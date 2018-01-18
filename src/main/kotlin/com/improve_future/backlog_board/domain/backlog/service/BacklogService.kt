package com.improve_future.backlog_board.domain.backlog.service

import com.improve_future.backlog_board.domain.backlog.model.Category
import com.improve_future.backlog_board.domain.backlog.repository.BacklogRepository
import com.improve_future.backlog_board.domain.backlog.model.Milestone
import com.improve_future.backlog_board.domain.backlog.model.Project
import com.improve_future.backlog_board.domain.backlog.repository.CategoryRepository
import com.improve_future.backlog_board.domain.backlog.repository.MilestoneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BacklogService {
    @Autowired
    lateinit private var backlogRepository: BacklogRepository

    @Autowired
    lateinit private var milestoneRepository: MilestoneRepository

    @Autowired
    lateinit private var categoryRepository: CategoryRepository

    fun findAllProject(spaceKey: String, apiKey: String): List<Project> {
        return backlogRepository.findAllProjects(spaceKey, apiKey)
    }

    fun retrieveUserIcon(userId: Long): ByteArray {
        return backlogRepository.retrieveUserIcon(userId)
    }

    fun retrieveProjectIcon(projectKey: String): ByteArray {
        return backlogRepository.retrieveProjectIcon(projectKey)
    }

    fun findAllMilestone(projectKey: String): List<Milestone> {
        return milestoneRepository.findAll(projectKey)
    }

    fun findAllCategory(projectKey: String): List<Category> {
        return categoryRepository.findAllCategory(projectKey)
    }
}