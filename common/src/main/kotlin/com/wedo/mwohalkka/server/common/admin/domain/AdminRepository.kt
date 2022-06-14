package com.wedo.mwohalkka.server.common.admin.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findByLoginId(loginId: String): Admin?
}
