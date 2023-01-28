package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor

/**
 * Self destruction device, used in rockets and bombs
 */
class Explosive(
    val damage: Float,
    val explosionRadius: Float
) : Weapon {

    override fun fire(world: World, shooter: Actor) {
        project(world, shooter, shooter.position, shooter.rotation, shooter.velocity)
    }

    override fun project(world: World, shooter: Actor, position: Vector3, rotation: Quaternion, velocity: Float) {
        world.actors
            .filter { it.position.dst(shooter.position) < explosionRadius }
            .forEach { it.life?.takeDamage(world, it, damage) }

        // destroy ourselves
        world.removeActors.add(shooter)
    }


    override fun getVelocity(): Float = explosionRadius

    override fun getDuration(): Float = 1f
    override fun getAmmo(): Int = 1
    override fun refillAmmo() {}

}