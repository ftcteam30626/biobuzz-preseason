package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

// This tells the phone that this is a "TeleOp" (manual control) program
@TeleOp(name = "BasicOpMode", group = "TeleOp")
public class BasicOpMode extends LinearOpMode {
    
    // Motor declarations
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private DcMotor lift = null;
    private DcMotor intake = null;

    private Servo basketServo = null;

    @Override
    public void runOpMode() {
        // --- INITIALIZATION ---
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        lift = hardwareMap.get(DcMotor.class, "lift");
        intake = hardwareMap.get(DcMotor.class, "intake");
        basketServo = hardwareMap.get(Servo.class,"basketServo");

        // Reverse left motors so positive power moves the robot forward
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        // Enable BRAKE mode to prevent drifting and hold the slide in place
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // --- MAIN LOOP ---
        while (opModeIsActive()) {
            basketServo.setPosition(0.5);
            intake.setDirection(DcMotorSimple.Direction.REVERSE);
            intake.setPower(1.0);
            
            // 1. MECANUM DRIVE
            // Get joystick inputs
            double y = -gamepad1.left_stick_y; // Forward/Back
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x; // Rotation

            // Normalize power so no motor exceeds 1.0, and maintain 0.8 scaling for control
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // Apply power to drive motors
            leftFront.setPower(frontLeftPower * 0.8);
            leftBack.setPower(backLeftPower * 0.8);
            rightFront.setPower(frontRightPower * 0.8);
            rightBack.setPower(backRightPower * 0.8);

            // 2. VIPER SLIDE AUTOMATED SEQUENCE (Simple Blocking Version)
            if (gamepad1.a) {
                // lift goes up for 3 second at 0.75 speed
                lift.setPower(0.75);
                sleep(2000);
                
                // wait 3 seconds
                basketServo.setPosition(1);
                lift.setPower(0);
                sleep(1000);
                basketServo.setPosition(0.5);
                
                // goes back down 3 seconds at 0.75 speed
                lift.setPower(-0.75);
                sleep(2000);
                
                // Stop the lift
                lift.setPower(0);
            }

            if (gamepad1.b) {
                intake.setPower(0);
            }


            // 4. TELEMETRY
            telemetry.addData("Status", "Running");
            telemetry.addData("Drive Power", "LF:%.2f RF:%.2f", frontLeftPower, frontRightPower);
            telemetry.update();
        }
    }
}
