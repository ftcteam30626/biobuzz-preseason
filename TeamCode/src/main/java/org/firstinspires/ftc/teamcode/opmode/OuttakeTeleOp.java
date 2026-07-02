package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.ftc.NextFTCOpMode;
import org.firstinspires.ftc.teamcode.subsystems.OuttakeSubsystem;

import static dev.nextftc.bindings.Bindings.button;

@TeleOp(name = "Outtake TeleOp", group = "TeleOp")
public class OuttakeTeleOp extends NextFTCOpMode {

    @Override
    public void onStartButtonPressed() {
        OuttakeSubsystem outtake = OuttakeSubsystem.INSTANCE;

        // Button A: Lift up to lower basket position
        button(() -> gamepad1.a).whenBecomesTrue(outtake.extendToLowerBasket);

        // Button B: Lift down to base position (0)
        button(() -> gamepad1.b).whenBecomesTrue(outtake.retractToBase);

        // Button X: Open basket gate
        button(() -> gamepad1.x).whenBecomesTrue(outtake.openGate);

        // Button Y: Close basket gate
        button(() -> gamepad1.y).whenBecomesTrue(outtake.closeGate);
    }
}
