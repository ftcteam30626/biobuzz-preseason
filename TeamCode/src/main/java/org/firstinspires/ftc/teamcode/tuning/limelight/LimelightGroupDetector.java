package org.firstinspires.ftc.teamcode.tuning.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Autonomous OpMode to detect and group balls using Limelight 3A.
 * It identifies clusters of balls, counts the total number of balls,
 * and identifies the closest group based on total area.
 */
@Autonomous(name = "Autonomous Limelight Group Detector", group = "Vision")
public class LimelightGroupDetector extends LinearOpMode {

    private Limelight3A limelight;

    // Threshold for grouping (in degrees). 
    private final double GROUPING_THRESHOLD = 6.0; 

    // Internal class to unify different detection types (Neural or Color)
    static class Ball {
        double x, y, area;
        Ball(double x, double y, double area) {
            this.x = x;
            this.y = y;
            this.area = area;
        }
    }

    @Override
    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        
        // Start Limelight and ensure it's on Pipeline 1
        limelight.start();
        limelight.pipelineSwitch(1);

        telemetry.addData("Status", "Limelight Initialized - Waiting for Start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            List<Ball> allBalls = new ArrayList<>();

            if (result != null) {
                // Collect detections from both Neural and Color pipelines.
                // We ignore result.isValid() because it often remains false unless a primary target is locked,
                // but we still want to process the raw detection lists.
                List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
                if (detectorResults != null) {
                    for (LLResultTypes.DetectorResult dr : detectorResults) {
                        allBalls.add(new Ball(dr.getTargetXDegrees(), dr.getTargetYDegrees(), dr.getTargetArea()));
                    }
                }

                List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                if (colorResults != null) {
                    for (LLResultTypes.ColorResult cr : colorResults) {
                        allBalls.add(new Ball(cr.getTargetXDegrees(), cr.getTargetYDegrees(), cr.getTargetArea()));
                    }
                }
            }

            // --- TELEMETRY ---
            // Always display the count to confirm OpMode is processing
            telemetry.addData("TOTAL BALL COUNT", allBalls.size());

            if (result != null) {
                telemetry.addData("Active Pipeline", result.getPipelineIndex());

                // Strictly enforce Pipeline 1
                if (result.getPipelineIndex() != 1) {
                    limelight.pipelineSwitch(1);
                }

                if (!allBalls.isEmpty()) {
                    // Group the detections based on proximity
                    List<List<Ball>> groups = groupBalls(allBalls);
                    
                    telemetry.addData("Total Groups", groups.size());
                    telemetry.addLine("--- Group Details ---");

                    int closestGroupIdx = -1;
                    double maxArea = -1;

                    for (int i = 0; i < groups.size(); i++) {
                        List<Ball> group = groups.get(i);
                        
                        double sumX = 0, sumY = 0, totalArea = 0;
                        for (Ball b : group) {
                            sumX += b.x;
                            sumY += b.y;
                            totalArea += b.area;
                        }
                        double avgX = sumX / group.size();
                        double avgY = sumY / group.size();

                        // Identify the group with the largest area (likely the closest)
                        if (totalArea > maxArea) {
                            maxArea = totalArea;
                            closestGroupIdx = i;
                        }

                        telemetry.addLine(String.format(Locale.US, "G%d: %d balls @ (%.1f, %.1f)", 
                                (i + 1), group.size(), avgX, avgY));
                    }

                    if (closestGroupIdx != -1) {
                        telemetry.addData("Targeting", "Group " + (closestGroupIdx + 1));
                    }
                } else {
                    telemetry.addLine("No balls detected. Check Pipeline 1 settings.");
                }
            } else {
                telemetry.addData("Limelight", "No data received (Check connection)");
            }

            telemetry.update();
            idle();
        }

        limelight.stop();
    }

    /**
     * Groups balls based on proximity to the group's center (centroid).
     */
    private List<List<Ball>> groupBalls(List<Ball> balls) {
        List<List<Ball>> groups = new ArrayList<>();

        for (Ball ball : balls) {
            boolean addedToGroup = false;

            for (List<Ball> group : groups) {
                if (isCloseToGroup(ball, group)) {
                    group.add(ball);
                    addedToGroup = true;
                    break;
                }
            }

            if (!addedToGroup) {
                List<Ball> newGroup = new ArrayList<>();
                newGroup.add(ball);
                groups.add(newGroup);
            }
        }
        return groups;
    }

    /**
     * Checks if a ball is close enough to the centroid of a group to be included.
     */
    private boolean isCloseToGroup(Ball ball, List<Ball> group) {
        double sumX = 0, sumY = 0;
        for (Ball member : group) {
            sumX += member.x;
            sumY += member.y;
        }
        double avgX = sumX / group.size();
        double avgY = sumY / group.size();

        double dist = Math.sqrt(Math.pow(ball.x - avgX, 2) + Math.pow(ball.y - avgY, 2));
        return dist < GROUPING_THRESHOLD;
    }
}
