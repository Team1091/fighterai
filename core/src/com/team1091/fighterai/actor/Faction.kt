package com.team1091.fighterai.actor

enum class Faction {
    UNALIGNED,
    RED,
    BLUE;

    fun isEnemy(faction: Faction): Boolean {
        if (faction == UNALIGNED || this == UNALIGNED) {
            return false
        }
        return this != faction
    }
}