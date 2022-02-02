package com.alex.eyk.bot.weather.core.entity.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun getByChat(chat: Long): User?

}