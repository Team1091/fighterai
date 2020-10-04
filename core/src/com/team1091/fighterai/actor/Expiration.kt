package com.team1091.fighterai.actor

import com.team1091.fighterai.FighterAIGame

/*
 This component removes an actor after a period of time.
 It's useful for things like bullets that you don't want to go forever
 */
class Expiration(ttlMS: Long) {

    val timeOfDeath: Long = System.currentTimeMillis() + ttlMS

    fun check(fighterGame: FighterAIGame, actor: Actor) {
        if (System.currentTimeMillis() > timeOfDeath) {
            fighterGame.removeActors.add(actor)
        }
    }

}