package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.angleBetween
import com.team1091.fighterai.types.forward

interface Pilot {
    fun fly(us: Telemetry, radar: Radar): PilotControl
}
