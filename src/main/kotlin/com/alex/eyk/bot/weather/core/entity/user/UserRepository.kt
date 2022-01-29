package com.alex.eyk.bot.weather.core.entity.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    fun findByChat(chat: Long): Optional<User>

}