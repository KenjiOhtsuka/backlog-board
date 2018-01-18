package com.improve_future.backlog_board.base

import com.improve_future.backlog_board.domain.backlog.model.User
import org.springframework.security.core.context.SecurityContextHolder

object SecurityContextHolder {
    fun getCurrentUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }
}