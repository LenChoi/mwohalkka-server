package com.wedo.mwohalkka.server.common.user.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class UserTest {
    @Test
    fun `유저 생성 성공`() {
        val user = User("test", "test@naver.com", "01012341234", LocalDate.now())

        assertThat(user.status).isEqualTo(UserStatus.CREATED)
        assertThat(user.createdAt).isNotNull
    }
}
