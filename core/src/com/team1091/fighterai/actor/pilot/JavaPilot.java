package com.team1091.fighterai.actor.pilot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.team1091.fighterai.actor.Radar;
import com.team1091.fighterai.actor.Telemetry;
import com.team1091.fighterai.math.StickPosition;
import com.team1091.fighterai.types.TypesKt;

import static com.team1091.fighterai.math.MathsKt.turnTowards;

public class JavaPilot implements Pilot {

    @Override
    public PilotControl fly(Telemetry us, Radar radar) {

        if (us.getPosition().z < 30) {
            // figure out how we are rotated, and which way up is.
            Vector3 localUp = TypesKt.getUp().cpy().mul(us.getRotation().cpy().conjugate());

            StickPosition maneuver = turnTowards(localUp);

            Gdx.app.log(us.getCallsign(), "Emergency pull up");
            return new PilotControl(
                    maneuver.getPitch(),
                    maneuver.getYaw(),
                    maneuver.getRoll(),
                    1f,
                    false,
                    false
            );
        }

        // Just do corkscrews, yay
        return new PilotControl(
                1,
                1,
                1,
                1f,
                false,
                false
        );
    }
}
