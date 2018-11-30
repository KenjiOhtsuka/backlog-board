package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.base.SecurityContextHolder
import com.improve_future.backlog_board.domain.backlog.model.IssueType
import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import com.improve_future.backlog_board.domain.backlog.service.IssueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap
import java.security.Principal
import java.security.Security

@Controller
@RequestMapping("project")
class ProjectController {
    @Autowired
    lateinit private var backlogService: BacklogService

    @Autowired
    lateinit private var issueService: IssueService

    @Autowired
    lateinit private var categoryService: BacklogService

    @GetMapping
    @ResponseBody
    fun index(attributes: RedirectAttributes, principal: Principal): String {
        val user = SecurityContextHolder.getCurrentUser()
        val projectList = backlogService.findAllProject(
                user.spaceKey!!, user.apiKey!!)
        return ProjectView.index(
                attributes, projectList)
    }

    @GetMapping("{key}/board")
    @ResponseBody
    fun board(
            @PathVariable("key") projectKey: String,
            @RequestParam("issue_type_id", required = false) issueTypeId: Long? = null,
            @RequestParam("milestone_id", required = false) milestoneId: Long? = null,
            @RequestParam("category_id", required = false) categoryId: Long? = null,
            attributes: RedirectAttributes): String {
        val user = SecurityContextHolder.getCurrentUser()

        val milestoneList = backlogService.findAllMilestone(
                user.spaceKey!!, user.apiKey!!, projectKey)
        val issueTypeList: List<IssueType> =
                issueService.findAllIssueType(
                        user.spaceKey!!, user.apiKey!!, projectKey)
        val issueList =
            if (milestoneId != null) issueService.findAllNonParentIssue(
                    user.spaceKey!!, user.apiKey!!,
                    projectKey, issueTypeId, milestoneId, categoryId)
            else emptyList()

        val categoryList = backlogService.findAllCategory(
                user.spaceKey!!, user.apiKey!!, projectKey)

        return BacklogView.board(
                attributes,
                projectKey,
                issueTypeId,
                issueTypeList,
                milestoneId,
                milestoneList,
                categoryId,
                categoryList,
                issueList.filter { it.childIssues.count() > 0 },
                issueList.filter { it.childIssues.count() == 0 })
    }

    @GetMapping("{key}/unclosed_board")
    @ResponseBody
    fun oldBoard(
            @PathVariable("key") projectKey: String,
            @RequestParam("milestone_id", required = false) milestoneId: Long? = null,
            @RequestParam("category_id", required = false) categoryId: Long? = null,
            attributes: RedirectAttributes): String {
        val user = SecurityContextHolder.getCurrentUser()

        val milestoneList = backlogService.findAllMilestone(
                user.spaceKey!!, user.apiKey!!, projectKey)
        val issueList = issueService.findAllUnclosedIssue(
                user.spaceKey!!, user.apiKey!!,
                projectKey, milestoneId, categoryId)

        val categoryList = backlogService.findAllCategory(
                user.spaceKey!!, user.apiKey!!, projectKey)

        return BacklogView.oldBoard(
                attributes,
                projectKey,
                milestoneId,
                milestoneList,
                categoryId,
                categoryList,
                issueList.filter { it.childIssues.count() > 0 },
                issueList.filter { it.childIssues.count() == 0 })
    }

    @RequestMapping(
            "{key}/gantt",
            method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun gantt(
            @PathVariable("key") projectKey: String,
            @RequestParam("category_id") categoryId: Long?,
            redirectAttributes: RedirectAttributesModelMap): String {
        val user = SecurityContextHolder.getCurrentUser()
        val categoryList = categoryService.findAllCategory(
                user.spaceKey!!, user.apiKey!!, projectKey)
        val issueList = issueService.findAllIssueForGanttChart(
                user.spaceKey!!, user.apiKey!!, projectKey)
        return ProjectView.gantt(redirectAttributes, projectKey, issueList)
    }

    @RequestMapping(
            "{key}/issue_list",
            method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun issueList(
            @PathVariable("key") projectKey: String,
            attributes: RedirectAttributes): String {
        val user = SecurityContextHolder.getCurrentUser()
        val issueList = issueService.findAllUnclosedIssue(
                user.spaceKey!!, user.apiKey!!, projectKey)
        return BacklogView.index(
                attributes, projectKey, issueList)
    }

    @RequestMapping(
            "{key}/icon",
            method = arrayOf(RequestMethod.GET))
    fun projectIcon(
            @PathVariable key: String,
            attributes: RedirectAttributes): HttpEntity<ByteArray> {
        val user = SecurityContextHolder.getCurrentUser()
        val imageByteArray = backlogService.retrieveProjectIcon(
                user.spaceKey!!, user.apiKey!!, key) //.toTypedArray()
        val header = HttpHeaders()
        header.contentType = MediaType.IMAGE_GIF
        header.contentLength = imageByteArray.size.toLong()
        return HttpEntity(imageByteArray, header)
    }
}