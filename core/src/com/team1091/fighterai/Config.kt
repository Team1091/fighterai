package com.team1091.fighterai

import com.team1091.fighterai.actor.pilot.AdrianPrototypePilot
import com.team1091.fighterai.types.CameraType

// This will be where we set out default ai class.  Try not to commit this file.
// Construct your ship ai here. This will probably look like   { YourClassname() }
val buildPlayerShip = { AdrianPrototypePilot() } //KeyboardPilot

// This constructs an opposition ai.  Right now it uses your own, but we can set it to vs others later in the dev process
val buildAdversarialShip = { AdrianPrototypePilot() }


//val cameraType = CameraType.GROUND
//val cameraType = CameraType.CHASE
val cameraType = CameraType.CHASE_TARGET

val fullScreen = false

val showHud = true