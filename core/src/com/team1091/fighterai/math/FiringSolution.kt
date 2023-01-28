package com.team1091.fighterai.math

import com.badlogic.gdx.math.Vector3

/*
This is used to figure out when and where to fire
 */
data class FiringSolution(
    val aimSpot: Vector3, // This is where we need to aim to hit them
    val path: Vector3,    // this is the offset to the aim spot
    val timeToImpact: Float // estimated time to impact
)