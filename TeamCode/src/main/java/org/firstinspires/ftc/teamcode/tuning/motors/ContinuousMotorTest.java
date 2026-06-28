package org.firstinspires.ftc.teamcode.tuning.motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * TeleOp OpMode where all motors move together using the left joystick.
 * Left Stick Y = All motors forward/backward
 * Left Bumper = Safety STOP
 */
@TeleOp(name = "Continuous Motor Test", group = "Test")
public class ContinuousMotorTest extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    @Override
    public void runOpMode() {
        // Initialize motors from hardware map
        backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        backRight = hardwareMap.get(DcMotor.class, "rightBack");

        // Set to run without encoders for maximum power testing
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Left Stick Y: Move all motors");
        telemetry.addLine("LEFT BUMPER to STOP ALL");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Get joystick input (negative because up is negative on the stick)
            double power = -gamepad1.left_stick_y;

            // Apply same power to all motors for simple forward/backward test
            backLeft.setPower(power);
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backRight.setPower(power);

            // Safety override: If Left Bumper is held, force all motors to stop
            if (gamepad1.left_bumper) {
                backLeft.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backRight.setPower(0);
            }

            // Report current power states
            telemetry.addData("BL Power", "%.2f", backLeft.getPower());
            telemetry.addData("FL Power", "%.2f", frontLeft.getPower());
            telemetry.addData("FR Power", "%.2f", frontRight.getPower());
            telemetry.addData("BR Power", "%.2f", backRight.getPower());
            telemetry.update();
        }
    }
}
