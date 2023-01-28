package com.team1091.fighterai.actor.pilot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry

// Pilot with a keyboard
class KeyboardPilot : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        val w = Gdx.input.isKeyPressed(Input.Keys.W)
        val a = Gdx.input.isKeyPressed(Input.Keys.A)
        val s = Gdx.input.isKeyPressed(Input.Keys.S)
        val d = Gdx.input.isKeyPressed(Input.Keys.D)

        val up = Gdx.input.isKeyPressed(Input.Keys.UP)
        val left = Gdx.input.isKeyPressed(Input.Keys.LEFT)
        val down = Gdx.input.isKeyPressed(Input.Keys.DOWN)
        val right = Gdx.input.isKeyPressed(Input.Keys.RIGHT)

        val space = Gdx.input.isKeyPressed(Input.Keys.SPACE)
        val shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)

        return PilotControl(
            throttle = (if (up) 1f else 0.5f) + (if (down) -0.5f else 0f),
            yaw = (if (left) -1f else 0f) + (if (right) 1f else 0f),
            pitch = (if (s) 1f else 0f) + (if (w) -1f else 0f),
            roll = (if (a) -1f else 0f) + (if (d) 1f else 0f),
            primaryWeapon = space,
            secondaryWeapon = shift
        )
    }
}
