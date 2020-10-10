package com.team1091.fighterai.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.team1091.fighterai.FighterAIGame

class BattleSummaryScreen(val fighterAIGame: FighterAIGame) : Screen {

    override fun render(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        fighterAIGame.screen = MissionSelectScreen(fighterAIGame);
    }

    override fun resize(width: Int, height: Int) {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}