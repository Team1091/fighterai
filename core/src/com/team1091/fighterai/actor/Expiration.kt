package com.team1091.fighterai.actor

import com.team1091.fighterai.World

/*
 This component removes an actor after a period of time.
 It's useful for things like bullets that you don't want to go forever
 */
class Expiration(ttlMS: Long) {

    val timeOfDeath: Long = System.currentTimeMillis() + ttlMS

    fun check(world: World, actor: Actor) {
        if (System.currentTimeMillis() > timeOfDeath) {
            world.removeActors.add(actor)
        }
    }

}