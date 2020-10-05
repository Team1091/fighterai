package com.team1091.fighterai.types

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.team1091.fighterai.actor.Engine

enum class AircraftType(
        var model: Model,
        val life: Float = 1f,
        val radius: Float = 1f,
        val engine: Engine
) {
    RAPTOR(
            life = 30f,
            radius = 1f,
            model = loader.loadModel(Gdx.files.internal("F_22_Raptor.obj")),
            engine = Engine(
                    maxAccel = 50f,
                    maxYaw = 60f,
                    maxRoll = 80f,
                    maxPitch = 80f
            )
    ),
    X56(
            life = 30f,
            radius = 1f,
            model = loader.loadModel(Gdx.files.internal("drone-swept.obj")),
            engine = Engine(
                    maxAccel = 50f,
                    maxYaw = 60f,
                    maxRoll = 80f,
                    maxPitch = 80f
            )
    ),
    BALLOON(
            life = 30f,
            radius = 1f,
            model = modelBuilder.createCylinder(1f, 1f, 1f, 8,
                    Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                    attr),
            engine = Engine(
                    maxAccel = 0f,
                    maxYaw = 0f,
                    maxRoll = 0f,
                    maxPitch = 0f
            )
    ),
    STORAGE(
            life = 100f,
            radius = 5f,
            model = modelBuilder.createBox(10f, 10f, 10f,
                    Material(ColorAttribute.createDiffuse(Color.SALMON)),
                    attr
            ),
            engine = Engine(
                    maxAccel = 0f,
                    maxYaw = 0f,
                    maxRoll = 0f,
                    maxPitch = 0f
            )

    )

}