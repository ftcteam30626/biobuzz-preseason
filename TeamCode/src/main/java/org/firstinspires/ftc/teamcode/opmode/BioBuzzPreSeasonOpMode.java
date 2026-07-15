package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.tuning.pedropathing.Constants;

import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "BioBuzzPreSeasonOpMode")
public class BioBuzzPreSeasonOpMode extends NextFTCOpMode {

    {
        addComponents(
                new PedroComponent(Constants::createFollower)
        );
    }

    @Override
    public void onStartButtonPressed() {
        new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        ).schedule();
    }
}