package org.firstinspires.ftc.teamcode;

import android.graphics.ColorSpace;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.video.BackgroundSubtractorKNN;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvTrackerApiPipeline;

import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name="Back-Strafe-Right")
public class BackStrafeAuto extends LinearOpMode
{
    //Declaration of motors and servos below
    //Declaring Motors
    public DcMotor BR;
    public DcMotor BL;
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor Arm;
    public DcMotor Spool;
    public DcMotor SM;

    //Declaring Servos
    public Servo LS;
    public Servo RS;

    //Detection Stuff
    private static int valMid = -1;
    private static int valLeft = -1;
    private static int valRight = -1;

    private static float rectHeight = .6f/8f;
    private static float rectWidth = 1.5f/8f;

    private static float offsetX = 0f/8f;//changing this moves the three rects and the three circles left or right, range : (-2, 2) not inclusive
    private static float offsetY = 0f/8f;//changing this moves the three rects and circles up or down, range: (-4, 4) not inclusive

    private static float[] midPos = {4f/8f+offsetX, 4f/8f+offsetY};//0 = col, 1 = row
    private static float[] leftPos = {2f/8f+offsetX, 4f/8f+offsetY};
    private static float[] rightPos = {6f/8f+offsetX, 4f/8f+offsetY};
    //moves all rectangles right or left by amount. units are in ratio to monitor

    private final int rows = 640;
    private final int cols = 480;


    //Declaring phone camera
    OpenCvCamera phoneCam;

    public void runOpMode() throws InterruptedException
    {
        /**Camera Stuffs Starts Here**/
        //Initializing Camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        //Allowing connection to camera
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(new BlockDetection());

        //Start to stream
        phoneCam.startStreaming(rows, cols, OpenCvCameraRotation.UPRIGHT);

        /** And ends here!**/

        //Initializing Motors
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        FL = hardwareMap.dcMotor.get("FL");
        FR = hardwareMap.dcMotor.get("FR");
        Arm = hardwareMap.dcMotor.get("Arm");
        Spool = hardwareMap.dcMotor.get("Spool");
        SM = hardwareMap.dcMotor.get("SM");

        //Initializing Servos
        LS = hardwareMap.servo.get("LS");
        RS = hardwareMap.servo.get("RS");

        //Reversing Motors
        BL.setDirection(DcMotor.Direction.REVERSE);
        FL.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Camera FPS: ", phoneCam.getFps());
        telemetry.addData("Camera (possible) max FPS: ", phoneCam.getCurrentPipelineMaxFps());

        waitForStart();
        telemetry.addData("Values", valLeft + "  " + valMid + "  " + valRight);
        telemetry.update();
        if(valLeft == 0)
        {
            FBMove(1, 4);
        }
        /**FL.setPower(-1);
        BL.setPower(-1);
        FR.setPower(-1);
        BR.setPower(-1);
        Thread.sleep(500);
        FL.setPower(0);
        BL.setPower(0);
        FR.setPower(0);
        BR.setPower(0);
        Thread.sleep(1000);
        SM.setPower(1);
        Thread.sleep(1000);
        SM.setPower(0);
        ResetEncoders();
         **/

    }

    //Stupid pipeline detection because stupid
    static class BlockDetection extends OpenCvPipeline {
        Mat yCbCrChan2Mat = new Mat();
        Mat thresholdMat = new Mat();
        Mat all = new Mat();

        List<MatOfPoint> contoursList = new ArrayList<>();

        enum Stage {
            detection,
            THRESHOLD,
            RAW_IMAGE,
        }

        public Stage stageToRenderToViewport = Stage.detection;
        public Stage[] stages = Stage.values();


        public void OnViewportTapped()
        {
            int currentStageNum = stageToRenderToViewport.ordinal();

            int nextStageNum = currentStageNum + 1;

            if(nextStageNum >= stages.length)
            {
                nextStageNum = 0;
            }

            stageToRenderToViewport = stages[nextStageNum];
        }

