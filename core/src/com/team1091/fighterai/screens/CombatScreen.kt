package com.team1091.fighterai.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.Mission
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageCollider
import com.team1091.fighterai.actor.Life
import com.team1091.fighterai.actor.weapon.Cannon
import com.team1091.fighterai.actor.weapon.MissileRack
import com.team1091.fighterai.size
import com.team1091.fighterai.types.BulletType
import com.team1091.fighterai.types.MissileType
import com.team1091.fighterai.types.up

class CombatScreen(
        val fighterAIGame: FighterAIGame,
        val mission: Mission
) : Screen {

    val cam: PerspectiveCamera
    val modelBatch = ModelBatch()
    val shapeRenderer = ShapeRenderer()
    val spriteBatch = fighterAIGame.spriteBatch
    val font = fighterAIGame.font

    val world = World(fighterAIGame.audio)
    val environment: Environment
    val players = mutableListOf<Actor>()   // these are ones we should watch

    init {
        // Setup of scenario
        environment = mission.place.environment
        mission.place.props(world)

        // setup ground
        val size = 10000f

        val imgTexture = Texture(Gdx.files.internal(mission.place.groundTexture))
        imgTexture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat)

        val imgTextureRegion = com.badlogic.gdx.graphics.g2d.TextureRegion(imgTexture)
        imgTextureRegion.setRegion(0, 0, imgTexture.width * 10, imgTexture.height * 10)

        val modelBuilder = ModelBuilder()

        val attr = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()
        modelBuilder.begin()
        modelBuilder.part("front", GL20.GL_TRIANGLES, attr, Material(TextureAttribute.createDiffuse(imgTextureRegion)))
                .rect(size, size, 0f,
                        -size, size, 0f,
                        -size, -size, 0f,
                        size, -size, 0f,
                        0f, 0f, 1f)

        val groundModel = modelBuilder.end()

        val ground = ModelInstance(groundModel)
        world.otherGeometry.add(ground)


        // Set up the mission units
        mission.flightGroups.forEach { flightGroup ->

            val aircraftType = flightGroup.aircraftType

            repeat(flightGroup.qty) { offset ->
                val pilot = flightGroup.pilot()
                world.actors.add(

                        Actor(
                                callsign = flightGroup.faction.name + " " + pilot.javaClass.simpleName,
                                position = flightGroup.placement.pos.cpy().add(up.cpy().scl(offset.toFloat() * 10)),
                                rotation = flightGroup.placement.rotation.cpy(),
                                velocity = 300f,
                                model = aircraftType.model,
                                pilot = pilot,
                                life = Life(aircraftType.life),
                                primaryWeapon = Cannon(BulletType.M61_VULCAN, 1000),
                                secondaryWeapon = MissileRack(MissileType.AMRAAM, 2),
                                faction = flightGroup.faction,
                                collider = DamageCollider(4f),
                                respawnable = true,
                                engine = aircraftType.engine
                        )
                )
            }

        }
        players.addAll(world.actors)

// No humans
//        val controllers = Controllers.getControllers().take(4)
//        mission.place.ships(world, controllers)
//        println("Controllers $controllers")

        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.near = 1f
        cam.far = 10000f

    }


    override fun render(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        val dt = Gdx.graphics.deltaTime
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)


        world.simulate(dt)


        // Render
        val width: Int = Gdx.graphics.width
        val height: Int = Gdx.graphics.height
        Gdx.gl.glViewport(0, 0, width, height)
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1f)

        // Camera Stuff
        look(null)

        // Render Models
        modelBatch.begin(cam)
        for (model in world.otherGeometry) {
            modelBatch.render(model, environment)
        }
        for (craft in world.actors) {
            modelBatch.render(craft.instance, environment)
        }
        modelBatch.end()

        // Draw HUD
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        players.forEachIndexed { index, player ->

            val conj = player.rotation.cpy().conjugate()
            val yRow = index * 300f

            shapeRenderer.setColor(Color.ORANGE)
            shapeRenderer.rect(0f, yRow, 200f, 200f)
            shapeRenderer.rect(200f, yRow, 200f, 200f)

//                shapeRenderer.rect
            for (craft in world.actors) {
                if (craft.engine != null && craft != player) {
                    val pointerToCraft = craft.position.cpy().sub(player.position).mul(conj)
                    val dist = pointerToCraft.dst(0f, 0f, 0f)
                    val x = pointerToCraft.x / dist
                    val y = pointerToCraft.z / dist
                    shapeRenderer.setColor(if (craft.faction.isEnemy(player.faction)) Color.RED else Color.GREEN)
                    if (pointerToCraft.y > 0) { // forward arc
                        shapeRenderer.rect(100f + x * 100f, 100f + y * 100f + yRow, 1f, 1f)
                    } else { // rear arc
                        shapeRenderer.rect(300f - x * 100f, 100f - y * 100f + yRow, 1f, 1f)
                    }

                }
            }

        }
        shapeRenderer.end()

        spriteBatch.begin()
        players.forEachIndexed { index, player ->
            val yRow = (index * 300f) + 300f
            font.draw(spriteBatch, player.callsign, 0f, yRow-16)
            font.draw(spriteBatch, "Vel: ${player.velocity.toInt()}  Ele: ${player.position.z.toInt()}", 0f, yRow - 32)
            font.draw(spriteBatch, "Gun: ${player.primaryWeapon?.getAmmo()?:0}  MSL: ${player.secondaryWeapon?.getAmmo()?:0}", 0f, yRow - 48)
        }
        spriteBatch.end()

    }

    override fun show() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        modelBatch.dispose()
        shapeRenderer.dispose()
    }


    private fun look(actor: Actor?) {

        if (actor != null) {
            cam.position.set(actor.position.cpy().add(Vector3(0f, -5f, 1f).mul(actor.rotation)))
            cam.lookAt(actor.position.cpy().add(Vector3(0f, 1000f, 0f).mul(actor.rotation)))
            cam.up.set(up.cpy().mul(actor.rotation))

            // TODO: comment this is for an overview
        } else if (true) {
            val anActor = world.actors.firstOrNull { it.life != null && it.life.cur > 0 }

            cam.up.set(0f, 0f, 1f)
            cam.position.set(200f, 200f, 150f)
            if (anActor != null) {
                cam.lookAt(anActor.position)
            } else
                cam.lookAt(0f, 0f, 0f)


        } else {

            val randomActor = world.actors.find { it.engine != null }

            if (randomActor != null) {
                cam.position.set(randomActor.position.cpy().add(Vector3(0f, -5f, 1f).mul(randomActor.rotation)))

                cam.lookAt(randomActor.position.cpy().add(Vector3(0f, 1000f, 0f).mul(randomActor.rotation)))
//                var target = (actor.pilot as AiPilot).lastTarget?.position
//                if (target == null) {
//                    target = actor.position.cpy().add(Vector3(0f, 1000f, 0f).mul(actor.rotation))
//                }
//                cam.lookAt(target)

                cam.up.set(up.cpy().mul(randomActor.rotation))

            } else {
                cam.position.set(size / 2f, size / 2f, size / 2f)
                cam.lookAt(0f, 0f, 0f)
                cam.up.set(0f, 0f, 1f)
            }


        }
        cam.update()
    }

}