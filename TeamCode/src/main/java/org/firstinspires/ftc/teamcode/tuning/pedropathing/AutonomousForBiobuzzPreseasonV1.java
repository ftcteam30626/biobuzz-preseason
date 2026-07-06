package org.firstinspires.ftc.teamcode.tuning.pedropathing;

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
public class AutonomousForBiobuzzPreseasonV1 extends OpMode {
    private Follower follower;
    private int pathState = 0;
    private Timer pathTimer = new Timer();
    private PathChain[] paths;
    private boolean isWaiting = false;
    private TelemetryManager telemetryManager;

    private final Pose startPose = new Pose(12, 78, Math.toRadians(0));
    private final Pose point2 = new Pose(20, 20, Math.toRadians(45));
    private final Pose point3 = new Pose(66, 54, Math.toRadians(45));
    private final Pose point4 = new Pose(108, 6, Math.toRadians(0));

    @Override
    public void init() {
        Drawing.init();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        
        // Load paths into an array to avoid repetitive code
        paths = new PathChain[] {
            follower.pathBuilder().addPath(new BezierLine(startPose, point2)).setLinearHeadingInterpolation(startPose.getHeading(), point2.getHeading()).build(),
            follower.pathBuilder().addPath(new BezierLine(point2, point3)).setLinearHeadingInterpolation(point2.getHeading(), point3.getHeading()).build(),
            follower.pathBuilder().addPath(new BezierLine(point3, point2)).setLinearHeadingInterpolation(point3.getHeading(), point2.getHeading()).build(),
            follower.pathBuilder().addPath(new BezierLine(point2, point4)).setLinearHeadingInterpolation(point2.getHeading(), point4.getHeading()).build(),
            follower.pathBuilder().addPath(new BezierLine(point4, point2)).setLinearHeadingInterpolation(point4.getHeading(), point2.getHeading()).build(),
            follower.pathBuilder().addPath(new BezierLine(point2, startPose)).setLinearHeadingInterpolation(point2.getHeading(), startPose.getHeading()).build()
        };
        
        telemetry.setMsTransmissionInterval(50);
    }

    @Override
    public void start() {
        pathState = 0;
        isWaiting = false;
    }

    @Override
    public void loop() {
        follower.update();

        // Move to the next path if the follower is finished
        if (pathState < paths.length && !follower.isBusy()) {
            // Check if we need to pause for 4 seconds (after indices 1, 2, 3, 4)
            if (pathState >= 2 && pathState <= 5 && !isWaiting) {
                pathTimer.resetTimer();
                isWaiting = true;
            }

            // If we aren't waiting, or the 4s is up, follow the next segment
            if (!isWaiting || pathTimer.getElapsedTimeSeconds() >= 4) {
                follower.followPath(paths[pathState++]);
                isWaiting = false;
            }
        }

        telemetryManager.addData("x", follower.getPose().getX());
        telemetryManager.addData("y", follower.getPose().getY());
        telemetryManager.addData("heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetryManager.addData("Path Index", pathState);
        telemetryManager.addData("Waiting", isWaiting);
        telemetryManager.update();
        Drawing.drawDebug(follower);
    }
}
