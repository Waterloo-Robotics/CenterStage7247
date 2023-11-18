package com.ftc.waterloo.h2oloobots;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.List;

public class CameraControl {

    TelemetryControl telemetryControl;
    static final int STREAM_WIDTH = 640; // modify for your camera
    static final int STREAM_HEIGHT = 480; // modify for your camera
    int width = 640;
    int height = 480;
    OpenCvWebcam webcam;
    BluePropPipeline bluePropPipeline;
    RedPropPipeline redPropPipeline;

    public enum Alliance {
        BLUE,
        RED
    }

    Alliance alliance = Alliance.RED;

    public enum PropLocation {
        LEFT,
        CENTER,
        RIGHT,
        NONE
    }


//    private AprilTagProcessor aprilTag;

//    private VisionPortal visionPortal;

    public CameraControl(HardwareMap hardwareMap, TelemetryControl telemetryControl, @NonNull Alliance alliance) {

        this.telemetryControl = telemetryControl;
        this.alliance = alliance;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = null;
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1"); // put your camera's name here
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        bluePropPipeline = new BluePropPipeline(width);
        redPropPipeline = new RedPropPipeline(width);
        switch (alliance) {
            case RED:
                webcam.setPipeline(redPropPipeline);
            break;

            case BLUE:
                webcam.setPipeline(bluePropPipeline);
            break;
        }
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(STREAM_WIDTH, STREAM_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetryControl.addData("Camera Failed","");
                telemetryControl.update();
            }
        });
        FtcDashboard.getInstance().startCameraStream(webcam,60);

        /*// Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)

                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);*/

    }

    public void stream() {

        telemetryControl.startCameraStream(webcam, 60);

    }

    public void close() {

        webcam.closeCameraDevice();

    }

    public PropLocation getLocation() {

        PropLocation location;
        switch (alliance) {

            case BLUE:
                location = bluePropPipeline.getLocation();
            break;

            default:
            case RED:
                location = redPropPipeline.getLocation();
            break;

        }

        return location;
    }

/*    @SuppressLint("DefaultLocale")
    public void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetryControl.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetryControl.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetryControl.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetryControl.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetryControl.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetryControl.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetryControl.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetryControl.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetryControl.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetryControl.addLine("RBE = Range, Bearing & Elevation");

    }*/

}

class RedPropPipeline extends OpenCvPipeline {

    private int width; // width of the image
    CameraControl.PropLocation location;

    /**
     *
     * @param width The width of the image (check your camera)
     */
    public RedPropPipeline(int width) {
        this.width = width;
    }

    @Override
    public Mat processFrame(Mat input) {
        // "Mat" stands for matrix, which is basically the image that the detector will process
        // the input matrix is the image coming from the camera
        // the function will return a matrix to be drawn on your phone's screen

        // The detector detects regular stones. The camera fits two stones.
        // If it finds one regular stone then the other must be the skystone.
        // If both are regular stones, it returns NONE to tell the robot to keep looking

        // Make a working copy of the input matrix in HSV
        Mat mat = new Mat();
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        // if something is wrong, we assume there's no skystone
        if (mat.empty()) {
            location = CameraControl.PropLocation.NONE;
            return input;
        }

        // We create a HSV range for yellow to detect regular stones
        // NOTE: In OpenCV's implementation,
        // Hue values are half the real value
        Scalar upperBlue = new Scalar(139, 255, 180);
        Scalar lowerBlue = new Scalar(98, 50, 25);
        Scalar upperRed = new Scalar(15, 255, 255);
        Scalar lowerRed = new Scalar(0, 50, 25);
        Mat thresh = new Mat();

        // We'll get a black and white image. The white regions represent the regular stones.
        // inRange(): thresh[i][j] = {255,255,255} if mat[i][i] is within the range
        Core.inRange(mat, lowerRed, upperRed, thresh);
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, new Mat());

