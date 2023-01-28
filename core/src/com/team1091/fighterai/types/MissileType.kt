package com.team1091.fighterai.types

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.team1091.fighterai.actor.Engine

enum class MissileType(
    val refire: Float,
    val damage: Float,
    val explosionRadius: Float,
    val radius: Float = 0.3f,
    var model: Model,
    val launchVelocity: Float = 40f,
    val expiration: Float,
    val engine: Engine
) {
    //TODO: add guidance system

    AMRAAM( // guided
        refire = 3f,
        damage = 20f,
        explosionRadius = 5f,
        expiration = 10f,
        model = modelBuilder.createCone(
            0.5f, 1f, 0.5f, 3,
            Material(ColorAttribute.createDiffuse(Color.BLACK)),
            attr
        ),
        engine = Engine(
            maxAccel = 70f,
            maxYaw = 65f,
            maxRoll = 65f,
            maxPitch = 65f
        )

    ),
    HYDRA( // unguided rocket
        refire = 1f,
        damage = 10f,
        explosionRadius = 5f,
        expiration = 8f,
        model = modelBuilder.createCone(
            0.5f, 1f, 0.5f, 3,
            Material(ColorAttribute.createDiffuse(Color.FIREBRICK)),
            attr
        ),
        engine = Engine(
            maxAccel = 80f,
            maxYaw = 0f,
            maxRoll = 0f,
            maxPitch = 0f
        )

    );

}