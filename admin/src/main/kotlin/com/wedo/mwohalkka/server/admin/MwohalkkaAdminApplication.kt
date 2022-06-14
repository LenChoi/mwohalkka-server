package com.wedo.mwohalkka.server.admin

import com.wedo.mwohalkka.core.repository.BaseRepositoryImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class MwohalkkaAdminApplication

fun main(args: Array<String>) {
    runApplication<MwohalkkaAdminApplication>(*args)
}