        Imgproc.GaussianBlur(thresh, thresh, new Size(5.0, 15.0), 0.0);

        // Use Canny Edge Detection to find edges
        // you might have to tune the thresholds for hysteresis
        Mat edges = new Mat();
//        Imgproc.Canny(thresh, edges, 100, 300);

        // https://docs.opencv.org/3.4/da/d0c/tutorial_bounding_rects_circles.html
        // Oftentimes the edges are disconnected. findContours connects these edges.
        // We then find the bounding rectangles of those contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        double maxVal = 0;
        int maxValIdx = 0;
        if (!contours.isEmpty()) {
            for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {

                double contourArea = Imgproc.contourArea(contours.get(contourIdx));

                if (maxVal < contourArea) {
                    maxVal = contourArea;
                    maxValIdx = contourIdx;
                }

            }
//        Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));
            Imgproc.drawContours(input, contours, maxValIdx, new Scalar(255, 0, 0));

            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
            Rect[] boundRect = new Rect[contours.size()];
//        for (int i = 0; i < contours.size(); i++) {
//            contoursPoly[i] = new MatOfPoint2f();
//            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
//            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
//        }

            contoursPoly[maxValIdx] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(maxValIdx).toArray()), contoursPoly[maxValIdx], 3, true);
            boundRect[maxValIdx] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[maxValIdx].toArray()));

            // Iterate and check whether the bounding boxes
            // cover left and/or right side of the image
            double left_x = 0.25 * width;
            double right_x = 0.825 * width;
            boolean left = false; // true if regular stone found on the left side
            boolean right = false; // "" "" on the right side
            boolean center = false;
//        for (int i = 0; i != boundRect.length; i++) {
//            if (boundRect[i].x < left_x)
//                left = true;
//            else if (boundRect[i].x + boundRect[i].width > right_x)
//                right = true;
//            else
//                center = true;
//
//            // draw red bounding rectangles on mat
//            // the mat has been converted to HSV so we need to use HSV as well
//            Imgproc.rectangle(input, boundRect[i], new Scalar(0.5, 76.9, 89.8));
//        }

            if (boundRect[maxValIdx].x < left_x)
                left = true;
            else if (boundRect[maxValIdx].x + boundRect[maxValIdx].width > right_x)
                right = true;
            else
                center = true;

            // draw red bounding rectangles on mat
            // the mat has been converted to HSV so we need to use HSV as well
            Imgproc.rectangle(input, boundRect[maxValIdx], new Scalar(0.5, 76.9, 89.8));

            // if there is no yellow regions on a side
            // that side should be a Skystone
            if (left) location = CameraControl.PropLocation.LEFT;
            else if (right) location = CameraControl.PropLocation.RIGHT;
            else if (center) location = CameraControl.PropLocation.CENTER;
                // if both are true, then there's no Skystone in front.
                // since our team's camera can only detect two at a time
                // we will need to scan the next 2 stones
            else location = CameraControl.PropLocation.NONE;
        } else {
            location = CameraControl.PropLocation.CENTER;
        }

        return input; // return the mat with rectangles drawn
    }

    public CameraControl.PropLocation getLocation() {
        return this.location;
    }

}

class BluePropPipeline extends OpenCvPipeline {

    private int width; // width of the image
    CameraControl.PropLocation location;

    /**
     *
     * @param width The width of the image (check your camera)
     */
    public BluePropPipeline(int width) {
        this.width = width;
    }

