package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.ftc.NextFTCOpMode;

import static dev.nextftc.bindings.Bindings.button;
//import static dev.nextftc.bindings.ButtonsKt.button; // Java import for NextBindings 'button'

@TeleOp(name = "Motor A/B Test", group = "TeleOp")
public class MotorTestOpMode extends NextFTCOpMode {

    @Override
    public void onStartButtonPressed() {
        // Bind the Gamepad 1 'A' button to trigger the startCommand when pressed
        button(() -> gamepad1.a).whenBecomesTrue(MotorMovementSubsystem.INSTANCE.startCommand);

        // Bind the Gamepad 1 'B' button to trigger the stopCommand when pressed
        button(() -> gamepad1.b).whenBecomesTrue(MotorMovementSubsystem.INSTANCE.stopCommand);
    }
}