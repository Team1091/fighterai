package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageAndDisappearCollider
import com.team1091.fighterai.actor.Expiration
import com.team1091.fighterai.types.BulletType

// Fires projectiles
class Cannon(val bulletType: BulletType, maxAmmoCount: Int) : Launcher(bulletType.refireMS, bulletType.launchVelocity, maxAmmoCount) {

    override fun project(world: World, shooter: Actor, position: Vector3, rotation: Quaternion, velocity: Float) {
        // I think we will need a sound player class, that detects the distance to each and scales the audio
        world.audio.laser(position)
        world.newActors.add(Actor(
                callsign = "${shooter.callsign}'s bullet",
                position = position,
                rotation = rotation,
                velocity = velocity,
                model = bulletType.model,
                expiration = Expiration(bulletType.expiration),
                faction = shooter.faction,
                radius = bulletType.radius,
                collider = DamageAndDisappearCollider(bulletType.damage),
                engine = null,
                friction = 0.2f
        ))
    }

    override fun getVelocity(): Float = launchVelocity
    override fun getDuration(): Float = bulletType.expiration / 1000f
}