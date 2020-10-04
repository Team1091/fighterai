package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.actor.*
import com.team1091.fighterai.actor.pilot.MissileGuidance
import com.team1091.fighterai.actor.pilot.beingAimedAtBy
import com.team1091.fighterai.types.MissileType

// Fires homing ordinance
class MissileRack(val missileType: MissileType) : Launcher(
        missileType.refireMS,
        missileType.launchVelocity
) {
    override fun project(fighterGame: FighterAIGame, shooter: Actor, position: Vector3, rotation: Quaternion, velocity: Float) {

        // find a target
        val target: Actor? = fighterGame.actors.asSequence()
                .filter { it.engine != null && it != shooter }
                .filter { it.faction.isEnemy(shooter.faction) }
                .filter { it beingAimedAtBy shooter }
                .minByOrNull { shooter.position.dst2(it.position) }

        if (target == null) {
            // fail to fire - should only do this sound to players
//            fighterGame.audio.beepFail()
            return
        }
        // fire
        fighterGame.audio.launch()

        fighterGame.newActors.add(Actor(
                callsign = "${shooter.callsign}'s ${missileType.name}",
                position = position,
                rotation = rotation,
                velocity = velocity,
                model = missileType.model,
                pilot = MissileGuidance(target, missileType),
                life = Life(1f),
                expiration = Expiration(missileType.expiration),
                radius = missileType.radius,
                collider = DamageCollider(1f),
                engine = missileType.engine
        ))

    }

    override fun getVelocity(): Float = launchVelocity
}