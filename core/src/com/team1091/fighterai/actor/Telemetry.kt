package com.team1091.fighterai.actor

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import java.util.*

class Telemetry(
        val worldId: UUID,
        val callsign: String,
        val position: Vector3,
        val rotation: Quaternion,
        var velocity: Float = 0f,
        val life: Float = 0f,
        val primaryWeaponVelocity: Float = 0f,
        val primaryWeaponDuration: Float = 0f,
        val primaryWeaponAmmo: Int = 0,
        val secondaryWeaponVelocity: Float = 0f,
        val secondaryWeaponDuration: Float = 0f,
        val secondaryWeaponAmmo: Int = 0,
        val faction: Faction = Faction.UNALIGNED,
        val radius: Float = 1f,
)