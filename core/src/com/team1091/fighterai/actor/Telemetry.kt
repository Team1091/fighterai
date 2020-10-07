package com.team1091.fighterai.actor

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.types.AircraftType
import java.util.*

class Telemetry(
        val worldId: UUID,
        val callsign:String,
        val position: Vector3,
        val rotation: Quaternion,
        var velocity: Float = 0f,
        val life: Float = 0f,
        val aircraftType: AircraftType? = null,
        val primaryWeaponVelocity: Float = 0f,
        val secondaryWeaponVelocity: Float = 0f,
        val faction: Faction = Faction.UNALIGNED,
        val radius: Float = 1f,
) {
}