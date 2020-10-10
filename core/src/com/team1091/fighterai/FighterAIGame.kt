package com.team1091.fighterai

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.team1091.fighterai.screens.MissionSelectScreen
import com.team1091.fighterai.types.AircraftType
import com.team1091.fighterai.types.BulletType
import com.team1091.fighterai.types.MissileType


class FighterAIGame : Game() {

    val audio: AudioManager = AudioManager()
    val world = World(audio)
    lateinit var spriteBatch: SpriteBatch
    lateinit var font: BitmapFont

    override fun create() {
        audio.init()
        spriteBatch = SpriteBatch()
        font = BitmapFont()
        setScreen(MissionSelectScreen(this))
    }

    override fun dispose() {
        audio.dispose()
        font.dispose()
        spriteBatch.dispose()
        AircraftType.values().forEach { it.model.dispose() }
        BulletType.values().forEach { it.model.dispose() }
        MissileType.values().forEach { it.model.dispose() }
    }
}
