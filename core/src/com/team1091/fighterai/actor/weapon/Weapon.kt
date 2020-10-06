package com.team1091.fighterai.actor.weapon

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor


interface Weapon {
    fun fire(world: World, shooter: Actor)
    fun project(world: World, shooter: Actor, position: Vector3, rotation: Quaternion, velocity: Float)

    fun getVelocity(): Float
}
