package com.team1091.fighterai

// This is a prototype for generating scripted missions

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.team1091.fighterai.actor.Actor
import com.team1091.fighterai.actor.DamageCollider
import com.team1091.fighterai.actor.Faction
import com.team1091.fighterai.actor.Life
import com.team1091.fighterai.actor.pilot.AiPilot
import com.team1091.fighterai.actor.pilot.T1000AiPilot
import com.team1091.fighterai.actor.weapon.Cannon
import com.team1091.fighterai.actor.weapon.MissileRack
import com.team1091.fighterai.types.AircraftType
import com.team1091.fighterai.types.BulletType
import com.team1091.fighterai.types.MissileType


enum class Place(
        environmentSetup: () -> Environment,
        val props: (world: World) -> Unit,
        val ships: (world: World, controllers: List<Controller>) -> Unit,
        val environment: Environment = environmentSetup()) {

    DESERT(
            environmentSetup = {
                val environment = Environment()
                environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f))
                environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
                environment
            },
            props = {

                val size = 10000f


                val imgTexture = Texture(com.badlogic.gdx.Gdx.files.internal("Tirari_Desert_-_NASA_-_satellite_2006_square.jpg"))
                imgTexture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat)

                val imgTextureRegion = com.badlogic.gdx.graphics.g2d.TextureRegion(imgTexture)
                imgTextureRegion.setRegion(0, 0, imgTexture.getWidth() * 10, imgTexture.getHeight() * 10);

                var modelBuilder = com.badlogic.gdx.graphics.g3d.utils.ModelBuilder()

                val attr = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()
                modelBuilder.begin()
                modelBuilder.part("front", GL20.GL_TRIANGLES, attr, Material(TextureAttribute.createDiffuse(imgTextureRegion)))
                        .rect(size, size, 0f,
                                -size, size, 0f,
                                -size, -size, 0f,
                                size, -size, 0f,
                                0f, 0f, 1f);

                val groundModel = modelBuilder.end()

                val ground = ModelInstance(groundModel)
                it.otherGeometry.add(ground)


//                val containerModel = modelBuilder.createBox(size, size, size,
//                        Material(ColorAttribute.createDiffuse(Color.GRAY)),
//                        attr)
//
//                for (x in 0..5) {
//                    for (y in 0..5) {
//                        for (z in 0..5) {
//                            val pos = Vector3(
//                                    x.toFloat() * 40f - (3 * 40),
//                                    y.toFloat() * 40f - (3 * 40),
//                                    z.toFloat() * 40f - (3 * 40)
//                            )
//
//                            it.actors.add(Actor(
//                                    pos,
//                                    Quaternion(),
//                                    0f,
//                                    containerModel,
//                                    pilot = DriftPilot(),
//                                    radius = size,
//                                    collider = DamageCollider(100f)
//                            ))
//                        }
//                    }
//                }

            },
            ships = { world: World, controllers: List<Controller> ->
                // players
//                controllers.forEachIndexed { i, controller ->
//                    Gdx.app.log("Controller Found, Assigning ship", controller.name)
//                    val aircraftType = AircraftType.RAPTOR // if(i%2==0)  AircraftType.SWORDFISH else AircraftType.TILAPIA
//                    val playerStart = PlayerStart.values()[i]
//                    val actor = Actor(
//                            position = playerStart.pos.cpy(),
//                            rotation = playerStart.rotation.cpy(),
//                            velocity = 300f,
//                            model = aircraftType.model,
//                            pilot = HumanPilot(controller),
//                            life = Life(aircraftType.life),
//                            aircraftType = aircraftType,
//                            primaryWeapon = Cannon(BulletType.RAILGUN),
//                            secondaryWeapon = MissileRack(MissileType.AMRAAM),
//                            radius = aircraftType.radius,
//                            collider = DamageCollider(4f),
//                            respawnable = true
//                    )
//                    fighterGame.actors.add(actor)
//                    fighterGame.players.add(actor)
//                }

                listOf(
                        Triple(T1000AiPilot(), PlayerStart.LEFT, Faction.BLUE),
                        Triple(AiPilot(), PlayerStart.RIGHT, Faction.RED)
                ).forEach {

                    val (pilot, playerStart, faction) = it

                    val aircraftType = AircraftType.RAPTOR

                    world.actors.add(
                            Actor(
                                    callsign = faction.name + " " + pilot.javaClass.simpleName,
                                    faction = faction,
                                    position = playerStart.pos.cpy(),
                                    rotation = playerStart.rotation.cpy(),
                                    velocity = 300f,
                                    model = aircraftType.model,
                                    pilot = pilot,
                                    life = Life(aircraftType.life),
                                    aircraftType = aircraftType,
                                    primaryWeapon = Cannon(BulletType.M61_VULCAN),
                                    secondaryWeapon = MissileRack(MissileType.AMRAAM),
                                    collider = DamageCollider(4f),
                                    respawnable = true,
                                    engine = aircraftType.engine
                            )
                    )
                }
            }
    )

//    OCEAN(
//            {
//                val environment = Environment()
//                environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f))
//                environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
//                environment
//            },
//            {},
//            { world: World, controllers: List<Controller> ->
//
//            }
//    )

}

class Campaign(val missions: List<Mission>)

class Mission(
        val name: String,
        val description: String,
        val place: Place,
        val flightGroups: List<FlightGroup>
)

class FlightGroup(
        val faction: Faction,
        val aircraftType: AircraftType,
        val qty: Int = 1,
        val players: Boolean = false,
        val placement: Placement = Placement.RANDOM
)

// Where entities start
enum class Placement {
    GRID, // grid, centered at 0, 0
    RANDOM,
    X_POS,
    X_NEG
//    Y_POS,
//    Y_NEG,
//    Z_POS,
//    Z_NEG;
}

val campaign = Campaign(
        arrayListOf(
                Mission(
                        "One v One",
                        "",
                        Place.DESERT,
                        listOf(
                                FlightGroup(
                                        Faction.RED,
                                        aircraftType = AircraftType.BALLOON,
                                        qty = 1,
                                        placement = Placement.X_NEG
                                ),
                                FlightGroup(
                                        Faction.BLUE,
                                        aircraftType = AircraftType.STORAGE,
                                        qty = 1,
                                        placement = Placement.X_POS
                                )
                        )

                )
        )
)