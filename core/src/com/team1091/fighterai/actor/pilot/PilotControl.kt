package com.team1091.fighterai.actor.pilot

data class PilotControl(
        val pitchp: Float = 0f,
        val yawp: Float = 0f,
        val rollp: Float = 0f,
        val accelp: Float = 0f,
        val primaryWeapon: Boolean = false,
        val secondaryWeapon: Boolean = false
)