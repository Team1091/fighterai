package com.team1091.fighterai

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

interface IAudioManager {
    fun setLocation(location: Vector3, orientation: Quaternion)

    fun explode(location: Vector3)
    fun laser(location: Vector3)
    fun launch(location: Vector3)
    fun beepFail(location: Vector3)
}