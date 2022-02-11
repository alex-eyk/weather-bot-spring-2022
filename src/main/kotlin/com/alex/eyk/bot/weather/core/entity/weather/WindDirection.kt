package com.alex.eyk.bot.weather.core.entity.weather

import java.util.NavigableMap
import java.util.TreeMap

enum class WindDirection(private val degrees: Float) {

    NORTH(0f),
    NORTH_EAST(45f),
    EAST(90f),
    SOUTH_EAST(135f),
    SOUTH(180f),
    SOUTH_WEST(225f),
    WEST(270f),
    NORTH_WEST(315f);

    companion object {

        private val navigableMap: NavigableMap<Float, WindDirection> = TreeMap()

        init {
            for (direction in values()) {
                navigableMap[direction.degrees] = direction
            }
        }

        fun ofDegrees(degrees: Float): WindDirection {
            val nearestAhead = navigableMap.ceilingEntry(degrees)
            val nearestRear = navigableMap.floorEntry(degrees)
            return if ((degrees - nearestRear.key) < (nearestAhead.key - degrees)) {
                nearestRear.value
            } else {
                nearestAhead.value
            }
        }
    }

    fun directionName(): String {
        return this.name.lowercase()
    }
}