package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap

@Controller
@RequestMapping("project")
class ProjectController {
    @Autowired
    lateinit var backlogService: BacklogService

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun index(attributes: RedirectAttributes): String {
        val projectList = backlogService.findAllProject()
        return ProjectView.index(
                attributes, projectList)
    }

    @RequestMapping(
            "{key}/board",
            method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun board(
            @PathVariable("key") projectKey: String,
            attributes: RedirectAttributes): String {
        val issueList = backlogService.findAllIssue(projectKey)
        return BacklogView.board(
                attributes,
                projectKey,
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
        val issueList = backlogService.findAllIssueForGanttChart(projectKey)
        return ProjectView.gantt(redirectAttributes, projectKey, issueList)
    }

    @RequestMapping(
            "{key}/issue_list",
            method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun issueList(
            @PathVariable("key") projectKey: String,
            attributes: RedirectAttributes): String {
        val issueList = backlogService.findAllIssue(projectKey)
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