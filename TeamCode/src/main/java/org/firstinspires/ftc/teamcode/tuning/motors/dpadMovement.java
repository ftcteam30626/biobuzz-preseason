package org.firstinspires.ftc.teamcode.tuning.motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "dpadMovement")

public class dpadMovement extends LinearOpMode{


    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        // Set motor directions once. 
        // Reverse the left side so that positive power moves all wheels forward.
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        // Use BRAKE mode to prevent the robot from drifting when power is 0.
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        while(opModeIsActive()){
            double lfP = 0, rfP = 0, lbP = 0, rbP = 0;
            double p = 0.5; // Default movement power

            if (gamepad1.dpad_up){
                // Forward
                lfP = p; rfP = p; lbP = p; rbP = p;
            }
            else if (gamepad1.dpad_down){
                // Backward
                lfP = -p; rfP = -p; lbP = -p; rbP = -p;
            }
            else if (gamepad1.dpad_right){
                // Strafe Right
                lfP = p; rfP = -p; lbP = -p; rbP = p;
            }
            else if (gamepad1.dpad_left){
                // Strafe Left
                lfP = -p; rfP = p; lbP = p; rbP = -p;
            }
            else if (gamepad1.x){
                // Spin Left
                lfP = -p; rfP = p; lbP = -p; rbP = p;
            }
            else if (gamepad1.b){
                // Spin Right
                lfP = p; rfP = -p; lbP = p; rbP = -p;
            }

            leftFront.setPower(lfP);
            rightFront.setPower(rfP);
            leftBack.setPower(lbP);
            rightBack.setPower(rbP);

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }

}