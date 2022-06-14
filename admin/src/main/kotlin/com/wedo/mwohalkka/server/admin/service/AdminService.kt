package com.wedo.mwohalkka.server.admin.service

import com.wedo.mwohalkka.core.service.GenericService
import com.wedo.mwohalkka.core.service.GenericServiceImpl
import com.wedo.mwohalkka.server.admin.repository.AdminAdminRepository
import com.wedo.mwohalkka.server.common.admin.domain.AdminRole
import com.wedo.mwohalkka.server.admin.admin.domain.AdminUser
import com.wedo.mwohalkka.server.common.admin.domain.Admin
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import javax.annotation.PostConstruct

interface AdminService : GenericService<Admin, Long, AdminAdminRepository>, UserDetailsService {
    override fun add(admin: Admin): Admin

    override fun update(admin: Admin): Admin

    override fun loadUserByUsername(username: String): UserDetails
}

@Service
class DefaultAdminService(
    val passwordEncoder: PasswordEncoder
) : GenericServiceImpl<Admin, Long, AdminAdminRepository>(), AdminService {

    @PostConstruct
    fun init() {
        repository.findByLoginId("admin") ?: run {
            val admin: Admin = Admin(
                "admin",
                "디폴트 어드민",
                passwordEncoder.encode("meta1234"),
                "821000000000",
                "cc_slack_id",
                roles = listOf(AdminRole.SUPER),
            )
            repository.save(admin)
        }
    }

    override fun add(admin: Admin): Admin {
        admin.password = passwordEncoder.encode(admin.password)

        return super.add(admin)
    }

    override fun update(admin: Admin): Admin {
        if (StringUtils.hasText(admin.passwordHolder)) {
            admin.password = passwordEncoder.encode(admin.passwordHolder)
        }

        return super.update(admin)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findByLoginId(username)

        user?.run { return AdminUser(user) }
            ?: throw UsernameNotFoundException("$username not found")
    }
}
