package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.leadTarget
import com.team1091.fighterai.math.turnTowards
import kotlin.math.max

// Steers a missile at an opponent
// Detailed instructions about how this works here:  https://www.youtube.com/watch?v=bZe5J8SVCYQ
class MissileGuidance(val target: Actor) : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        // else calculate where they will be, and fly towards that location
        val solution = leadTarget(
            us.position,
            target.position,
            Vector3(0f, target.velocity, 0f).mul(target.rotation),
            max(us.velocity, 5f)
        )

        val relativePosition = solution.path.mul(us.rotation.cpy().conjugate())

        val (pitch, yaw, roll) = turnTowards(relativePosition)

        return PilotControl(
            pitch = pitch,
            yaw = yaw,
            throttle = if (relativePosition.y > 0) 1f else 0.25f,
            // if we are close enough, detonate. velocity takes the place of range here
            secondaryWeapon = target.position.dst(us.position) < us.secondaryWeaponVelocity
        )
    }
}