package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.math.leadTarget
import com.team1091.fighterai.math.turnTowards
import kotlin.math.max

// Steers a missile at an opponent
class MissileGuidance(val target: Actor) : Pilot {

    override fun fly(us: Actor, radar: Radar): PilotControl {
        // if we are close enough, detonate
        if (us.explosive != null && target.position.dst(us.position) < us.explosive.explosionRadius) {
            us.explosive.detonate = true
        }

        // else calculate where they will be, and fly towards that location
        val solution = leadTarget(
                us.position,
                target.position,
                Vector3(0f, target.velocity, 0f).mul(target.rotation),
                max(us.velocity, 5f)
        )

        val unRotatedTargetOffset = solution.path.mul(us.rotation.cpy().conjugate())

        var (pitch, yaw, roll) = turnTowards(unRotatedTargetOffset)

        return PilotControl(
                pitch = pitch,
                yaw = yaw,
                accel = if (unRotatedTargetOffset.y > 0) 1f else 0.25f
        )
    }
}