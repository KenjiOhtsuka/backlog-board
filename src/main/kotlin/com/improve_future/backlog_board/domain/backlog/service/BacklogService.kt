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
    private lateinit var backlogRepository: BacklogRepository

    @Autowired
    private lateinit var milestoneRepository: MilestoneRepository

    @Autowired
    lateinit private var categoryRepository: CategoryRepository

//    fun findAllIssue(spaceKey: String, apiKey: String, projectKey: String): List<Issue> {
//        return backlogRepository.findAllIssues(
//                spaceKey, apiKey, projectKey)
//    }

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
}
