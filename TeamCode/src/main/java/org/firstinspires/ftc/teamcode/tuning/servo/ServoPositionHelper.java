package org.firstinspires.ftc.teamcode.tuning.servo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Servo Position Helper", group = "Tuning")
public class ServoPositionHelper extends LinearOpMode {
    // Declare OpMode member.
    private Servo servo = null;

    /*
     * Create a variable which we will modify with our code. Eventually we will instruct
     * the servo to run to the position captured by this variable.
     */
    private double servoPosition = 0.5;

    // Create a variable for size of each "step" that we will increment or decrement our servo position by.
    private double positionAdjustment = 0.05;

    // This variable captures how much we need to increment or decrement the step size by
    private final double STEP_ADJUSTMENT = 0.01;

    /*
     * This variable is the maximum position we want to send to the servo.
     * Some servos do not operate well went sent a signal too large, or too small.
     * Most Hitec Linear servos for example only respond to signals within a 1050-1950µsec range.
     * Converted to 0-1, that means we should not send a Hitec Linear Servo less than 0.25, or more than 0.75.
     */
    private final double MIN_POSITION = 0;
    private final double MAX_POSITION = 1;

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

        // Initialize the servo. Make sure the name matches your configuration.
        servo = hardwareMap.get(Servo.class, "testServo");

        servoPosition = 0.5;
        servo.setPosition(servoPosition);

        telemetry.addData("Status", "Initialized. Servo: testServo");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            boolean currentGamepadY = gamepad1.y;
            boolean currentGamepadA = gamepad1.a;
            boolean currentGamepadB = gamepad1.b;
            boolean currentGamepadUp = gamepad1.dpad_up;
            boolean currentGamepadDown = gamepad1.dpad_down;

            // Check to see if the user is clicking the Y button on the gamepad to increment target.
            if (currentGamepadY && !previousGamepadY) {
                // += is an operator that lets us add the step variable without overwriting the servoPosition variable.
                servoPosition += positionAdjustment;
            } else if (currentGamepadA && !previousGamepadA) {
                // We use an else if statement here so that we only check if A is pressed after we know
                // that the Y button is not pressed.
                servoPosition -= positionAdjustment;
            }

            // Here we modify the step size if the user clicks D-pad up or D-pad down.
            if (currentGamepadUp && !previousGamepadUp) {
                positionAdjustment += STEP_ADJUSTMENT;
            } else if (currentGamepadDown && !previousGamepadDown) {
                positionAdjustment -= STEP_ADJUSTMENT;
                // Prevent step size from becoming negative.
                if (positionAdjustment < 0) positionAdjustment = 0;
            }

            // Reset the position to center (0.5) if B is pressed.
            if (currentGamepadB && !previousGamepadB) {
                servoPosition = 0.5;
            }

            // Check to see if we're setting the servoPosition to less than the min, or more than the max.
            if (servoPosition > MAX_POSITION) {
                servoPosition = MAX_POSITION;
            } else if (servoPosition < MIN_POSITION) {
                servoPosition = MIN_POSITION;
            }

            /*
             * Finally, set the servo to the servoPosition variable. We do this only once per loop
             * so that we can be sure not to write conflicting positions to the servo.
             */
            servo.setPosition(servoPosition);

            // Because our logic has finished, we set our "previousGamepad" booleans to the current ones.
            previousGamepadY = currentGamepadY;
            previousGamepadA = currentGamepadA;
            previousGamepadB = currentGamepadB;
            previousGamepadUp = currentGamepadUp;
            previousGamepadDown = currentGamepadDown;

            // Show tuning info
            telemetry.addData("Target Position", servoPosition);
            telemetry.addData("Actual Position", servo.getPosition());
            telemetry.addData("Step Size (D-pad)", positionAdjustment);
            telemetry.addLine("\nControls:");
            telemetry.addLine("  Y / A : Increase/Decrease Target Position");
            telemetry.addLine("  D-pad Up/Down : Adjust Step Size");
            telemetry.addLine("  B : Reset Position to 0.5");
            telemetry.update();
        }
    }
}
