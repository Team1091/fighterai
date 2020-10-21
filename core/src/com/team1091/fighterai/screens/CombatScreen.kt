package com.team1091.fighterai.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.CameraSystem
import com.team1091.fighterai.FighterAIGame
import com.team1091.fighterai.Mission
import com.team1091.fighterai.World
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageCollider
import com.team1091.fighterai.actor.Faction
import com.team1091.fighterai.actor.Life
import com.team1091.fighterai.actor.weapon.Cannon
import com.team1091.fighterai.actor.weapon.MissileRack
import com.team1091.fighterai.types.BulletType
import com.team1091.fighterai.types.MissileType
import com.team1091.fighterai.types.up
import java.lang.Float.min

class CombatScreen(
        val fighterAIGame: FighterAIGame,
        val mission: Mission
) : Screen {


    val modelBatch = ModelBatch()
    val shapeRenderer = ShapeRenderer()
    val spriteBatch = fighterAIGame.spriteBatch
    val font = fighterAIGame.font

    val world = World(fighterAIGame.audio)
    val cameraMan = CameraSystem(world)

    val environment: Environment = mission.place.environment
    val players = mutableListOf<Actor>()   // these are ones we should watch

    init {
        // Setup of scenario
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
                                secondaryWeapon = MissileRack(MissileType.AMRAAM, 4),
                                faction = flightGroup.faction,
                                radius = 1f,
                                collider = DamageCollider(4f),
                                respawns = 1,
                                engine = aircraftType.engine,
                                friction = 0.6f
                        )
                )
                // Commenting this line in will give you a camera behind the first actor
                // cameraMan.currentTarget = world.actors.first()
            }

        }

        // Add buildings
        mission.structures.forEach { structure ->
            world.actors.add(
                    Actor(
                            callsign = "building",
                            position = Vector3(structure.position.x, structure.position.y, 3f),
                            rotation = Quaternion(),
                            velocity = 0f,
                            model = modelBuilder.createBox(5f, 5f, 5f,
                                    Material(ColorAttribute.createDiffuse(Color.ORANGE)),
                                    com.team1091.fighterai.types.attr),
                            life = Life(10f),
                            faction = structure.faction,
                            radius = 3f,
                            collider = DamageCollider(10f),
                            engine = null
                    )
            )

        }
        players.addAll(world.actors)

// No humans
//        val controllers = Controllers.getControllers().take(4)
//        mission.place.ships(world, controllers)
//        println("Controllers $controllers")


    }


    override fun render(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        val dt = Gdx.graphics.deltaTime
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        // This makes sure that the simulation is at least called 10 times a second on slower hardware
        // If the hardware is too slow, the timestamp grows and bullets may phase through the opponent
        world.simulate(min(dt, 0.1f))


        // Render
        val width: Int = Gdx.graphics.width
        val height: Int = Gdx.graphics.height
        Gdx.gl.glViewport(0, 0, width, height)
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1f)

        // Camera Stuff
        cameraMan.look(delta)

        // Render Models
        modelBatch.begin(cameraMan.cam)
        for (model in world.otherGeometry) {
            modelBatch.render(model, environment)
        }
        for (craft in world.actors) {
            modelBatch.render(craft.instance, environment)
        }
        modelBatch.end()

        // Draw HUD
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)

        var red = 1
        var blue = 1
        players.forEach { player ->

            val conj = player.rotation.cpy().conjugate()
            val (xOffset, yOffset) = if (player.faction == Faction.RED) {
                Pair(0f, red++ * 300f)
            } else {
                Pair(Gdx.graphics.width - 400f, blue++ * 300f)
            }

            shapeRenderer.color = Color.ORANGE
            shapeRenderer.rect(xOffset, yOffset, 200f, 200f)
            shapeRenderer.rect(xOffset + 200f, yOffset, 200f, 200f)

//                shapeRenderer.rect
            for (craft in world.actors) {
                if (craft.engine != null && craft != player) {
                    val pointerToCraft = craft.position.cpy().sub(player.position).mul(conj)
                    val dist = pointerToCraft.dst(0f, 0f, 0f)
                    val x = pointerToCraft.x / dist
                    val y = pointerToCraft.z / dist
                    shapeRenderer.color = if (craft.faction.isEnemy(player.faction)) Color.RED else Color.GREEN
                    if (pointerToCraft.y > 0) { // forward arc
                        shapeRenderer.rect(xOffset + 100f + x * 100f, 100f + y * 100f + yOffset, 1f, 1f)
                    } else { // rear arc
                        shapeRenderer.rect(xOffset + 300f - x * 100f, 100f - y * 100f + yOffset, 1f, 1f)
                    }

                }
            }

        }
        shapeRenderer.end()

        red = 1
        blue = 1
        spriteBatch.begin()
        players.forEach { player ->
            val (xOffset, yOffset) = if (player.faction == Faction.RED) {
                Pair(0f, red++ * 300f)
            } else {
                Pair(Gdx.graphics.width - 400f, blue++ * 300f)
            }

            font.draw(spriteBatch, player.callsign, xOffset, yOffset - 16)
            font.draw(spriteBatch, "Vel: ${player.velocity.toInt()}  Ele: ${player.position.z.toInt()}", xOffset, yOffset - 32)
            font.draw(spriteBatch, "HP: ${player.life?.cur?:0} Gun: ${player.primaryWeapon?.getAmmo() ?: 0}  MSL: ${player.secondaryWeapon?.getAmmo() ?: 0}", xOffset, yOffset - 48)
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


}