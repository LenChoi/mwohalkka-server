package com.wedo.mwohalkka.server.admin.admin.audit

import com.wedo.mwohalkka.server.admin.security.AdminContext
import com.wedo.mwohalkka.server.common.admin.domain.Admin
import com.wedo.mwohalkka.server.common.admin.domain.AdminRepository
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdminAwareAudit(
    private val adminRepository: AdminRepository
) : AuditorAware<Admin> {
    override fun getCurrentAuditor(): Optional<Admin> {
        if (AdminContext.isAuthenticated()) {
            val adminId = AdminContext.id()

            return Optional.of(adminRepository.getById(adminId))
        }

        return Optional.empty()
    }
}
