package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.RadarContact
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.angleBetween
import com.team1091.fighterai.math.leadTarget
import com.team1091.fighterai.math.turnTowards
import com.team1091.fighterai.types.forward
import com.team1091.fighterai.types.up
import kotlin.math.max

/*
 This is an example ai pilot
 */
class AiPilot : Pilot {

    var lastTarget: RadarContact? = null
    var lastTargetTime: Long = 0
    var mode: AiState = AiState.ATTACK
    val groundProximityWarning = 30
    val optimalHeight = 300

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        // Are we diving into the ground?  Lets not.
        if (us.position.z < groundProximityWarning) {
            // figure out how we are rotated, and which way up is.
            return pullUp(us)
        }

        val target: RadarContact? = acquireTarget(radar, us)


        if (target == null) {

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

//                    else if (us beingAimedAtBy target)
//                        mode = AiState.EVADE

                -1f
            }
            AiState.EVADE -> {
                0f
            }
        }

        // else calculate where they will be, and fly towards that location
        val solution = leadTarget(
                us.position,
                target.position,
                Vector3(0f, target.velocity, 0f).mul(target.rotation),
                max(us.velocity, us.primaryWeaponVelocity)
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
        if (unRotatedTargetOffset.y > 0) { // They are in front of  us

            // if we are close enough, shoot
            if (us.secondaryWeaponAmmo > 0 && dist < us.secondaryWeaponDuration * us.secondaryWeaponVelocity) {
                secondary = true
            }

            if (dist < us.primaryWeaponDuration * us.primaryWeaponVelocity && mode == AiState.ATTACK) {
                accel = 0.25f
                primary = us.primaryWeaponAmmo > 0

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
                accel = accel,
                primaryWeapon = primary,
                secondaryWeapon = secondary
        )

    }

    private fun acquireTarget(radar: Radar, us: Telemetry): RadarContact? {
        val time = System.currentTimeMillis()
        val target: RadarContact?

        // find a target, close and in front of us are good ideas
        if (lastTargetTime + 1000 < time) {
            target = radar.contacts
                    .filter {
                        us.faction.isEnemy(it.faction)
                    }
                    .minByOrNull {
                        it.position.dst(us.position)
                    }

            lastTarget = target
            lastTargetTime = time
        } else {
            target = lastTarget
        }
        return target
    }

    private fun flyTowards(us: Telemetry, heading: Vector3): PilotControl {

        val localUp = heading.cpy().mul(us.rotation.cpy().conjugate())
        val (pitch, yaw, roll) = turnTowards(localUp)

        return PilotControl(
                pitch = pitch,
                yaw = yaw,
                roll = roll,
                accel = 1f,
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
                accel = 1f,
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