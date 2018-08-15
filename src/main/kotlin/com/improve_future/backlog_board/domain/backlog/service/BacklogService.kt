package com.improve_future.backlog_board.domain.backlog.service

import com.improve_future.backlog_board.domain.backlog.model.Category
import com.improve_future.backlog_board.domain.backlog.repository.BacklogRepository
import com.improve_future.backlog_board.domain.backlog.model.Issue
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

    fun findAllIssue(projectKey: String): List<Issue> {
        return backlogRepository.findAllIssues(projectKey)
    }

    @Autowired
    lateinit private var categoryRepository: CategoryRepository

    fun findAllProject(spaceKey: String, apiKey: String): List<Project> {
        return backlogRepository.findAllProjects(spaceKey, apiKey)
    }

    fun retrieveUserIcon(
            spaceKey: String, apiKey: String, userId: Long): ByteArray {
        return backlogRepository.retrieveUserIcon(spaceKey, apiKey, userId)
    }

    fun retrieveProjectIcon(
            spaceKey: String, apiKey: String, projectKey: String): ByteArray {
        return backlogRepository.retrieveProjectIcon(
                spaceKey, apiKey, projectKey)
    }

    fun findAllMilestone(
            spaceKey: String, apiKey: String, projectKey: String): List<Milestone> {
        return milestoneRepository.findAll(
                spaceKey, apiKey, projectKey)
    }

    fun findAllCategory(spaceKey: String, apiKey: String, projectKey: String): List<Category> {
        return categoryRepository.findAllCategory(
                spaceKey, apiKey, projectKey)
    }

    fun findAllMilestone(projectKey: String): List<Milestone> {
        return milestoneRepository.findAll(projectKey)
    }
}
