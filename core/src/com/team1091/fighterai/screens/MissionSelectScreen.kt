package com.team1091.fighterai.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.campaign

/**
 * This allows us to select a mission to run
 */
class MissionSelectScreen(val flightAIGame: FighterAIGame) : Screen {

    val spriteBatch = flightAIGame.spriteBatch
    val font = flightAIGame.font

    override fun render(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            val mission = campaign.missions.first()
            flightAIGame.screen = CombatScreen(flightAIGame, mission)
        }

        spriteBatch.begin()
        font.draw(spriteBatch, "Select a mission by pressing its number on your keyboard", 100f, Gdx.graphics.height - 20f)
        campaign.missions.forEachIndexed { index, mission ->
            //This will cause issues if there are more than 10
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_0 + index)) {
                flightAIGame.screen = CombatScreen(flightAIGame, mission)
            }

            font.draw(spriteBatch, index.toString() + " - " + mission.name + " : " + mission.description, 100f, Gdx.graphics.height - (20f * (index + 3)))

        }
        spriteBatch.end()

    }


    override fun show() {

    }

    override fun hide() {

    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun dispose() {
    }
}