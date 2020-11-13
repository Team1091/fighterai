package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.turnTowards

class AdrianPilot : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        val enemy = radar.contacts.find { it.faction.isEnemy(us.faction) }
        if (enemy != null) {
            // then we should fly towards them

            // 1) subtract our position from theirs to get offset (translate)
            // 2) rotate by the opposite rotation that we have
            val relativePosition = enemy.position.cpy().sub(us.position).mul(us.rotation.conjugate())

            // If they are above zero go up, otherwise go down
            // val pitch = if (relativePosition.z > 0) 1f else -1f
            // val yaw = if (relativePosition.x > 0) 1f else -1f

            // This is the same as above, but uses a fuction to hide the details
            val stickPosition = turnTowards(relativePosition)

            return PilotControl(
                    stickPosition.pitch,
                    stickPosition.yaw,
                    stickPosition.roll,
                    1f,
                    false,
                    false
            )
        }

        // This just does loops
        return PilotControl(
                0f,
                0f,
                1f,
                1f,
                false,
                false
        )
    }
}