package com.team1091.fighterai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.types.up

class CameraSystem(
        val world: World
) {
    val cam: PerspectiveCamera

    init {
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.near = 1f
        cam.far = 10000f
    }

    var currentTarget: Actor? = null
    val dolly = Dolly(
            pos = Vector3(0f, 0f, 1f),
            vel = Vector3(0f, 0f, 10f)
    )

    fun look(delta: Float) {

        // move the dolly
        dolly.pos.add(dolly.vel.cpy().scl(delta))

//        if (currentTarget == null) {
//            currentTarget = world.actors.first { it.respawnable }
//        }
        // analyze world, find a target
        val actor = currentTarget

        if (actor != null) {
            // This is a camera directly behind and above.

            val targetCameraPosition = actor.position.cpy().add(Vector3(0f, -5f, 1f).mul(actor.rotation))

            dolly.pos.set(targetCameraPosition)

            // val diff = targetCameraPosition.cpy().sub(actor.position)
            // dolly.vel.add(diff.nor())
            cam.lookAt(actor.position.cpy().add(Vector3(0f, 1000f, 0f).mul(actor.rotation)))
            cam.up.set(up.cpy().mul(actor.rotation))
            // cam.up.set(up)

        } else {
            // Look at from origin
            val anActor = world.actors.firstOrNull { it.life != null && it.life.cur > 0 }

            val averageHeight = world.actors.map { it.position.z }.average()

            if (cam.position.z < averageHeight - 10) {
                dolly.vel.set(0f, 0f, 10f)
            } else if (cam.position.z > averageHeight + 10) {
                dolly.vel.set(0f, 0f, -10f)
            } else {
                dolly.vel.set(0f, 0f, 0f)
            }

            cam.up.set(0f, 0f, 1f)

            if (anActor != null) {
                cam.lookAt(anActor.position)
            } else
                cam.lookAt(0f, 0f, 0f)


        }
        cam.position.set(dolly.pos)
        cam.update()
    }

    class Dolly(val pos: Vector3, val vel: Vector3)

    // TODO: black bars - titles
    // TODO: orbiting camera dolly - circles
    // TODO: static dolly
}

