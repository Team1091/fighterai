package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.math.findInForwardArc
import com.team1091.fighterai.math.leadTarget
import com.team1091.fighterai.math.turnTowards
import com.team1091.fighterai.types.up
import kotlin.math.max

/*
 This is an example ai pilot
 */
class AiPilot : Pilot {

    var lastTarget: Actor? = null
    var lastTargetTime: Long = 0
    var mode: AiState = AiState.ATTACK

    override fun fly(fighterGame: FighterAIGame, us: Actor): PilotControl {

        // Are we diving into the ground?  Lets not.
        if (us.position.z < 30) {
            // figure out how we are rotated, and which way up is.
            val localUp = up.cpy().mul(us.rotation.cpy().conjugate())
            var (pitchp, yawp, rollp) = turnTowards(localUp)

            Gdx.app.log(us.callsign, "Emergency pull up")
            return PilotControl(
                    pitchp = pitchp,
                    yawp = yawp,
                    rollp = rollp,
                    accelp = 1f,
                    primaryWeapon = false,
                    secondaryWeapon = false
            )
        }

        val time = System.currentTimeMillis()
        val target: Actor?

        // find a target, close and in front of us are good ideas
        if (lastTargetTime + 1000 < time) {
            target = findInForwardArc(fighterGame, us = us, enemyOnly = true)
//            if(target==null){
//                Gdx.app.log(us.callsign, "Could not find Enemy")
//            }
            lastTarget = target
            lastTargetTime = time
        } else {
            target = lastTarget
        }


        if (target == null) {
            // if we dont have a target, get in formation?
            return PilotControl(accelp = 1f)
        }

        val dist = us.position.dst(target.position)

        // If in attack mode and we are too close, change to retreat mode.
        // if we are in retreat mode and too far away, switch to attack mode
        val towards = when (mode) {
            AiState.ATTACK -> {
                if (dist < 100f) mode = AiState.RETREAT
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
                max(us.velocity, us.primaryWeapon?.getVelocity() ?: 3f)
        )

        val unRotatedTargetOffset = solution.path.mul(us.rotation.cpy().conjugate())

        var (pitchp, yawp, rollp) = turnTowards(unRotatedTargetOffset)


        var primary = false
        var secondary = false
        var accelp:Float
        if (unRotatedTargetOffset.y > 0) { // They are in front of  us

            // if we are close enough, shoot
            if (dist < 1000) {
                secondary = true
            }

            if (dist < 300 && mode == AiState.ATTACK) {

                accelp = 0.25f
                primary = true

            } else {
                accelp = 1f
            }

        } else {
            if (mode == AiState.ATTACK) {
                accelp = 1f
            } else { // retreat
                accelp = 1f
            }

        }

        return PilotControl(
                pitchp = pitchp * towards,
                yawp = yawp * towards,
                rollp = rollp * towards,
                accelp = accelp,
                primaryWeapon = primary,
                secondaryWeapon = secondary
        )

    }

}


enum class AiState {
    ATTACK,
    RETREAT,
    EVADE,
}