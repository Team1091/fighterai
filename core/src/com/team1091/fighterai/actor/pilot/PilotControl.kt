package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.math.limit

class PilotControl(
        pitch: Float = 0f,
        yaw: Float = 0f,
        roll: Float = 0f,
        accel: Float = 0f,
        val primaryWeapon: Boolean = false,
        val secondaryWeapon: Boolean = false
) {
    val pitch = limit(pitch, -1f, 1f)
    val yaw = limit(yaw, -1f, 1f)
    val roll = limit(roll, -1f, 1f)
    val accel = limit(accel, 0.25f, 1f)
}
