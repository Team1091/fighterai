package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor

abstract class Launcher(
        val refireMS: Long,
        val launchVelocity: Float,
        val maxAmmoCount: Int,
        val tubeOffsets: List<Vector3> = listOf(
                Vector3(1f, 2f, 0f),
                Vector3(-1f, 2f, 0f)
//                right.cpy().scl(2f).add(forward),
//                left.cpy().scl(2f).add(forward),
//                up.cpy().scl(2f).add(forward),
//                down.cpy().scl(2f).add(forward)
        )
) : Weapon {

    private var lastFired: Long = 0
    private var nextTube = 0
    private var ammoCount = maxAmmoCount

    override fun fire(world: World, shooter: Actor) {
        val millis = System.currentTimeMillis()

        if (ammoCount <= 0) {
            return
        }

        if (lastFired + refireMS < millis) {
            ammoCount--
            val offset = tubeOffsets[nextTube++].cpy().mul(shooter.rotation)

            if (nextTube >= tubeOffsets.size)
                nextTube = 0

            project(
                    world,
                    shooter,
                    shooter.position.cpy().add(offset),
                    shooter.rotation.cpy(),
                    shooter.velocity + launchVelocity
            )

            lastFired = millis
        }
    }

    override fun getAmmo(): Int = ammoCount
    override fun refillAmmo() {
        ammoCount = maxAmmoCount
    }
}