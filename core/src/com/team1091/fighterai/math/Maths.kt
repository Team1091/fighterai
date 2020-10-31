package com.team1091.fighterai.math

import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor

import com.team1091.fighterai.types.forward
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

// https://gamedev.stackexchange.com/questions/35859/algorithm-to-shoot-at-a-target-in-a-3d-game
fun leadTarget(src: Vector3, targetPos: Vector3, targetVel: Vector3, projectileVel: Float): FiringSolution {

    // vector to the target
    val toTarget = targetPos.cpy().sub(src)

    val a = targetVel.dot(targetVel) - projectileVel * projectileVel
    val b = 2 * targetVel.dot(toTarget)
    val c = toTarget.dot(toTarget)

    val p = -b / (2 * a)
    val q = sqrt(b * b - 4f * a * c) / (2 * a)

    val t1 = p - q
    val t2 = p + q
    val t: Float

    if (t1 > t2 && t2 > 0) {
        t = t2
    } else {
        t = t1
    }

    val aimSpot = targetPos.cpy().mulAdd(targetVel, t)
    val bulletPath = aimSpot.cpy().sub(src)
    val timeToImpact = bulletPath.len() / projectileVel //speed must be in units per second

    return FiringSolution(aimSpot, bulletPath, timeToImpact)
}

// Gets the angle between two vectors, in radians
fun angleBetween(a: Vector3, b: Vector3): Float {

    val unitA = a.cpy().nor()
    val unitB = b.cpy().nor()

    val dot = unitA.dot(unitB)
    return acos(dot)
}

// Given a local vector, this turns to that vector
fun turnTowards(relativePosition: Vector3): StickPosition =
        StickPosition(
                pitch = if (relativePosition.z > 0f) 1f else -1f,
                yaw = if (relativePosition.x > 0f) 1f else -1f,
                roll = if (relativePosition.x > 0f) 1f else -1f
        )


data class StickPosition(
        var pitch: Float,
        var yaw: Float,
        var roll: Float
)

fun <T : Comparable<T>> limit(o: T, min: T, max: T): T {
    return if (o < min) min
    else if (o > max) max
    else o
}

// Used to find targets in the forward arc.
fun findInForwardArc(
        world: World,
        us: Actor,
        maxDist: Float? = null,
        maxAngle: Float? = null
): List<Actor> {
    return world.actors.asSequence()
            .filter { it != us }
            .filter {
                maxDist == null || it.position.dst(us.position) < maxDist
            }
            .filter {
                maxAngle == null || us angleTo it < maxAngle
            }.toList()
}


infix fun Actor.beingAimedAtBy(target: Actor): Boolean {
    val angle = this angleTo target
    return angle < 0.174533f // 10 degrees
}

infix fun Actor.angleTo(target: Actor): Float {
    val difference = position.cpy().sub(target.position)
    val aim = forward.cpy().mul(target.rotation)
    return angleBetween(difference, aim)
}