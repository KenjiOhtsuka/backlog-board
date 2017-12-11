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
@RequestMapping("user")
class UserController {
    @Autowired
    lateinit var backlogService: BacklogService

    @RequestMapping(
            "{id}/icon",
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
}