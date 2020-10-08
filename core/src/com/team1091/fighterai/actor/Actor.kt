package com.team1091.fighterai.actor

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.pilot.Pilot
import com.team1091.fighterai.actor.weapon.Weapon
import com.team1091.fighterai.types.AircraftType
import java.util.*
import java.util.UUID.randomUUID

open class Actor(
        val callsign: String,
        val position: Vector3,
        val rotation: Quaternion,
        var velocity: Float = 0f,
        model: Model,
        val pilot: Pilot? = null,
        val life: Life? = null,
        val primaryWeapon: Weapon? = null,
        val secondaryWeapon: Weapon? = null,
        val faction: Faction = Faction.UNALIGNED,
        val expiration: Expiration? = null,
        val radius: Float = 1f,
        val collider: Collider?,
        val respawnable: Boolean = false,
        val engine: Engine?,
        val explosive: Explosive? = null
) {
    val instance = ModelInstance(model)
    val worldId = randomUUID()

    fun toRadarContact(): RadarContact {
        return RadarContact(faction, position.cpy(), rotation.cpy(), velocity)
    }

    fun toTelemetry():Telemetry {
        return Telemetry(
                worldId,
                callsign,
                position.cpy(),
                rotation.cpy(),
                velocity,
                life?.cur ?: 0f,
                aircraftType,
                primaryWeapon?.getVelocity() ?: 0f,
                secondaryWeapon?.getVelocity() ?: 0f,
                faction,
                radius)
    }

    init {
        instance.transform.setToTranslation(position).rotate(rotation)
    }
}


