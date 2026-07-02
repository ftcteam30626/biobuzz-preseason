package org.firstinspires.ftc.teamcode.tuning.motors;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

/**
 * A simple motor movement subsystem using NextFTC command architecture.
 */
public class MotorMovementSubsystem implements Subsystem {
    // Singleton pattern instance
    public static final MotorMovementSubsystem INSTANCE = new MotorMovementSubsystem();

    private MotorMovementSubsystem() { }

    private MotorEx motor = new MotorEx("leftFront");

    // Command definitions that declare this subsystem as a requirement
    public final Command startCommand = new SetPower(motor, 1.0).requires(this);
    public final Command stopCommand = new SetPower(motor, 0.0).requires(this);
}