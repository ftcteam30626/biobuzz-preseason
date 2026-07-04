package org.firstinspires.ftc.teamcode.tuning.pedropathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * A simple autonomous OpMode to reset the robot to a specific starting pose.
 */
@Autonomous(name = "Reset", group = "Autonomous")
public class Reset extends OpMode {
    private Follower follower;
    
    // The target startPose coordinates as requested
    private final Pose targetPose = new Pose(82.03968235404191, 10.379491017964071, Math.toRadians(90));

    @Override
    public void init() {
        // Initialize the follower using the helper method in your Constants class
        follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void start() {
        // Create a path from the robot's current estimated position to the targetPose
        Path path = new Path(new BezierLine(follower.getPose(), targetPose));
        path.setConstantHeadingInterpolation(Math.toRadians(90));
        
        // Command the follower to move to the target
        follower.followPath(path);
    }

    @Override
    public void loop() {
        // Must be called every loop to update the robot's position and motor powers
        follower.update();

        // Telemetry to track the reset progress
        telemetry.addData("Status", follower.isBusy() ? "Driving to Reset Pose" : "Arrived at Reset Pose");
        telemetry.addData("Target X", targetPose.getX());
        telemetry.addData("Target Y", targetPose.getY());
        telemetry.addData("Current X", follower.getPose().getX());
        telemetry.addData("Current Y", follower.getPose().getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
}
