package com.team1091.fighterai

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

const val size = 1000f

enum class PlayerStart(val pos: Vector3, val rotation: Quaternion) {

    WEST(Vector3(-size, 0f, 200f), Quaternion().setEulerAngles(0f, 0f, -90f)),
    EAST(Vector3(size, 0f, 200f), Quaternion().setEulerAngles(0f, 0f, 90f)),
    SOUTH(Vector3(0f, -size, 200f), Quaternion().setEulerAngles(0f, 0f, 0f)),
    NORTH(Vector3(0f, size, 200f), Quaternion().setEulerAngles(0f, 0f, 180f)),
}