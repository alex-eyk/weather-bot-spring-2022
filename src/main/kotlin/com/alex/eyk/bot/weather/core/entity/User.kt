package com.alex.eyk.bot.weather.core.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(

    @Id
    @Column(name = "`chat`", unique = true, nullable = false)
    var chat: Long,

    @Column(name = "`enabled`", nullable = false)
    var enabled: Boolean

)