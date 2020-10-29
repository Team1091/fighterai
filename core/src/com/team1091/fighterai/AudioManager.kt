package com.team1091.fighterai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import kotlin.random.Random

class AudioManager : IAudioManager {

    lateinit var laser: Sound
    lateinit var explosion: Sound

    lateinit var launch: Sound
    lateinit var beep: Sound

    private val viewLocation = Vector3()
    private val viewOrientation = Quaternion()

    private fun pitch() = (Random.nextFloat() * 0.2f) + 0.9f

    fun init() {
        laser = Gdx.audio.newSound(Gdx.files.internal("audio/laser2.mp3"))
        explosion = Gdx.audio.newSound(Gdx.files.internal("audio/explosions/explosion08.wav"))
        launch = Gdx.audio.newSound(Gdx.files.internal("audio/lowDown.mp3"))
        beep = Gdx.audio.newSound(Gdx.files.internal("audio/lowRandom.mp3"))
    }


    override fun setLocation(location: Vector3, orientation: Quaternion) {
        viewLocation.set(location)
        viewOrientation.set(orientation)
    }

    fun play(sound: Sound, location: Vector3) {

        // volume is based on distance.  1 = close,  0 = 1000, beyond = 0
        val dist = viewLocation.dst(location)
        val volume = clamp(1f - (dist / 1000), 0f, 1f)
        if (volume <= 0.001f)
            return

        val fromPerspective = location.cpy().sub(viewLocation).mul(viewOrientation.conjugate()).nor()

//        Gdx.app.log("T", fromPerspective.x.toString())
        sound.play(volume, pitch(), fromPerspective.x)
    }

    override fun explode(location: Vector3) {
        play(explosion, location)
    }

    override fun laser(location: Vector3) {
        play(laser, location)
    }

    override fun launch(location: Vector3) {
        play(launch, location)
    }

    override fun beepFail(location: Vector3) {
        play(beep, location)
    }

    fun dispose() {
        laser.dispose()
        explosion.dispose()
        launch.dispose()
        beep.dispose()
    }
}
