package org.firstinspires.ftc.teamcode.tuning.pedropathing;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Heading Test", group = "Autonomous")
public class HeadingTest extends OpMode {
    private Follower follower;
    private int pathState;

    private PathChain fullPath;

    private TelemetryManager telemetryManager;

    // Requested coordinates
    private final Pose startPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose point2 = new Pose(128, 80, Math.toRadians(180));

    /** This method builds the path for the heading test. */
    public void buildPaths() {
        fullPath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, point2))
                .setLinearHeadingInterpolation(startPose.getHeading(), point2.getHeading())
                .build();
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        buildPaths();
        
        // Ensure telemetry updates quickly for tuning
        telemetry.setMsTransmissionInterval(50);
    }

    @Override
    public void start() {
        pathState = 0;
    }

    @Override
    public void loop() {
        // Essential: Update the follower to process movement and localizer
        follower.update();

        switch (pathState) {
            case 0:
                // Follow the single path segment
                follower.followPath(fullPath);
                pathState = 1;
                break;
            case 1:
                if (!follower.isBusy()) {
                    pathState = -1; // Finished
                }
                break;
        }

        // Telemetry for real-time monitoring of current vs target poses
        telemetryManager.addData("x", follower.getPose().getX());
        telemetryManager.addData("y", follower.getPose().getY());
        telemetryManager.addData("heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetryManager.addData("Path State", pathState);
        telemetryManager.update();
    }
}
