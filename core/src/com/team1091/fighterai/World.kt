package com.team1091.fighterai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.math.findInForwardArc
import com.team1091.fighterai.types.forward
import com.team1091.fighterai.types.right
import com.team1091.fighterai.types.up
import java.util.*
import kotlin.math.pow

class World {

    val actors = mutableListOf<Actor>()
    val newActors = mutableListOf<Actor>()
    val removeActors = mutableListOf<Actor>()
    var respawnActors = mutableListOf<Actor>()

    var otherGeometry = mutableListOf<ModelInstance>()


    fun simulate(dt: Float) {
        // Simulate
        for (craft in actors) {

            if (craft.pilot != null) {

                val visibleActors = findInForwardArc(this, craft)
                val pilotControl = craft.pilot.fly(craft, Radar(visibleActors.map { it.toRadarContact() }))

                // If we have an engine, control us
                craft.engine?.also { engine ->
                    craft.rotation.mul(Quaternion(up, -1f * pilotControl.yaw * engine.maxYaw * dt))
                    craft.rotation.mul(Quaternion(right, pilotControl.pitch * engine.maxPitch * dt))
                    craft.rotation.mul(Quaternion(forward, pilotControl.roll * engine.maxRoll * dt))
                    craft.velocity += pilotControl.accel * engine.maxAccel * dt // Speed up
                }

                // if primary shoot
                if (pilotControl.primaryWeapon) {
                    craft.primaryWeapon?.fire(this, craft)
                }

                // if secondary shoot
                if (pilotControl.secondaryWeapon) {
                    craft.secondaryWeapon?.fire(this, craft)
                }
            }
            craft.explosive?.act(craft, this)

            with(craft) {
                velocity *= 1f - (0.8f * dt) // Slow down, air resistance?

                position.add(Vector3(0f, velocity * dt, 0f).mul(rotation))
                instance.transform.setToTranslation(position).rotate(rotation)
            }

            if (craft.position.z < 0) { // ground hit
                craft.life?.die(this, craft)
            }

            if (craft.expiration != null) {
                craft.expiration.check(this, craft)
            }
        }

        // this will be inefficient, we may need to fix that if we want to support large battles
        for (i in (0 until actors.size)) {
            for (j in (i + 1 until actors.size)) {
                // test collision
                if (actors[i].position.dst2(actors[j].position) <= (actors[i].radius + actors[j].radius).pow(2)) {
                    // collide
                    actors[i].collider?.collision(this, actors[i], actors[j])
                    actors[j].collider?.collision(this, actors[j], actors[i])
                }
            }
        }

        // to prevent concurrent modification exceptions
        actors.addAll(newActors)
        newActors.clear()

        if (removeActors.isNotEmpty()) {
            actors.removeAll(removeActors)
            removeActors.clear()
        }

        if (respawnActors.isNotEmpty()) {
            respawnActors.forEach {

                val start = PlayerStart.values()[Random().nextInt(PlayerStart.values().size)]
                Gdx.app.log(it.callsign, start.name)
                it.position.set(start.pos)
                it.rotation.set(start.rotation)
                it.velocity = 300f
                if (it.life != null) {
                    it.life.cur = it.life.max
                }


                actors.add(it)
            }
            respawnActors.clear()
        }
    }

}