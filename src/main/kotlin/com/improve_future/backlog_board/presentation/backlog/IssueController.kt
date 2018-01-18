package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.base.SecurityContextHolder
import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import com.improve_future.backlog_board.domain.backlog.service.IssueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("issue")
class IssueController {
    @Autowired
    lateinit var issueService: IssueService

    @PatchMapping("{id}")
    @ResponseBody
    fun update(
            @PathVariable("id") id: Long,
            @RequestBody requestBody: Map<String, Any?>
    ): Map<String, Any?> {
        val user = SecurityContextHolder.getCurrentUser()
        val issue = issueService.update(
                user.spaceKey!!, user.apiKey!!,
                id, requestBody["issue"] as MutableMap<String, Any?>)
            return IssueJsonView.show(issue)
    }

    @GetMapping("{id}")
    @ResponseBody
    fun showJson(
        @PathVariable id: Long): Map<String, Any?> {
        return IssueJsonView.show(issueService.findIssue(id))
    }
}