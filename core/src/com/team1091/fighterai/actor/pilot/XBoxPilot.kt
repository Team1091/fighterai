package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.mappings.Xbox
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.accel
import com.team1091.fighterai.math.deaden

// Pilot with an old XBOX controller
class XBoxPilot(val controller: Controller) : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        return PilotControl(
                throttle = accel(-controller.getAxis(Xbox.L_STICK_VERTICAL_AXIS)),
                yaw = deaden(controller.getAxis(Xbox.L_STICK_HORIZONTAL_AXIS)),
                pitch = deaden(controller.getAxis(Xbox.R_STICK_VERTICAL_AXIS)),
                roll = deaden(controller.getAxis(Xbox.R_STICK_HORIZONTAL_AXIS)),
                primaryWeapon = controller.getButton(Xbox.R_BUMPER),
                secondaryWeapon = controller.getButton(Xbox.L_BUMPER)
        )
    }
}
