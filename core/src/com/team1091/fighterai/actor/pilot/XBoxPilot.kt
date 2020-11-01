package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.mappings.Xbox
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry

import kotlin.math.abs
import kotlin.math.pow

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


    fun accel(x: Float) = (0.3f * (x + 1f).pow(2f)) - 0.2f

    fun deaden(value: Float): Float {
        return value * abs(value)
    }
}
