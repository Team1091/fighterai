package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.RadarContact
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.angleBetween
import com.team1091.fighterai.math.leadTarget
import com.team1091.fighterai.math.turnTowards
import com.team1091.fighterai.types.forward
import com.team1091.fighterai.types.up

/*
 This is an example ai pilot
 */
class AdrianPrototypePilot : Pilot {

    private var mode: AiState = AiState.ATTACK
    private val groundProximityWarning = 100
    private val optimalHeight = 300

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        // Are we diving into the ground?  Lets not.
        if (us.position.z < groundProximityWarning) {
            // figure out how we are rotated, and which way up is.
            return pullUp(us)
        }

        // Is there an enemy missile coming at us?
        val incoming = radar.contacts.asSequence()
            .filter { it.faction.isEnemy(us.faction) }
            .filter { us.position.dst(it.position) < 100f }
            .filter { enemy ->
                val diff = us.position.cpy().sub(enemy.position)
                val aim = forward.cpy().mul(enemy.rotation)
                angleBetween(diff, aim) < 0.174533f
            }
            .minByOrNull { us.position.dst2(it.position) }

        if (incoming != null) {
            val enemyVector = forward.cpy().mul(incoming.rotation)
            val ourVector = forward.cpy().mul(incoming.rotation)

            return flyTowards(us, enemyVector.crs(ourVector))
        }


        val target: RadarContact? = acquireTarget(radar, us)

        if (target == null) {
            // There are no targets around, get above optimal height

            if (us.position.z < optimalHeight)
                return pullUp(us)
            else
                return flyTowards(us, forward)// if we dont have a target, get in formation?
        }

        val dist = us.position.dst(target.position)

        // If in attack mode and we are too close, change to retreat mode.
        // if we are in retreat mode and too far away, switch to attack mode
        val towards = when (mode) {
            AiState.ATTACK -> {
                if (dist < 100f)
                    mode = AiState.RETREAT
                1f
            }

            AiState.RETREAT -> {
                if (dist > 300f)
                    mode = AiState.ATTACK
                -1f
            }

            AiState.EVADE -> {
                0f
            }
        }

        // else calculate where they will be, and fly towards that location
        // This should just be used for lining up a target, at extreme distance we should just fly towards them
        val solution = leadTarget(
            us.position,
            target.position,
            Vector3(0f, target.velocity, 0f).mul(target.rotation),
            us.velocity + us.primaryWeaponVelocity
        )

        val unRotatedTargetOffset = solution.path.mul(us.rotation.cpy().conjugate())

        val angle = angleBetween(forward, unRotatedTargetOffset)
        val power = if (angle < 0.3) {
            angle * (1f / 0.3f)
        } else 1f

        val (pitch, yaw, roll) = turnTowards(unRotatedTargetOffset)


        var primary = false
        var secondary = false
        val accel: Float
        if (unRotatedTargetOffset.y > 0 && angle < 5 * degreesToRadians) { // They are in front of  us

            // if we are close enough, shoot
            if (us.secondaryWeaponAmmo > 0 && dist < us.secondaryWeaponDuration * us.secondaryWeaponVelocity) {
                secondary = true
            }

            if (us.primaryWeaponAmmo > 0 && dist < us.primaryWeaponDuration * us.primaryWeaponVelocity && mode == AiState.ATTACK) {
                accel = 0.25f
                primary = true
            } else {
                accel = 1f
            }

        } else {
            accel = 1f
        }

        return PilotControl(
            pitch = pitch * towards * power,
            yaw = yaw * towards * power,
            roll = roll * towards * power,
            throttle = accel,
            primaryWeapon = primary,
            secondaryWeapon = secondary
        )

    }

    private fun acquireTarget(radar: Radar, us: Telemetry): RadarContact? {
        // find a target, close and in front of us are good ideas
        return radar.contacts
            .filter {
                us.faction.isEnemy(it.faction)
            }
            .minByOrNull {
                it.position.dst(us.position)
            }
    }

    private fun flyTowards(us: Telemetry, heading: Vector3): PilotControl {

        val relativePosition = heading.cpy().mul(us.rotation.cpy().conjugate())
        val (pitch, yaw, roll) = turnTowards(relativePosition)

        return PilotControl(
            pitch = pitch,
            yaw = yaw,
            roll = roll,
            throttle = 1f,
            primaryWeapon = false,
            secondaryWeapon = false
        )

    }

    private fun pullUp(us: Telemetry): PilotControl {
        val localUp = up.cpy().mul(us.rotation.cpy().conjugate())
        val (pitch, yaw, roll) = turnTowards(localUp)

        Gdx.app.log(us.callsign, "Emergency pull up")
        return PilotControl(
            pitch = pitch,
            yaw = yaw,
            roll = roll,
            throttle = 1f,
            primaryWeapon = false,
            secondaryWeapon = false
        )
    }

}

enum class AiState {
    ATTACK,
    RETREAT,
    EVADE,
}