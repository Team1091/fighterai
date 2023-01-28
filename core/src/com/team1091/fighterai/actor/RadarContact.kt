package com.team1091.fighterai.actor

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

class RadarContact(
    val faction: Faction,
    val position: Vector3,
    val rotation: Quaternion,
    var velocity: Float = 0f,
)