package com.wedo.mwohalkka.server.admin.security

import com.wedo.mwohalkka.server.admin.admin.domain.AdminUser
import org.springframework.security.core.context.SecurityContextHolder

/**
 * 어드민 로그인 정보 컨텍스트
 */
class AdminContext {
    companion object {
        fun user(): AdminUser {
            return SecurityContextHolder.getContext().authentication.principal as AdminUser
        }

        fun userName(): String = user().username

        fun id(): Long = user().id!!

        fun isAuthenticated(): Boolean = SecurityContextHolder.getContext().authentication != null
    }
}
