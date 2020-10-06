package com.team1091.fighterai.actor


import com.team1091.fighterai.World

interface Collider {
    fun collision(world: World, us: Actor, actor: Actor)
}

class DamageCollider(val damage: Float) : Collider {
    override fun collision(world: World, us: Actor, actor: Actor) {
        actor.life?.takeDamage(world, actor, damage)
    }
}

class DamageAndDisappearCollider(val damage: Float) : Collider {
    override fun collision(world: World, us: Actor, actor: Actor) {
        actor.life?.takeDamage(world, actor, damage)
        world.removeActors.add(us)
    }
}