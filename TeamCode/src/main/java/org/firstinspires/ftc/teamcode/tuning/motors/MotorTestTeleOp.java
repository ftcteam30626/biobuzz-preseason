package org.firstinspires.ftc.teamcode.tuning.motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * TeleOp OpMode to test individual motors using Gamepad 1 buttons.
 * A = backLeft
 * B = frontLeft
 * X = frontRight
 * Y = backRight
 */
@TeleOp(name = "Motor Test TeleOp", group = "Test")
public class MotorTestTeleOp extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    @Override
    public void runOpMode() {
        // Initialize hardware map
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Set motors to run without encoders for simple power control
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized - Press Start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Control backLeft with A
            if (gamepad1.a) {
                backLeft.setPower(1.0);
            } else {
                backLeft.setPower(0.0);
            }

            // Control frontLeft with B
            if (gamepad1.b) {
                frontLeft.setPower(1.0);
            } else {
                frontLeft.setPower(0.0);
            }

            // Control frontRight with X
            if (gamepad1.x) {
                frontRight.setPower(1.0);
            } else {
                frontRight.setPower(0.0);
            }

            // Control backRight with Y
            if (gamepad1.y) {
                backRight.setPower(1.0);
            } else {
                backRight.setPower(0.0);
            }

            // Telemetry to show motor status
            telemetry.addData("Running", "A:%b B:%b X:%b Y:%b", 
                gamepad1.a, gamepad1.b, gamepad1.x, gamepad1.y);
            telemetry.addData("Powers", "BL:%.2f FL:%.2f FR:%.2f BR:%.2f",
                backLeft.getPower(), frontLeft.getPower(), frontRight.getPower(), backRight.getPower());
            telemetry.update();
        }
    }
}
