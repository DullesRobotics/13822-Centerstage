package org.firstinspires.ftc.teamcode.OpModes.AutonOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class AutonRedStack extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    DcMotor rightLever;
    //DcMotor leftLever;
    Servo bucket;
    Servo door;
    DcMotor intake;
    double speed = 0.5;
    boolean leftTurn = false;
    boolean leftStrafe = false;
    double power;
    boolean middle, left, right;


    private static double ticks = 384.5;
    private static double wheelDiameter = 9.6;
    //ticks per 1mm
    private static double conversion = (ticks / (2 * Math.PI * (wheelDiameter / 2)));
    private double strafeConversion = 1.136;

    double cX = 0;
    double cY = 0;
    double width = 0;

    private OpenCvCamera controlHubCam;  // Use OpenCvCamera class from FTC SDK
    private static final int CAMERA_WIDTH = 640; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 360; // height of wanted camera resolution

    // Calculate the distance using the formula
    public static final double objectWidthInRealWorldUnits = 3.75;  // Replace with the actual width of the object in real-world units
    public static final double focalLength = 728;  // Replace with the focal length of the camera in pixels

    @Override
    public void runOpMode() throws InterruptedException {
        AutonFunction functions = new AutonFunction();

        intake = hardwareMap.get(DcMotor.class, "INTAKE");
        rightLever = hardwareMap.get(DcMotor.class, "RLM");
        //leftLever = hardwareMap.get(DcMotor.class, "LLM");
        bucket = hardwareMap.get(Servo.class, "BUCKET");
        door = hardwareMap.get(Servo.class,"OUTDOOR");

        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM");

        //Fot the robot to drive forward one side of the bot needs to be reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //leftLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        initOpenCV();
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        FtcDashboard.getInstance().startCameraStream(controlHubCam, 30);

        telemetry.addData("Just Checking...", "Did you preload");
        telemetry.update();

        waitForStart();
        resetRuntime();

        while (opModeIsActive()) {
            telemetry.addData("Coordinate", "(" + (int) cX + ", " + (int) cY + ")");
            telemetry.addData("Distance in Inch", (getDistance(width)));
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("FLM", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BLM", backLeftMotor.getCurrentPosition());
            telemetry.addData("FRM", frontRightMotor.getCurrentPosition());
            telemetry.addData("BRM", backRightMotor.getCurrentPosition());
            telemetry.update();

            //driveWithEncoders(.25, -50, 5000);
            //turnWithEncoders(.25, 55,false,2500);
            //strafeWithEncoders(.25, 50, false, 5000);
            //sleep(1000);
            //requestOpModeStop();

            if(cX>0 && cX < 250){
                left = true;
            }if(cX>250 && cX < 390){
                middle = true;
            }if(cX>390 && cX < 640){
                right = true;
            }

            //additionally make left the else option
            if(left){
                driveWithEncoders(.25, -70, 3000);
                turnWithEncoders(.25, 53, true, 2500);
                driveWithEncoders(.25, -15, 1000);
                intake.setPower(.25);
                sleep(250);
                driveWithEncoders(.25, 200,5000);
                intake.setPower(0);
                strafeWithEncoders(.25, 10, false, 1000);
                lever(2000);
                //adjust the following for scoring
                driveWithEncoders(.25, 40,2000);
                door.setPosition(.5);
                sleep(1000);
            }

            //
            if(middle){
                driveWithEncoders(.25, -80, 3000);
                intake.setPower(.25);
                sleep(250);
                driveWithEncoders(.25, 20,1500);
                intake.setPower(0);
                turnWithEncoders(.25, 55,true,2000);
                //strafeWithEncoders(.25, 10, false,2000);
                driveWithEncoders(.25,200,5000);
                //strafeWithEncoders(.25, 13, false, 1500);
                lever(2000);
                //adjust the following for scoring
                driveWithEncoders(.25, 30,2000);
                door.setPosition(.5);
                sleep(1000);
                driveWithEncoders(.25, -15, 1000);
                strafeWithEncoders(.25, 50, true, 2000);
                driveWithEncoders(.25,15,1000);
            }
            if(right){
                driveWithEncoders(.25, -90, 3000);
                turnWithEncoders(.25, 55, false, 2500);
                driveWithEncoders(.25, -15, 1000);
                intake.setPower(.25);
                sleep(250);
                driveWithEncoders(.25, 20,1500);
                intake.setPower(0);
                strafeWithEncoders(.25, 40, false, 1000);
                turnWithEncoders(.25, 110,true,4500);
                driveWithEncoders(.25,190,5000);
                //strafeWithEncoders(.25,20,true,2000);
                lever(2000);
                //adjust the following for scoring
                driveWithEncoders(.25, 38,2000);
                door.setPosition(.5);
                sleep(1000);
                //driveWithEncoders(.25, -15, 1000);
                //strafeWithEncoders(.25, 40, false, 2000);
                //driveWithEncoders(.25,15,1000);
            }
            //functions.driveWithEncoders(.5,10000);
            //driveWithEncoders(.25, 50, 1000);
            //sleep(5000);
            requestOpModeStop();
        }

        controlHubCam.stopStreaming();

    }

    private void lever(int wait){
        if(((Math.abs(rightLever.getTargetPosition()) - Math.abs(rightLever.getCurrentPosition())) >= 25)){
            power = .25;
        } else{
            power = 1;
        }
        rightLever.setPower(power);
        //leftLever.setPower(power);
        rightLever.setTargetPosition(545);
        //leftLever.setTargetPosition(300);
        rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //leftLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sleep(250);
        bucket.setPosition(.15);
        sleep(wait);
    }
    private void setStrafe() {
        if (leftStrafe) {
            frontLeftMotor.setPower(speed);
            backLeftMotor.setPower(-speed);
            frontRightMotor.setPower(-speed);
            backRightMotor.setPower(speed);
        } else {
            frontLeftMotor.setPower(-speed);
            backLeftMotor.setPower(speed);
            frontRightMotor.setPower(speed);
            backRightMotor.setPower(-speed);
        }
    }

    private void setSpeed() {
        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);
    }

    private void setTurn() {
        if (leftTurn) {
            frontLeftMotor.setPower(-speed);
            backLeftMotor.setPower(-speed);
            frontRightMotor.setPower(speed);
            backRightMotor.setPower(speed);
        } else {
            frontLeftMotor.setPower(speed);
            backLeftMotor.setPower(speed);
            frontRightMotor.setPower(-speed);
            backRightMotor.setPower(-speed);
        }
    }
    public void strafeWithEncoders(double speed, int distance, boolean leftStrafe, int wait){
        frontLeftMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        backLeftMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        frontRightMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        backRightMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(leftStrafe) {
            frontLeftMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
            backLeftMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
            frontRightMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
            backRightMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
        }if(!leftStrafe){
            frontLeftMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
            backLeftMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
            frontRightMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
            backRightMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
        }

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sleep(wait);
    }

    public void turnWithEncoders(double speed, int distance,boolean leftTurn, int wait){
        frontLeftMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        backLeftMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        frontRightMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        backRightMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));

        //distance is referring to the distance the wheels need to spin for a turn
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if(leftTurn) {
            frontLeftMotor.setTargetPosition((int) (distance * conversion));
            backLeftMotor.setTargetPosition((int) (distance * conversion));
            frontRightMotor.setTargetPosition((int) (-distance * conversion));
            backRightMotor.setTargetPosition((int) (-distance * conversion));
        } else{
            frontLeftMotor.setTargetPosition((int) (-distance * conversion));
            backLeftMotor.setTargetPosition((int) (-distance * conversion));
            frontRightMotor.setTargetPosition((int) (distance * conversion));
            backRightMotor.setTargetPosition((int) (distance * conversion));
        }

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sleep(wait);
    }

    public void driveWithEncoders(double speed, int distance, int wait) {
        frontLeftMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        backLeftMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        frontRightMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        backRightMotor.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);

        frontLeftMotor.setTargetPosition((int)(distance * conversion));
        backLeftMotor.setTargetPosition((int)(distance * conversion));
        frontRightMotor.setTargetPosition((int)(distance * conversion));
        backRightMotor.setTargetPosition((int)(distance * conversion));

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sleep(wait);
    }
    private void initOpenCV() {

        // Create an instance of the camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Use OpenCvCameraFactory class from FTC SDK to create camera instance
        controlHubCam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        controlHubCam.setPipeline(new AutonRedStack.YellowBlobDetectionPipeline());

        controlHubCam.openCameraDevice();
        controlHubCam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
    }

    private class YellowBlobDetectionPipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            // Preprocess the frame to detect yellow regions
            Mat yellowMask = preprocessFrame(input);

            // Find contours of the detected yellow regions
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(yellowMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Find the largest yellow contour (blob)
            MatOfPoint largestContour = findLargestContour(contours);

            if (largestContour != null) {
                // Draw a red outline around the largest detected object
                Imgproc.drawContours(input, contours, contours.indexOf(largestContour), new Scalar(255, 0, 0), 2);
                // Calculate the width of the bounding box
                width = calculateWidth(largestContour);

                // Display the width next to the label
                String widthLabel = "Width: " + (int) width + " pixels";
                Imgproc.putText(input, widthLabel, new Point(cX + 10, cY + 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                //Display the Distance
                String distanceLabel = "Distance: " + String.format("%.2f", getDistance(width)) + " inches";
                Imgproc.putText(input, distanceLabel, new Point(cX + 10, cY + 60), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                // Calculate the centroid of the largest contour
                Moments moments = Imgproc.moments(largestContour);
                cX = moments.get_m10() / moments.get_m00();
                cY = moments.get_m01() / moments.get_m00();

                // Draw a dot at the centroid
                String label = "(" + (int) cX + ", " + (int) cY + ")";
                Imgproc.putText(input, label, new Point(cX + 10, cY), Imgproc.FONT_HERSHEY_COMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                Imgproc.circle(input, new Point(cX, cY), 5, new Scalar(0, 255, 0), -1);

            }

            return input;
        }

        private Mat preprocessFrame(Mat frame) {
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            Scalar lowerYellow = new Scalar(100, 100, 100);
            Scalar upperYellow = new Scalar(180, 255, 255);


            Mat yellowMask = new Mat();
            Core.inRange(hsvFrame, lowerYellow, upperYellow, yellowMask);

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
            Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_OPEN, kernel);
            Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_CLOSE, kernel);

            return yellowMask;
        }

        private MatOfPoint findLargestContour(List<MatOfPoint> contours) {
            double maxArea = 0;
            MatOfPoint largestContour = null;

            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > maxArea) {
                    maxArea = area;
                    largestContour = contour;
                }
            }

            return largestContour;
        }

        private double calculateWidth(MatOfPoint contour) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            return boundingRect.width;
        }
    }
    private static double getDistance(double width){
        double distance = (objectWidthInRealWorldUnits * focalLength) / width;
        return distance;
    }
}
