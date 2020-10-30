package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.types.forward
import kotlin.math.abs

class HarshPilot1 : Pilot {

    var iterator = 0




    override fun fly(us: Telemetry, radar: Radar): PilotControl {
        if(abs(forward.cpy().mul(us.rotation).z) <= 0.01f){
            iterator = iterator + 1
        }

        var pitch = 0f
        if (iterator <= 4) {
            pitch = 1f
        }
        if (iterator >= 4) {
            pitch = 0f

        }


        // This just does loops
        return PilotControl(
                pitch,
                0f,
                0f,
                1f,
                false,
                false
        )
    }
}