        @Override
        //Mat for color conversion stuffs
        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);//converts rgb to ycrcb
            Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);

            Imgproc.threshold(yCbCrChan2Mat, thresholdMat, 102, 255, Imgproc.THRESH_BINARY_INV);

            //outline/contour
            Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            yCbCrChan2Mat.copyTo(all);//copies mat object
            //Imgproc.drawContours(all, contoursList, -1, new Scalar(255, 0, 0), 3, 8);//draws blue contours


            //get values from frame
            double[] pixMid = thresholdMat.get((int)(input.rows()* midPos[1]), (int)(input.cols()* midPos[0]));//gets value at circle
            valMid = (int)pixMid[0];

            double[] pixLeft = thresholdMat.get((int)(input.rows()* leftPos[1]), (int)(input.cols()* leftPos[0]));//gets value at circle
            valLeft = (int)pixLeft[0];

            double[] pixRight = thresholdMat.get((int)(input.rows()* rightPos[1]), (int)(input.cols()* rightPos[0]));//gets value at circle
            valRight = (int)pixRight[0];

            //create three points
            Point pointMid = new Point((int)(input.cols()* midPos[0]), (int)(input.rows()* midPos[1]));
            Point pointLeft = new Point((int)(input.cols()* leftPos[0]), (int)(input.rows()* leftPos[1]));
            Point pointRight = new Point((int)(input.cols()* rightPos[0]), (int)(input.rows()* rightPos[1]));

            //draw circles on those points
            Imgproc.circle(all, pointMid,5, new Scalar( 255, 0, 0 ),1 );//draws circle
            Imgproc.circle(all, pointLeft,5, new Scalar( 255, 0, 0 ),1 );//draws circle
            Imgproc.circle(all, pointRight,5, new Scalar( 255, 0, 0 ),1 );//draws circle

            //draw 3 rectangles
            Imgproc.rectangle(//1-3
                    all,
                    new Point(
                            input.cols()*(leftPos[0]-rectWidth/2),
                            input.rows()*(leftPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(leftPos[0]+rectWidth/2),
                            input.rows()*(leftPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);
            Imgproc.rectangle(//3-5
                    all,
                    new Point(
                            input.cols()*(midPos[0]-rectWidth/2),
                            input.rows()*(midPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(midPos[0]+rectWidth/2),
                            input.rows()*(midPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);
            Imgproc.rectangle(//5-7
                    all,
                    new Point(
                            input.cols()*(rightPos[0]-rectWidth/2),
                            input.rows()*(rightPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(rightPos[0]+rectWidth/2),
                            input.rows()*(rightPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);

            switch (stageToRenderToViewport)
            {
                case THRESHOLD:
                {
                    return thresholdMat;
                }

                case detection:
                {
                    return all;
                }

                case RAW_IMAGE:
                {
                    return input;
                }

                default:
                {
                    return input;
                }
            }
        }

    }


    public void FBMove(double power, int target)
    {
        ResetEncoders();
        FL.setTargetPosition(target * 2240);
        AllRunToPos();
        while (FL.isBusy()) {
            FL.setPower(power);
            BL.setPower(power);
            FR.setPower(power);
            BR.setPower(power);
        }
        RunWithoutEncoders();
    }

    public void AllRunToPos()
    {
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void ResetEncoders()
    {
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void RunWithoutEncoders()
    {
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        SM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void RightTurn(double power, int target)
    {
        ResetEncoders();
        FL.setTargetPosition(target);
        AllRunToPos();
        while (FL.isBusy()) {
            FL.setPower(power);
            BL.setPower(power);
            FR.setPower(-power);
            BR.setPower(-power);
        }
        RunWithoutEncoders();
    }

    public void LeftTurn(double power, int target)
    {
        ResetEncoders();
        FL.setTargetPosition(target);
        AllRunToPos();
        while (FL.isBusy()) {
            FL.setPower(-power);
            BL.setPower(-power);
            FR.setPower(power);
            BR.setPower(power);
        }
        RunWithoutEncoders();
    }
    public void Strafe(double power, int target)
    {
        ResetEncoders();
        SM.setTargetPosition(target);
        AllRunToPos();
    }
}