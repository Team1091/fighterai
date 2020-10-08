package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import java.util.*

// This is just a pilot that's out of control
class DriftPilot : Pilot {
    val control = PilotControl(
            Random().nextFloat(),
            Random().nextFloat(),
            Random().nextFloat()
    )

    override fun fly(us: Actor, radar: Radar) = control
}