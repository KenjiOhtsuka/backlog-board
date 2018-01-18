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

    fun findIssue(id: Long): Issue {
        return issueRepository.findOne(id)
    }

    fun findAllNonParentIssue(projectKey: String, milestoneId: Long, categoryId: Long?): List<Issue> {
        return backlogRepository.findAllIssues(projectKey, milestoneId, categoryId)
    }

    fun findAllUnclosedIssue(projectKey: String, milestoneId: Long? = null, categoryId: Long? = null): List<Issue> {
        return backlogRepository.findAllUnclosedIssues(projectKey, milestoneId, categoryId)
    }

    fun findAllIssueForGanttChart(projectKey: String): List<Issue> {
        return backlogRepository.findAllIssuesInStartOrder(projectKey)
    }

    fun updateStatus(id: Long, statusId: Long) {
        backlogRepository.updateStatus(id, statusId)
    }

    fun update(
            spaceKey: String, apiKey: String,
            id: Long, valueMap: MutableMap<String, Any?>): Issue {
        valueMap["id"] = id
        return issueRepository.update(
                spaceKey, apiKey, valueMap)
    }
}