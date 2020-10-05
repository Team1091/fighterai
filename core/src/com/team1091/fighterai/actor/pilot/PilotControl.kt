package com.team1091.fighterai.actor.pilot

data class PilotControl(
        val pitch: Float = 0f,
        val yaw: Float = 0f,
        val roll: Float = 0f,
        val accel: Float = 0f,
        val primaryWeapon: Boolean = false,
        val secondaryWeapon: Boolean = false
)