package com.team1091.fighterai.types

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute

enum class BulletType(
    val launchVelocity: Float = 20f,
    val refire: Float = 1f,
    var model: Model,
    val expiration: Float = 3f, //seconds
    val radius: Float = 0.3f,
    val damage: Float = 2f
    // sound?
    // damage?
) {
    RAILGUN(
        launchVelocity = 300f,
        refire = 2f,
        model = modelBuilder.createCone(
            0.25f, 0.25f, 0.25f, 3,
            Material(ColorAttribute.createDiffuse(Color.ORANGE)),
            attr
        )
    ),
    M61_VULCAN(
        launchVelocity = 200f,
        refire = 0.15f,
        model = modelBuilder.createCone(
            0.25f, 0.25f, 0.25f, 3,
            Material(ColorAttribute.createDiffuse(Color.YELLOW)),
            attr
        )
    ) // bot weapon, objectively worse
}