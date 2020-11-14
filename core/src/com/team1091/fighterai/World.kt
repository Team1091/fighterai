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

const val gravity = -10f

class World(val audio: IAudioManager) {
    val actors = mutableListOf<Actor>()
    val newActors = mutableListOf<Actor>()
    val removeActors = mutableListOf<Actor>()
    var respawnActors = mutableListOf<Actor>()
    var otherGeometry = mutableListOf<ModelInstance>()

    var timePassed = 0f

    fun simulate(dt: Float) {
        timePassed += dt

        // Simulate
        for (craft in actors) {

            if (craft.pilot != null) {

                val visibleActors = findInForwardArc(this, craft)
                val pilotControl = craft.pilot.fly(craft.toTelemetry(), Radar(visibleActors.map { it.toRadarContact() }))

                // If we have an engine, control us
                craft.engine?.also { engine ->
                    craft.rotation.mul(Quaternion(up, -1f * pilotControl.yaw * engine.maxYaw * dt))
                    craft.rotation.mul(Quaternion(right, pilotControl.pitch * engine.maxPitch * dt))
                    craft.rotation.mul(Quaternion(forward, pilotControl.roll * engine.maxRoll * dt))
                    craft.rotation.nor()

                    val forwardVec = forward.cpy().mul(craft.rotation)
                    craft.velocity += (pilotControl.throttle * engine.maxAccel + forwardVec.z * gravity) * dt // Speed up
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

            with(craft) {
                velocity *= 1f - (craft.friction * dt) // Slow down, air resistance?

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

                // Find furthest player start
                val start = PlayerStart.values().maxByOrNull { playerStart ->
                    this.actors.map { actor -> playerStart.pos.dst(actor.position) }.minOrNull() ?: 0f
                }!!

                Gdx.app.log(it.callsign, start.name)
                it.position.set(start.pos)
                it.rotation.set(start.rotation)
                it.velocity = 300f

                if (it.life != null) {
                    it.life.cur = it.life.max
                }

                it.primaryWeapon?.refillAmmo()
                it.secondaryWeapon?.refillAmmo()

                it.respawns--

                actors.add(it)
            }
            respawnActors.clear()
        }
    }

    fun removeActor(worldId: UUID) {
        val actor = actors.firstOrNull { it.worldId == worldId }
        if (actor == null) {
            return
        }

        removeActors.add(actor)
    }
}