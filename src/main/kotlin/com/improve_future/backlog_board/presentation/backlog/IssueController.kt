package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("issue")
class IssueController {
    @Autowired
    lateinit var backlogService: BacklogService

    @PatchMapping("{id}")
    @ResponseBody
    fun updateIssue(
            @PathVariable id: Long,
            @RequestParam(value="status_id") statusId: Long,
            attributes: RedirectAttributes): Map<String, Any> {
        backlogService.updateStatus(id, statusId)
        return mapOf("data" to mapOf<String, Any>())   }
}