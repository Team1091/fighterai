package com.team1091.fighterai.actor

import com.team1091.fighterai.FighterAIGame

interface Collider {
    fun collision(fighterGame: FighterAIGame, us: Actor, actor: Actor)
}

class DamageCollider(val damage: Float) : Collider {
    override fun collision(fighterGame: FighterAIGame, us: Actor, actor: Actor) {
        actor.life?.takeDamage(fighterGame, actor, damage)
    }
}

class DamageAndDisappearCollider(val damage: Float) : Collider {
    override fun collision(fighterGame: FighterAIGame, us: Actor, actor: Actor) {
        actor.life?.takeDamage(fighterGame, actor, damage)
        fighterGame.removeActors.add(us)
    }
}