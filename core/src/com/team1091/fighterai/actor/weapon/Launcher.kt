package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.types.*

abstract class Launcher(
        val refireMS: Long,
        val launchVelocity: Float,
        val tubeOffsets: List<Vector3> = listOf(
                right.cpy().scl(1f).add(forward),
                left.cpy().scl(1f).add(forward),
                up.cpy().scl(1f).add(forward),
                down.cpy().scl(1f).add(forward)
        )
) : Weapon {

    var lastFired: Long = 0
    var nextTube = 0

    override fun fire(fighterGame: FighterAIGame, shooter: Actor) {
        val millis = System.currentTimeMillis()
        if (lastFired + refireMS < millis) {

            val offset = tubeOffsets[nextTube++].cpy().mul(shooter.rotation)

            if (nextTube >= tubeOffsets.size)
                nextTube = 0

            project(
                    fighterGame,
                    shooter,
                    shooter.position.cpy().add(offset),
                    shooter.rotation.cpy(),
                    shooter.velocity + launchVelocity
            )
            Gdx.app.log(shooter.callsign, "Fired a missile")
            lastFired = millis
        }
    }

}