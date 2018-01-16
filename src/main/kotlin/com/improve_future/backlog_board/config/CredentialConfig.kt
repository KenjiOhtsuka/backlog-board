package com.improve_future.backlog_board.config

import com.improve_future.backlog_board.domain.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AccountStatusUserDetailsChecker
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component

@Configuration
@EnableWebSecurity
@Order
open class CredentialConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit private var userAuthenticationProvider: UserAuthenticationProvider

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().
                antMatchers("/static/**").
                antMatchers("/backlog/**")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.
//                    antMatcher("*").
//                    authorizeRequests().anyRequest().permitAll().
                //antMatcher("/account/login").httpBasic().
                //and()
//                    authorizeRequests().
//                    anyRequest().
//                    authenticated()
//                .antMatchers("/account/login").permitAll()
//                .antMatchers("/account/forgot").permitAll()
//                .antMatchers("/account/confirm").permitAll()
//                .antMatchers("/account/**").authenticated()
                //.and()
                formLogin().
                loginPage("/account/login").
                usernameParameter("space_id").
                passwordParameter("api_key").
                defaultSuccessUrl("/project").
                failureUrl("/account/login?error").
                permitAll().
                and().
                logout().
                logoutRequestMatcher(
                        AntPathRequestMatcher(
                                "/account/logout")).
                logoutSuccessUrl("/account/login").
                deleteCookies("JSESSIONID").
                invalidateHttpSession(true).
                permitAll().
                and().
                authenticationProvider(userAuthenticationProvider).
                authorizeRequests().anyRequest().authenticated()
        //and().
        http.csrf().
                requireCsrfProtectionMatcher(
                        AntPathRequestMatcher("*"))
    }

    @Autowired
    lateinit private var accountService: AccountService

    @Bean
    open fun preAuthenticatedAuthenticationProvider(): PreAuthenticatedAuthenticationProvider {
        val provider = PreAuthenticatedAuthenticationProvider()
        provider.setPreAuthenticatedUserDetailsService(accountService)
        provider.setUserDetailsChecker(AccountStatusUserDetailsChecker())
        return provider
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(preAuthenticatedAuthenticationProvider())
    }

    @Component
    class UserAuthenticationProvider: AuthenticationProvider {
        @Autowired
        lateinit var accountService: AccountService

        override fun supports(authentication: Class<*>?): Boolean {
            return true
        }

        override fun authenticate(
                authentication: Authentication?): Authentication {
            val loginCodeAndPassword = authentication as UsernamePasswordAuthenticationToken
            val user = accountService.authenticate(
                    loginCodeAndPassword.principal.toString(),
                    loginCodeAndPassword.credentials.toString())

            user ?: throw Exception("Invalid User.")

            return UsernamePasswordAuthenticationToken(
                    user, null, user.authorities)
        }
    }
}
