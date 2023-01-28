package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageCollider
import com.team1091.fighterai.actor.Expiration
import com.team1091.fighterai.actor.Life
import com.team1091.fighterai.actor.pilot.MissileGuidance
import com.team1091.fighterai.math.beingAimedAtBy
import com.team1091.fighterai.types.MissileType

// Fires homing ordinance
class MissileRack(val missileType: MissileType, maxAmmoCount: Int) : Launcher(
        missileType.refire,
        missileType.launchVelocity,
        maxAmmoCount,
        listOf(
                Vector3(-1f, 1f, -1f),
                Vector3(1f, 1f, -1f)
        )
) {
    override fun project(world: World, shooter: Actor, position: Vector3, rotation: Quaternion, velocity: Float) {

        // find a target
        val target: Actor? = world.actors.asSequence()
                .filter { it.life != null && it != shooter }
                .filter { it.faction.isEnemy(shooter.faction) }
                .filter { it beingAimedAtBy shooter }
                .minByOrNull { shooter.position.dst2(it.position) }

        if (target == null) {
            // fail to fire - should only do this sound to players
            world.audio.beepFail(shooter.position)
            return
        }

        Gdx.app.log(shooter.callsign, "Fired a missile")
        world.audio.launch(shooter.position)

        world.newActors.add(Actor(
                callsign = "${shooter.callsign}'s ${missileType.name}",
                position = position,
                rotation = rotation,
                velocity = velocity,
                model = missileType.model,
                pilot = MissileGuidance(target),
                life = Life(1f),
                secondaryWeapon = Explosive(missileType.damage, missileType.explosionRadius),
                expiration = Expiration(world.timePassed + missileType.expiration),
                faction = shooter.faction,
                radius = missileType.radius,
                collider = DamageCollider(1f),
                engine = missileType.engine
        ))
    }

    override fun getVelocity(): Float = launchVelocity
    override fun getDuration(): Float = missileType.expiration
}