package com.wedo.mwohalkka.server.common.admin.domain

import org.springframework.security.core.GrantedAuthority

enum class AdminRole(var descr: String) : GrantedAuthority {
    ADMIN("슈퍼 어드민"),

    SUPER("슈퍼 어드민");

    override fun getAuthority(): String {
        return "ROLE_$name"
    }
}
