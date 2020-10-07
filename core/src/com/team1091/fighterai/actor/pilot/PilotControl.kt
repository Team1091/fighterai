package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.math.limit

data class PilotControl(
        val pitchp: Float = 0f,
        val yawp: Float = 0f,
        val rollp: Float = 0f,
        val accelp: Float = 0f,
        val primaryWeapon: Boolean = false,
        val secondaryWeapon: Boolean = false
) {
    val pitch = limit(pitchp, -1f, 1f)
    val yaw = limit(yawp, -1f, 1f)
    val roll = limit(rollp, -1f, 1f)
    val accel = limit(accelp, -1f, 1f)
}
