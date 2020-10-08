package com.team1091.fighterai.types

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.team1091.fighterai.actor.Engine

enum class MissileType(
        val refireMS: Long,
        val damage: Float,
        val explosionRadius: Float,
        val radius: Float = 0.3f,
        var model: Model,
        val launchVelocity: Float = 40f,
        val expiration: Long,
        val engine: Engine
) {
    //TODO: add guidance system

    AMRAAM( // guided
            refireMS = 3000,
            damage = 20f,
            explosionRadius = 5f,
            expiration = 10000,
            model = modelBuilder.createCone(0.5f, 1f, 0.5f, 3,
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
            refireMS = 1000,
            damage = 10f,
            explosionRadius = 5f,
            expiration = 8000,
            model = modelBuilder.createCone(0.5f, 1f, 0.5f, 3,
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
//    , // bot weapon
//    ANACONDA(
//            refireMS = 20000,
//            damage = 100f,
//            explosionRadius = 20f,
//            radius = 1f,
//            acceleration = 15f,
//            turn = 15f,
//            expiration = 30000,
//            model = modelBuilder.createCone(0.25f, 0.25f, 0.25f, 3,
//                    Material(ColorAttribute.createDiffuse(Color.CYAN)),
//                    attr
//            )
//    )

}