package com.team1091.fighterai.actor.pilot;

import com.team1091.fighterai.actor.Radar;
import com.team1091.fighterai.actor.Telemetry;
import org.jetbrains.annotations.NotNull;

public class SampleJavaPilot implements Pilot {

    @Override
    public PilotControl fly(@NotNull Telemetry us, @NotNull Radar radar) {


        // Just do loops.  It makes us harder to hit
        return new PilotControl(
                1,
                0,
                0,
                1f,
                false,
                false
        );
    }
}
