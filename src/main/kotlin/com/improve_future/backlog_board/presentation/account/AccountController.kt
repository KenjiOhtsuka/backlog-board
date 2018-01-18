package com.improve_future.backlog_board.presentation.account

import com.improve_future.backlog_board.domain.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("account")
class AccountController {
    @Autowired
    private lateinit var accountService: AccountService

    @RequestMapping("login", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun getLogin(request: HttpServletRequest): String {
        return AccountView.loginForm(request)
    }
}