package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageAndDisappearCollider
import com.team1091.fighterai.actor.Expiration
import com.team1091.fighterai.types.BulletType

// Fires projectiles
class Cannon(val bulletType: BulletType) : Launcher(bulletType.refireMS, bulletType.launchVelocity) {

    override fun project(fighterGame: FighterAIGame, shooter: Actor, position: Vector3, rotation: Quaternion, velocity: Float) {

        // I think we will need a sound player class, that detects the distance to each and scales the audio
        fighterGame.audio.laser()
        fighterGame.newActors.add(Actor(
                "${shooter.callsign}'s bullet",
                position,
                rotation,
                velocity,
                bulletType.model,
                expiration = Expiration(bulletType.expiration),
                radius = bulletType.radius,
                collider = DamageAndDisappearCollider(bulletType.damage),
                engine = null
        ))
    }

    override fun getVelocity(): Float = launchVelocity

}