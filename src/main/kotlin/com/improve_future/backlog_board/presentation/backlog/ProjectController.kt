package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.model.Issue
import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import com.improve_future.backlog_board.domain.backlog.service.IssueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap
import java.security.Principal

@Controller
@RequestMapping("project")
class ProjectController {
    @Autowired
    lateinit private var backlogService: BacklogService

    @Autowired
    lateinit private var issueService: IssueService

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun index(attributes: RedirectAttributes, principal: Principal): String {
        val projectList = backlogService.findAllProject()
        return ProjectView.index(
                attributes, projectList)
    }

    @GetMapping("{key}/board")
    @ResponseBody
    fun board(
            @PathVariable("key") projectKey: String,
            @RequestParam("milestone_id", required = false) milestoneId: Long? = null,
            @RequestParam("category_id", required = false) categoryId: Long? = null,
            attributes: RedirectAttributes): String {
        val milestoneList = backlogService.findAllMilestone(projectKey)
        val issueList =
            if (milestoneId != null) issueService.findAllIssue(projectKey, milestoneId, categoryId)
            else emptyList()

        val categoryList = backlogService.findAllCategory(projectKey)

        return BacklogView.board(
                attributes,
                projectKey,
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
        val milestoneList = backlogService.findAllMilestone(projectKey)
        val issueList = issueService.findAllUnclosedIssue(projectKey, milestoneId, categoryId)

        val categoryList = backlogService.findAllCategory(projectKey)

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
            redirectAttributes: RedirectAttributesModelMap): String {
        val issueList = issueService.findAllIssueForGanttChart(projectKey)
        return ProjectView.gantt(redirectAttributes, projectKey, issueList)
    }

    @RequestMapping(
            "{key}/issue_list",
            method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun issueList(
            @PathVariable("key") projectKey: String,
            attributes: RedirectAttributes): String {
        val issueList = issueService.findAllUnclosedIssue(projectKey)
        return BacklogView.index(
                attributes, projectKey, issueList)
    }

    @RequestMapping(
            "{key}/icon",
            method = arrayOf(RequestMethod.GET))
    fun projectIcon(
            @PathVariable key: String,
            attributes: RedirectAttributes): HttpEntity<ByteArray> {

        val imageByteArray = backlogService.retrieveProjectIcon(key) //.toTypedArray()
        val header = HttpHeaders()
        header.contentType = MediaType.IMAGE_GIF
        header.contentLength = imageByteArray.size.toLong()
        return HttpEntity(imageByteArray, header)
    }
}