    @Override
    public Mat processFrame(Mat input) {
        // "Mat" stands for matrix, which is basically the image that the detector will process
        // the input matrix is the image coming from the camera
        // the function will return a matrix to be drawn on your phone's screen

        // The detector detects regular stones. The camera fits two stones.
        // If it finds one regular stone then the other must be the skystone.
        // If both are regular stones, it returns NONE to tell the robot to keep looking

        // Make a working copy of the input matrix in HSV
        Mat mat = new Mat();
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        // if something is wrong, we assume there's no skystone
        if (mat.empty()) {
            location = CameraControl.PropLocation.NONE;
            return input;
        }

        // We create a HSV range for yellow to detect regular stones
        // NOTE: In OpenCV's implementation,
        // Hue values are half the real value
        Scalar upperBlue = new Scalar(139, 255, 180);
        Scalar lowerBlue = new Scalar(98, 50, 25);
        Mat thresh = new Mat();

        // We'll get a black and white image. The white regions represent the regular stones.
        // inRange(): thresh[i][j] = {255,255,255} if mat[i][i] is within the range
        Core.inRange(mat, lowerBlue, upperBlue, thresh);
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, new Mat());

        Imgproc.GaussianBlur(thresh, thresh, new Size(5.0, 15.0), 0.0);

        // Use Canny Edge Detection to find edges
        // you might have to tune the thresholds for hysteresis
        Mat edges = new Mat();
//        Imgproc.Canny(thresh, edges, 100, 300);

        // https://docs.opencv.org/3.4/da/d0c/tutorial_bounding_rects_circles.html
        // Oftentimes the edges are disconnected. findContours connects these edges.
        // We then find the bounding rectangles of those contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        double maxVal = 0;
        int maxValIdx = 0;

        if (!contours.isEmpty()) {
            for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {

                double contourArea = Imgproc.contourArea(contours.get(contourIdx));

                if (maxVal < contourArea) {
                    maxVal = contourArea;
                    maxValIdx = contourIdx;
                }

            }
//        Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));
            Imgproc.drawContours(input, contours, maxValIdx, new Scalar(255, 0, 0));

            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
            Rect[] boundRect = new Rect[contours.size()];
//        for (int i = 0; i < contours.size(); i++) {
//            contoursPoly[i] = new MatOfPoint2f();
//            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
//            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
//        }

            contoursPoly[maxValIdx] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(maxValIdx).toArray()), contoursPoly[maxValIdx], 3, true);
            boundRect[maxValIdx] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[maxValIdx].toArray()));

            // Iterate and check whether the bounding boxes
            // cover left and/or right side of the image
            double left_x = 0.25 * width;
            double right_x = 0.825 * width;
            boolean left = false; // true if regular stone found on the left side
            boolean right = false; // "" "" on the right side
            boolean center = false;
//        for (int i = 0; i != boundRect.length; i++) {
//            if (boundRect[i].x < left_x)
//                left = true;
//            else if (boundRect[i].x + boundRect[i].width > right_x)
//                right = true;
//            else
//                center = true;
//
//            // draw red bounding rectangles on mat
//            // the mat has been converted to HSV so we need to use HSV as well
//            Imgproc.rectangle(input, boundRect[i], new Scalar(0.5, 76.9, 89.8));
//        }

            if (boundRect[maxValIdx].x < left_x)
                left = true;
            else if (boundRect[maxValIdx].x + boundRect[maxValIdx].width > right_x)
                right = true;
            else
                center = true;

            // draw red bounding rectangles on mat
            // the mat has been converted to HSV so we need to use HSV as well
            Imgproc.rectangle(input, boundRect[maxValIdx], new Scalar(0.5, 76.9, 89.8));

            // if there is no yellow regions on a side
            // that side should be a Skystone
            if (left) location = CameraControl.PropLocation.LEFT;
            else if (right) location = CameraControl.PropLocation.RIGHT;
            else if (center) location = CameraControl.PropLocation.CENTER;
                // if both are true, then there's no Skystone in front.
                // since our team's camera can only detect two at a time
                // we will need to scan the next 2 stones
            else location = CameraControl.PropLocation.NONE;
        } else {
            location = CameraControl.PropLocation.CENTER;
        }

        return input; // return the mat with rectangles drawn
    }

    public CameraControl.PropLocation getLocation() {
        return this.location;
    }

}
