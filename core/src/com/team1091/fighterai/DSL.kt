package com.team1091.fighterai

// This is a prototype for generating scripted missions

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector2
import com.team1091.fighterai.actor.Faction
import com.team1091.fighterai.actor.pilot.KeyboardPilot
import com.team1091.fighterai.actor.pilot.Pilot
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
        props = {}
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

val campaign = Campaign(
    arrayListOf(

        Mission(
            "Shoot some boxes",
            "Attack and destroy boxes on the ground.  It's important to line up shots, and not crash.",
            Place.DESERT,
            listOf(
                FlightGroup(
                    pilot = buildPlayerShip,
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
            "Intercept",
            "Intercept a moving plane",
            Place.DESERT,
            listOf(
                FlightGroup(
                    pilot = buildPlayerShip,
                    faction = Faction.RED,
                    aircraftType = AircraftType.RAPTOR,
                    qty = 1,
                    placement = PlayerStart.EAST
                ),
                FlightGroup(
                    pilot = buildAdversarialShip , // {KeyboardPilot()},//
                    faction = Faction.BLUE,
                    aircraftType = AircraftType.X56,
                    qty = 1,
                    placement = PlayerStart.SOUTH
                )
            )

        ),

        Mission(
            "One v One",
            "Dogfight, don't hit the ground, and don't get shot",
            Place.DESERT,
            listOf(
                FlightGroup(
                    pilot = buildPlayerShip,
                    faction = Faction.RED,
                    aircraftType = AircraftType.RAPTOR,
                    qty = 1,
                    placement = PlayerStart.EAST
                ),
                FlightGroup(
                    pilot = buildAdversarialShip,
                    faction = Faction.BLUE,
                    aircraftType = AircraftType.X56,
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
                    pilot = buildPlayerShip,
                    faction = Faction.RED,
                    aircraftType = AircraftType.RAPTOR,
                    qty = 3,
                    placement = PlayerStart.EAST
                ),
                FlightGroup(
                    pilot = buildAdversarialShip,
                    faction = Faction.BLUE,
                    aircraftType = AircraftType.X56,
                    qty = 3,
                    placement = PlayerStart.WEST
                )
            )
        )
    )
)