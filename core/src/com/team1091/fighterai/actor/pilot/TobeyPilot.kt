package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.turnTowards

class TobeyPilot : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        val enemy = radar.contacts.find { it.faction.isEnemy(us.faction) }
        if (enemy != null) {
            // then we should fly towards them

            // 1) subtract our position from theirs to get offset (translate)
            // 2) rotate by the opposite rotation that we have
            val relativePosition = enemy.position.cpy().sub(us.position).mul(us.rotation.conjugate())

            // If they are above zero go up, otherwise go down
            val pitch = if (relativePosition.z > 0) 1f else -1f
            val yaw = if (relativePosition.x > 0) 1f else -1f

            val stickPosition = turnTowards(relativePosition)



            return PilotControl(
                pitch,
                yaw,
                0f,
                1f,
                false,
                false
            )


        }


//        us.position
//        us.rotation
//        us.velocity

        // This just does loops
        return PilotControl(
            1f,
            0f,
            0f,
            1f,
            false,
            false
        )
    }
}