package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry

interface Pilot {
    fun fly(us: Telemetry, radar: Radar): PilotControl
}

class Tobey : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        // This just flies forward
        return PilotControl()
    }
}

