package com.team1091.fighterai.actor

import com.badlogic.gdx.Gdx
import com.team1091.fighterai.World

/*
This keeps track of how much life the object has.
You need this to take damage, for how do you kill that which has no life?
 */
class Life(val max: Float, var cur: Float = max) {

    fun takeDamage(world: World, actor: Actor, float: Float) {
        Gdx.app.log(actor.callsign, "Took $float damage")
        cur -= float

        if (cur <= 0) {
            die(world, actor)
        }
    }

    fun die(world: World, actor: Actor) {
        // TODO: sound
        //world.audio.explode()
        world.removeActors.add(actor)
        if (actor.respawns > 0) {
            world.respawnActors.add(actor)
        }
        Gdx.app.log(actor.callsign, "Exploded")
    }

}