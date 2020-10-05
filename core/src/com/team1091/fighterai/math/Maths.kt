package com.team1091.fighterai.math

import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.pilot.angleTo
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

fun accel(x: Float) = (0.3f * (x + 1f).pow(2f)) - 0.2f


fun angleBetween(a: Vector3, b: Vector3): Float {

    val unitA = a.cpy().nor()!!
    val unitB = b.cpy().nor()!!

    val dot = unitA.dot(unitB)
    return acos(dot)
}


fun deaden(value: Float): Float {
    return value * abs(value)
}


fun turnTowards(unRotatedTargetOffset: Vector3): Triple<Float, Float, Float> {
    var pitchp = if (unRotatedTargetOffset.z > 0f) 1f else -1f
    var yawp = if (unRotatedTargetOffset.x > 0f) 1f else -1f
    var rollp = yawp
    return Triple(pitchp, yawp, rollp)
}

// Used to find a target in the forward arc, closest first.
fun findInForwardArc(
        fighterGame: FighterAIGame,
        us: Actor,
        maxDist: Float? = null,
        maxAngle: Float? = null,
        enemyOnly: Boolean = false
): Actor? {

    return fighterGame.actors.asSequence()
            .filter { it != us && it.engine != null }
            .filter {
                maxDist == null || it.position.dst(us.position) < maxDist
            }
            .filter {
                maxAngle == null || us angleTo it < maxAngle
            }
            .filter {
                !enemyOnly || us.faction.isEnemy(it.faction)
            }
            .minByOrNull {
                it.position.dst(us.position)
            }
}