package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

/**
 * OuttakeSubsystem handles the lift (viper slide) and the basket gate servo.
 */
public class OuttakeSubsystem implements Subsystem {
    public static final OuttakeSubsystem INSTANCE = new OuttakeSubsystem();

    private final MotorEx outTakeViperSlideMotor = new MotorEx("outTakeViperSlide");
    private final ServoEx basketGateServo = new ServoEx("basketGate");

    // Control system for the viper slide. These constants should be tuned for your robot.
    private final ControlSystem viperSlideControlSystem = ControlSystem.builder()
            .posPid(0.005, 0, 0) 
            .elevatorFF(0)
            .build();

    // Target positions
    public static final double LowerBasketPosition = 100.0;
    public static final double basketGateOpenPosition = 0.3;
    public static final double basketGateClosePosition = 0.5;

    private OuttakeSubsystem() { }

    @Override
    public void periodic() {
        // Continuously update motor power to reach/maintain the target position
        outTakeViperSlideMotor.setPower(viperSlideControlSystem.calculate(outTakeViperSlideMotor.getState()));
    }

    // Lifts the slide to the lower basket position
    public final Command extendToLowerBasket = new RunToPosition(viperSlideControlSystem, LowerBasketPosition).requires(this);

    // Retracts the slide to the base (position 0)
    public final Command retractToBase = new RunToPosition(viperSlideControlSystem, 0).requires(this);

    // Opens the basket gate
    public final Command openGate = instant(() -> basketGateServo.setPosition(basketGateOpenPosition));

    // Closes the basket gate
    public final Command closeGate = instant(() -> basketGateServo.setPosition(basketGateClosePosition));
}
