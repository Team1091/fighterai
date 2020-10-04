package com.team1091.fighterai.actor

import com.badlogic.gdx.Gdx
import com.team1091.fighterai.FighterAIGame

/*
This keeps track of how much life the object has.
You need this to take damage, for how do you kill that which has no life?
 */
class Life(val max: Float, var cur: Float = max) {

    fun takeDamage(fighterGame: FighterAIGame, actor: Actor, float: Float) {
        Gdx.app.log("Aircraft", "${actor.callsign} took $float damage")
        cur -= float

        if (cur <= 0) {
            die(fighterGame, actor)
        }
    }

    fun die(fighterGame: FighterAIGame, actor: Actor) {
        fighterGame.audio.explode()
        fighterGame.removeActors.add(actor)
        if (actor.respawnable) {
            fighterGame.respawnActors.add(actor)
        }
        Gdx.app.log("Destruction", "Target Exploded")
    }

}