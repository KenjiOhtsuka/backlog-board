package com.improve_future.backlog_board.domain.backlog.service

import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.repository.BacklogRepository
import com.improve_future.backlog_board.domain.backlog.repository.IssueRepository
import com.nulabinc.backlog4j.IssueType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IssueService {
    @Autowired
    lateinit private var issueRepository: IssueRepository

    @Autowired
    lateinit private var backlogRepository: BacklogRepository

    fun findIssue(spaceKey: String, apiKey: String, id: Long): Issue {
        return issueRepository.findOne(spaceKey, apiKey, id)
    }

    fun findAllNonParentIssue(
            spaceKey: String, apiKey: String, projectKey: String, milestoneId: Long, categoryId: Long?): List<Issue> {
        return backlogRepository.findAllIssues(
                spaceKey, apiKey, projectKey, milestoneId, categoryId)
    }

    fun findAllUnclosedIssue(
            spaceKey: String, apiKey: String, projectKey: String, milestoneId: Long? = null, categoryId: Long? = null): List<Issue> {
        return backlogRepository.findAllUnclosedIssues(
                spaceKey, apiKey, projectKey, milestoneId, categoryId)
    }

    fun findAllIssueForGanttChart(
            spaceKey: String, apiKey: String, projectKey: String): List<Issue> {
        return backlogRepository.findAllIssuesInStartOrder(
                spaceKey, apiKey, projectKey)
    }

    fun update(
            spaceKey: String, apiKey: String,
            id: Long, valueMap: MutableMap<String, Any?>): Issue {
        valueMap["id"] = id
        return issueRepository.update(
                spaceKey, apiKey, valueMap)
    }
}