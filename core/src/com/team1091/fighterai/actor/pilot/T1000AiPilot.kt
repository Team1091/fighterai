package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.math.FiringSolution
import com.team1091.fighterai.math.leadTarget
import com.team1091.fighterai.math.turnTowards
import com.team1091.fighterai.types.up
import kotlin.math.max

/*
 This is an example ai pilot
 */
class T1000AiPilot : Pilot {

    val groundProximityWarning = 30;
    val targetProximityWarning = 100f;
    val safeDistance = 300f;
    val primaryWeaponRange = 300f;
    val secondaryWeaponRange = 1000f;

    var lastTarget: Actor? = null
    var lastTargetTime: Long = 0
    var mode: AiState = AiState.ATTACK

    override fun fly(us: Actor, radar: Radar): PilotControl {
        // Are we diving into the ground?  Lets not.
        if (us.position.z < groundProximityWarning) {
           return pullUp(us);
        }

        val target = acquireTarget(us, radar)
        if (target == null) {
            // if we dont have a target, get in formation?
            return PilotControl(accelp = 1f)
        }

        val acceleration = calculateAcceleration(us, target)
        val (pitch, yaw, roll) = calculateFlightPath(us, target)
        val (firePrimaryWeapon, fireSecondaryWeapon) = calculateWeaponAction(us, target)

        return PilotControl(
                pitchp = pitch,
                yawp = yaw,
                rollp = roll,
                accelp = acceleration,
                primaryWeapon = firePrimaryWeapon,
                secondaryWeapon = fireSecondaryWeapon
        )
    }

    fun pullUp(us: Actor):PilotControl {
        // figure out how we are rotated, and which way up is.
        val localUp = up.cpy().mul(us.rotation.cpy().conjugate())
        var (pitch, yaw, roll) = turnTowards(localUp)

        Gdx.app.log(us.callsign, "Emergency pull up")
        return PilotControl(
                pitchp = pitch,
                yawp = yaw,
                rollp = roll,
                accelp = 1f,
                primaryWeapon = false,
                secondaryWeapon = false
        )
    }

    fun acquireTarget(us: Actor, radar: Radar):Actor? {
        val time = System.currentTimeMillis()
        val target = radar.blips
                .filter {
                    us.faction.isEnemy(it.faction)
                }
                .minByOrNull() {
                    it.position.dst(us.position)
                }

        // find a target, close and in front of us are good ideas
        if (lastTargetTime + 1000 < time) {
            lastTarget = target
            lastTargetTime = time
        }

        return target
    }

    fun calculateFlightPath(us: Actor, target: Actor): Triple<Float, Float, Float> {
        val distanceToTarget = us.position.dst(target.position);

        // If in attack mode and we are too close, change to retreat mode.
        // if we are in retreat mode and too far away, switch to attack mode
        val direction = when (mode) {
            AiState.ATTACK -> {
                if (distanceToTarget < targetProximityWarning)
                    mode = AiState.RETREAT
                1f
            }
            AiState.RETREAT -> {
                if (distanceToTarget > safeDistance)
                    mode = AiState.ATTACK
                -1f
            }
            AiState.EVADE -> {
                0f
            }
        }

        val targetTrajectory = targetTrajectory(us, target)
        val unRotatedTargetOffset = targetTrajectory.path.mul(us.rotation.cpy().conjugate())
        val (pitchp, yawp, rollp) = turnTowards(unRotatedTargetOffset)
        return Triple(pitchp * direction, yawp * direction, rollp * direction)
    }

    fun calculateAcceleration(us: Actor, target: Actor):Float {
        val distanceToTarget = us.position.dst(target.position);
        val targetTrajectory = targetTrajectory(us, target)
        val unRotatedTargetOffset = targetTrajectory.path.mul(us.rotation.cpy().conjugate())
        if (unRotatedTargetOffset.y > 0 && distanceToTarget < primaryWeaponRange && mode == AiState.ATTACK) {
            return 0.25f
        }

        return 1f
    }

    fun calculateWeaponAction(us: Actor, target: Actor):Pair<Boolean, Boolean> {
        val distanceToTarget = us.position.dst(target.position);
        val targetTrajectory = targetTrajectory(us, target)
        val unRotatedTargetOffset = targetTrajectory.path.mul(us.rotation.cpy().conjugate())
        if (unRotatedTargetOffset.y > 0) { // They are in front of  us
            // if we are close enough, shoot
            return Pair(distanceToTarget < primaryWeaponRange, distanceToTarget < secondaryWeaponRange)
        }

        return Pair(false, false)
    }

    fun targetTrajectory(us: Actor, target: Actor): FiringSolution {
        return leadTarget(
                us.position,
                target.position,
                Vector3(0f, target.velocity, 0f).mul(target.rotation),
                max(us.velocity, us.primaryWeapon?.getVelocity() ?: 3f)
        )
    }
}