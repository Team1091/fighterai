package com.team1091.fighterai

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageCollider
import com.team1091.fighterai.actor.Life
import com.team1091.fighterai.actor.weapon.Cannon
import com.team1091.fighterai.actor.weapon.MissileRack
import com.team1091.fighterai.types.AircraftType
import com.team1091.fighterai.types.BulletType
import com.team1091.fighterai.types.MissileType
import com.team1091.fighterai.types.up


class FighterAIGame : ApplicationAdapter() {

    //    https://stackoverflow.com/questions/17902373/split-screen-in-libgdx
    internal lateinit var cam: PerspectiveCamera
    internal lateinit var modelBatch: ModelBatch
    internal lateinit var environment: Environment
    internal lateinit var shapeRenderer: ShapeRenderer
    internal lateinit var spriteBatch: SpriteBatch
    internal lateinit var font: BitmapFont


//    internal lateinit var envCubemap: EnvironmentCubemap

    val audio: AudioManager = AudioManager()
    val world = World(audio)

    // these are ones we should watch
    var players = mutableListOf<Actor>()

    override fun create() {
        audio.init()

        modelBatch = ModelBatch()
        shapeRenderer = ShapeRenderer()
        spriteBatch = SpriteBatch()
        font = BitmapFont()

        val mission = campaign.missions.first()


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
                                primaryWeapon = Cannon(BulletType.M61_VULCAN),
                                secondaryWeapon = MissileRack(MissileType.HYDRA),
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

    override fun render() {
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
            font.draw(spriteBatch, player.callsign, 0f, yRow)
            font.draw(spriteBatch, "Vel:"+player.velocity.toInt() +" Ele:"+player.position.z.toInt(),0f, yRow-16)
        }
        spriteBatch.end()


    }

    override fun dispose() {
        audio.dispose()
        modelBatch.dispose()
        shapeRenderer.dispose()

        AircraftType.values().forEach { it.model.dispose() }
        BulletType.values().forEach { it.model.dispose() }
        MissileType.values().forEach { it.model.dispose() }

    }
}

const val size = 1000f

enum class PlayerStart(val pos: Vector3, val rotation: Quaternion) {

    WEST(Vector3(-size, 0f, 200f), Quaternion().setEulerAngles(0f, 0f, -90f)),
    EAST(Vector3(size, 0f, 200f), Quaternion().setEulerAngles(0f, 0f, 90f)),
    SOUTH(Vector3(0f, -size, 200f), Quaternion().setEulerAngles(0f, 0f, 0f)),
    NORTH(Vector3(0f, size, 200f), Quaternion().setEulerAngles(0f, 0f, 180f)),
}