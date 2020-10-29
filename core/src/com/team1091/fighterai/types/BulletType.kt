package com.team1091.fighterai.types

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute

enum class BulletType(
        val launchVelocity: Float = 20f,
        val refireMS: Long = 1000,
        var model: Model,
        val expiration: Long = 3000,//ms
        val radius: Float = 0.3f,
        val damage: Float = 2f
        // sound?
        // damage?
) {
    RAILGUN(
            launchVelocity = 300f,
            refireMS = 2000,
            model = modelBuilder.createCone(0.25f, 0.25f, 0.25f, 3,
                    Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                    attr)),
    M61_VULCAN(
            launchVelocity = 200f,
            refireMS = 150,
            model = modelBuilder.createCone(0.25f, 0.25f, 0.25f, 3,
                    Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                    attr)) // bot weapon, objectively worse
}