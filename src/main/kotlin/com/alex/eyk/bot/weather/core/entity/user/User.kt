package com.alex.eyk.bot.weather.core.entity.user

import com.alex.eyk.bot.weather.core.entity.Activity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(

    @Id
    @Column(name = "`chat`", unique = true, nullable = false)
    var chat: Long,

    @Column(name = "`enabled`", nullable = false)
    var enabled: Boolean = true,

    @Column(name = "`activity`", nullable = false)
    var activity: Activity = Activity.NONE

)