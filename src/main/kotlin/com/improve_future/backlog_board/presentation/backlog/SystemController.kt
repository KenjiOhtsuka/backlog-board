package com.improve_future.backlog_board.presentation.backlog

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class SystemController {
    @GetMapping("/")
    fun redirectToProjectList(): String {
        return "redirect:/project"
    }
}