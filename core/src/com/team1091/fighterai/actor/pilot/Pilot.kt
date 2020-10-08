package com.team1091.fighterai.actor.pilot

import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.Radar
import com.team1091.fighterai.actor.Telemetry
import com.team1091.fighterai.math.angleBetween
import com.team1091.fighterai.types.forward

interface Pilot {
    fun fly(us: Telemetry, radar: Radar): PilotControl
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