package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class IntakeSubsystem implements Subsystem {

    // Creates a intakeSubsystem that can be called by anybody
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();

    // Identifies the motor that controls the intake
    private final MotorEx intakeMotor = new MotorEx("intakeMotor");

    // Speed for moving the intakeMotor
    public static final double startSpeed = 0.75;

    private IntakeSubsystem() { }

    // Starts intake
    public final Command startIntake = instant(() -> intakeMotor.setPower(startSpeed));

    // Stops intake
    public final Command stopIntake = instant(() -> intakeMotor.setPower(0));
}
