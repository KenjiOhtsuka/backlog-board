package com.improve_future.backlog_board.domain.backlog.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import com.nulabinc.backlog4j.User as BacklogUser

class User: AbstractBacklog(), UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return AuthorityUtils.createAuthorityList("ROLE_USER")
    }

    override fun isEnabled() = true
    override fun getUsername() = name
    override fun isCredentialsNonExpired() = true
    override fun getPassword(): String {
        return ""
    }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true

    var id: Long? = null
    var key: String? = null
    var name: String? = null
    var emailAddress: String? = null
    var role: BacklogUser.RoleType? = null
    var apiKey: String? = null

    var userId: String = ""
    val urlString: String by lazy {
        "https://$spaceKey.backlog.jp/user/$key"
    }
}