package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.mappings.Xbox
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.math.accel
import com.team1091.fighterai.math.deaden

// Pilot with an old XBOX controller
class HumanPilot(val controller: Controller) : Pilot {
    override fun fly(us: Actor, radar: Radar): PilotControl {
        with(us) {

            if (engine == null) {
                return PilotControl()
            }

            return PilotControl(
                    accel(-controller.getAxis(Xbox.L_STICK_VERTICAL_AXIS)),
                    deaden(controller.getAxis(Xbox.L_STICK_HORIZONTAL_AXIS)),
                    deaden(controller.getAxis(Xbox.R_STICK_VERTICAL_AXIS)),
                    deaden(controller.getAxis(Xbox.R_STICK_HORIZONTAL_AXIS)),
                    primaryWeapon = controller.getButton(Xbox.R_BUMPER),
                    secondaryWeapon = controller.getButton(Xbox.L_BUMPER)
            )
        }
    }
}
