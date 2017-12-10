package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/backlog")
class BacklogController {
    @Autowired
    lateinit var backlogService: BacklogService

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun index(
            attributes: RedirectAttributes
    ): String {
        val issues = backlogService.findAllIssue()
        return BacklogView.index(
                attributes, issues)
    }

    @RequestMapping("/board", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun board(
            attributes: RedirectAttributes
    ): String {
        val issues = backlogService.findAllIssue()
        return BacklogView.board(
                attributes,
                issues.filter { it.childIssues.count() > 0 },
                issues.filter { it.childIssues.count() == 0 })
    }

    @RequestMapping(
            "/user/{id}/icon",
            method = arrayOf(RequestMethod.GET))
    fun icon(
            @PathVariable id: Long,
            attributes: RedirectAttributes): HttpEntity<ByteArray> {

        val imageByteArray = backlogService.retrieveUserIcon(id) //.toTypedArray()
        val header = HttpHeaders()
        header.contentType = MediaType.IMAGE_GIF
        header.contentLength = imageByteArray.size.toLong()
        return HttpEntity(imageByteArray, header)
    }

    @RequestMapping("issue/{id}", method = arrayOf(RequestMethod.PATCH))
    @ResponseBody
    fun updateIssue(
            @PathVariable id: Long,
            @RequestParam(value="status_id") statusId: Long,
            attributes: RedirectAttributes): Map<String, Any> {
        backlogService.updateStatus(id, statusId)
        return mapOf("data" to mapOf<String, Any>())   }
}