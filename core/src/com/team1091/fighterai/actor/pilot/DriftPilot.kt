package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.actor.Actor
import java.util.*

// This is just a pilot that's out of control
class DriftPilot : Pilot {

    val control = PilotControl(
            pitch = Random().nextFloat(),
            yaw = Random().nextFloat(),
            roll = Random().nextFloat()
    )

    override fun fly(fighterGame: FighterAIGame, us: Actor) = control

}