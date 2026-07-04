package org.firstinspires.ftc.teamcode.tuning.pedropathing;

import static org.firstinspires.ftc.teamcode.tuning.pedropathing.Tuning.telemetryM;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Pedro Pathing Test", group = "Autonomous")
public class PedroPathingTest extends OpMode {
    private Follower follower;
    private Timer pathTimer;
    private int pathState;

    private PathChain path1, path2, path3, path4;

    // Poses defined from the requested coordinates, all set to 90 degrees
    private final Pose startPose = new Pose(82.03968235404191, 10.379491017964071, Math.toRadians(90));
    private final Pose point2 = new Pose(129.92739520958082, 57.39446107784432, Math.toRadians(90));
    private final Pose point3 = new Pose(129.22283820927152, 109.73049216748115, Math.toRadians(90));
    private final Pose finalPose = new Pose(129.92739520958082, 37.39446107784432, Math.toRadians(90));

    /** This method builds the paths for the robot to follow. */
    public void buildPaths() {
        // Segment 1: Start -> Point 2
        path1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, point2))
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .build();

        // Segment 2: Point 2 -> Point 3
        path2 = follower.pathBuilder()
                .addPath(new BezierLine(point2, point3))
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .build();

        // Segment 3: Point 3 -> Final Pose
        path3 = follower.pathBuilder()
                .addPath(new BezierLine(point3, finalPose))
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .build();

        // Segment 4: Final Pose -> Start Pose
        path4 = follower.pathBuilder()
                .addPath(new BezierLine(finalPose, startPose))
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .build();
    }

    /** This method handles the path following state machine. */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start -> Point 2
                follower.followPath(path1);
                setPathState(1);
                break;

            case 1: // Wait for Point 2
                if (!follower.isBusy()) {
                    setPathState(2);
                }
                break;

            case 2: // Point 2 -> Point 3
                follower.followPath(path2);
                setPathState(3);
                break;

            case 3: // Wait for Point 3
                if (!follower.isBusy()) {
                    setPathState(4);
                }
                break;

            case 4: // Wait 2 seconds at Point 3
                if (pathTimer.getElapsedTimeSeconds() >= 2) {
                    follower.followPath(path3);
                    setPathState(5);
                }
                break;

            case 5: // Wait for Final Pose
                if (!follower.isBusy()) {
                    setPathState(6);
                }
                break;

            case 6: // Wait 2 seconds at Final Pose
                if (pathTimer.getElapsedTimeSeconds() >= 2) {
                    follower.followPath(path4);
                    setPathState(7);
                }
                break;

            case 7: // Wait for return to Start
                if (!follower.isBusy()) {
                    setPathState(-1); // Done
                }
                break;
        }
    }

    /** This method sets the current path state and resets the timer. */
    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        
        // Initialize the localizer at the startPose.
        follower.setStartingPose(startPose);
        telemetryM.update(telemetry);
        follower.update();

        buildPaths();
    }

    @Override
    public void start() {
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        // Telemetry for monitoring progress
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Path State", pathState);
        telemetry.addData("Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}
