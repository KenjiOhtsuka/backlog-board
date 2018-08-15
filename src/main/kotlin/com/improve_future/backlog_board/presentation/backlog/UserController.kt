package com.improve_future.backlog_board.presentation.backlog

import com.improve_future.backlog_board.base.SecurityContextHolder
import com.improve_future.backlog_board.domain.backlog.service.BacklogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("user")
class UserController {
    @Autowired
    lateinit var backlogService: BacklogService

    @GetMapping("{id}/icon")
    fun icon(
            @PathVariable id: Long,
            attributes: RedirectAttributes): HttpEntity<ByteArray> {
        val user = SecurityContextHolder.getCurrentUser()
        val imageByteArray = backlogService.retrieveUserIcon(
                user.spaceKey!!, user.apiKey!!, id) //.toTypedArray()
        val header = HttpHeaders()
        header.contentType = MediaType.IMAGE_GIF
        header.contentLength = imageByteArray.size.toLong()
        return HttpEntity(imageByteArray, header)
    }
}