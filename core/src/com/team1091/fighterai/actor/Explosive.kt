package com.team1091.fighterai.actor

import com.team1091.fighterai.World

class Explosive(
        val damage: Float,
        val explosionRadius: Float,
        var detonate: Boolean = false
) {
    fun act(us: Actor, world: World) {
        if (!detonate)
            return

        world.actors
                .filter { it.position.dst(us.position) < explosionRadius }
                .forEach { it.life?.takeDamage(world, it, damage) }

        // destroy ourselves
        world.removeActors.add(us)
    }
}