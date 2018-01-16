package com.improve_future.backlog_board.domain.account.service

import com.improve_future.backlog_board.domain.backlog.factory.UserFactory
import com.improve_future.backlog_board.domain.backlog.model.User
import com.improve_future.backlog_board.domain.backlog.repository.AbstructGlobalBacklogRepository
import com.nulabinc.backlog4j.conf.BacklogConfigure
import com.nulabinc.backlog4j.conf.BacklogJpConfigure
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AccountService:
        AbstructGlobalBacklogRepository(),
        UserDetailsService,
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadUserDetails(token: PreAuthenticatedAuthenticationToken?): UserDetails {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun authenticate(spaceId: String, apiKey: String): User {
        return UserFactory.createFromBacklogUser(
                spaceId,
                buildClient(spaceId, apiKey).myself)
    }
}