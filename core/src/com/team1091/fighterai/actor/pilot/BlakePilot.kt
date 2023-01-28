package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry

class BlakePilot : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        // This just does loops
        return PilotControl(
            1f,
            0f,
            0f,
            1f,
            true,
            true
        )
    }
}