package org.firstinspires.ftc.teamcode.tuning.motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Motor Encoder Position Helper", group = "Tuning")
public class MotorEncoderPositionHelper extends LinearOpMode {
    // Declare OpMode member.
    private DcMotorEx motor = null;

    /*
     * Create a variable which we will modify with our code. Eventually we will instruct
     * the motor to run to the position captured by this variable.
     */
    private int targetPosition = 0;

    // Create a variable for size of each "step" that we will increment or decrement our motor position by.
    private int positionAdjustment = 10;

    // This variable captures how much we need to increment or decrement the step size by
    private final int STEP_ADJUSTMENT = 50;

    // These booleans are used in the "rising edge detection"
    private boolean previousGamepadY = false;
    private boolean previousGamepadA = false;
    private boolean previousGamepadUp = false;
    private boolean previousGamepadDown = false;
    private boolean previousGamepadB = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        // Initialize the motor. Make sure the name matches your configuration.
        motor = hardwareMap.get(DcMotorEx.class, "testMotor");

        // Reverse the motor direction so that positive targets result in positive encoder movement.
        // If the motor still moves the wrong way, you can change this back to FORWARD.
        motor.setDirection(DcMotor.Direction.REVERSE);

        // Reset the encoder and set the motor to run to position
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power for movement. Adjust this as needed for your specific mechanism.
        motor.setPower(0.8); // Increased power to help hold position against gravity

        // Ensure the motor stops and holds its position when power is zero (if not in RUN_TO_POSITION)
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized. Motor: testMotor");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        while (opModeIsActive()) {

            boolean currentGamepadY = gamepad1.y;
            boolean currentGamepadA = gamepad1.a;
            boolean currentGamepadB = gamepad1.b;
            boolean currentGamepadUp = gamepad1.dpad_up;
            boolean currentGamepadDown = gamepad1.dpad_down;

            // Check to see if the user is clicking the Y button on the gamepad to increment target.
            if (currentGamepadY && !previousGamepadY) {
                targetPosition += positionAdjustment;
            } else if (currentGamepadA && !previousGamepadA) {
                // Check if A is pressed to decrement target.
                targetPosition -= positionAdjustment;
            }

            // Modify the step size if the user clicks D-pad up or D-pad down.
            if (currentGamepadUp && !previousGamepadUp) {
                positionAdjustment += STEP_ADJUSTMENT;
            } else if (currentGamepadDown && !previousGamepadDown) {
                positionAdjustment -= STEP_ADJUSTMENT;
                // Prevent step size from becoming negative.
                if (positionAdjustment < 0) positionAdjustment = 0;
            }

            // Reset the encoder to zero at the current physical position if B is pressed.
            if (currentGamepadB && !previousGamepadB) {
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                targetPosition = 0;
                motor.setTargetPosition(0);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

            /*
             * Update the motor's target position.
             */
            motor.setTargetPosition(targetPosition);

            // Save previous states for rising edge detection
            previousGamepadY = currentGamepadY;
            previousGamepadA = currentGamepadA;
            previousGamepadB = currentGamepadB;
            previousGamepadUp = currentGamepadUp;
            previousGamepadDown = currentGamepadDown;

            // Show tuning info
            telemetry.addData("Target Position", targetPosition);
            telemetry.addData("Actual Position", motor.getCurrentPosition());
            telemetry.addData("Motor Power", motor.getPower());
            telemetry.addData("Motor Velocity", motor.getVelocity());
            telemetry.addData("Step Size (D-pad)", positionAdjustment);
            telemetry.addLine("\nControls:");
            telemetry.addLine("  Y / A : Increase/Decrease Target Position");
            telemetry.addLine("  D-pad Up/Down : Adjust Step Size");
            telemetry.addLine("  B : Reset Encoder and Target to 0");
            telemetry.addLine("\nNote: If motor 'falls', check if motor.setPower() is high enough.");
            telemetry.update();
        }
    }
}