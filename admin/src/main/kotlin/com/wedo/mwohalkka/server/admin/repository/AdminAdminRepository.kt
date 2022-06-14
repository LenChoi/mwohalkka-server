package com.wedo.mwohalkka.server.admin.repository

import com.wedo.mwohalkka.core.repository.BaseRepository
import com.wedo.mwohalkka.server.common.admin.domain.Admin

interface AdminAdminRepository : BaseRepository<Admin, Long> {
    fun findByLoginId(loginId: String): Admin?
}
