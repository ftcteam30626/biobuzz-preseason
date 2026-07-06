package org.firstinspires.ftc.teamcode.opmode;

import org.firstinspires.ftc.teamcode.tuning.pedropathing.Constants;

import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.DriverControlledCommand;

public class BioBuzzPreSeasonOpMode extends NextFTCOpMode {

    {
        addComponents(
            /* existing components */
            new PedroComponent(Constants::createFollower));
    }

    @Override
    public void onStartButtonPressed(){
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );
        driverControlled.schedule();
    }

}
