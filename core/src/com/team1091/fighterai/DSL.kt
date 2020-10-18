package com.team1091.fighterai

// This is a prototype for generating scripted missions

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector2
import com.team1091.fighterai.actor.Faction
import com.team1091.fighterai.actor.pilot.AiPilot
import com.team1091.fighterai.actor.pilot.Pilot
import com.team1091.fighterai.actor.pilot.T1000AiPilot
import com.team1091.fighterai.types.AircraftType

enum class Place(
        environmentSetup: () -> Environment,
        val groundTexture: String?,
        val props: (world: World) -> Unit,
        val environment: Environment = environmentSetup()
) {

    DESERT(
            environmentSetup = {
                val environment = Environment()
                environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f))
                environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
                environment
            },
            groundTexture = "Tirari_Desert_-_NASA_-_satellite_2006_square.jpg",
            props = {

                // This is for additional things, like buildings


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

            }
    ),
    OCEAN(
            {
                val environment = Environment()
                environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f))
                environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
                environment
            },
            "blue.jpg",
            {}

    )

}

class Campaign(val missions: List<Mission>)

class Mission(
        val name: String,
        val description: String,
        val place: Place,
        val flightGroups: List<FlightGroup>,
        val structures: List<Structure> = listOf()
)

class FlightGroup(
        val faction: Faction,
        val aircraftType: AircraftType,
        val qty: Int = 1,
        val placement: PlayerStart = PlayerStart.values().random(),
        val pilot: () -> Pilot
)

class Structure(
        val faction: Faction,
        val position: Vector2
)

val campaign = Campaign(arrayListOf(

        Mission(
                "Shoot some boxes",
                "Attack and destroy boxes on the ground.  It's important to line up shots, and not crash.",
                Place.DESERT,
                listOf(
                        FlightGroup(
                                pilot = { AiPilot() },
                                faction = Faction.RED,
                                aircraftType = AircraftType.RAPTOR,
                                qty = 1,
                                placement = PlayerStart.EAST
                        )
                ),
                listOf(
                        Structure(Faction.BLUE, Vector2(10f, 10f)),
                        Structure(Faction.BLUE, Vector2(10f, -10f)),
                        Structure(Faction.BLUE, Vector2(-10f, -10f)),
                        Structure(Faction.BLUE, Vector2(-10f, 10f)),
                )

        ),
        Mission(
                "One v One",
                "Dogfight, don't hit the ground, and don't get shot",
                Place.DESERT,
                listOf(
                        FlightGroup(
                                pilot = { T1000AiPilot() },
                                faction = Faction.RED,
                                aircraftType = AircraftType.RAPTOR,
                                qty = 1,
                                placement = PlayerStart.EAST
                        ),
                        FlightGroup(
                                pilot = { AiPilot() },
                                faction = Faction.BLUE,
                                aircraftType = AircraftType.RAPTOR,
                                qty = 1,
                                placement = PlayerStart.WEST
                        )
                )

        ),
        Mission(
                "3v3",
                "Ocean showdown, you will need to make sure you don't run into your allies.",
                Place.OCEAN,
                listOf(
                        FlightGroup(
                                pilot = { T1000AiPilot() },
                                faction = Faction.RED,
                                aircraftType = AircraftType.RAPTOR,
                                qty = 3,
                                placement = PlayerStart.EAST
                        ),
                        FlightGroup(
                                pilot = { AiPilot() },
                                faction = Faction.BLUE,
                                aircraftType = AircraftType.RAPTOR,
                                qty = 3,
                                placement = PlayerStart.WEST
                        )
                )

        )
))