package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;

@Autonomous(name="AutoVision")
public class AutoVision extends LinearOpMode



{
    //Declaration of motors and servos below
    //Declaring Motors
    public DcMotor BR;
    public DcMotor BL;
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor LA;
    public DcMotor CG;
    public DcMotor CL;
    public DcMotor CR;

    //Declaring Servos
    public Servo HK;

    //Vuforia being declared
    private static final String TFOD_MODEL_ASSET = "Skystone FTC";
    private static final String LABEL_FOUNDATION = "Foundation";
    //Vuforia license key
    private static final String VUFORIA_KEY = "AXudchL/////AAABmaroK78zg0ZFpS1V9FRQ779Ts9dWY6EpHQGLt6dKWb8ocrUgdPeJ+/Ps1eVUW08uXKxF9Vt5LjTBuGHK1Pr7SN9yoDYZ86up4KUkiIEFpBInbKzipptJvUNXHEVA6trUzg3QDZBSctZPE4dJkK7PV6F4S2idWsWzMoyDP4SDIg6Tgo7JcK3HadqRIofDjjeRB0tM5IWhOE1DhPZ4U+OR5Y82mfnPqAYVLvX3EnhPejOsR8Q3Z9a7BQVJIdZtdZCEeWkaLb1jUYROvngXqYOTnQ4oUYtmPydrVCbLQgBMXaemwx64/A2pEGSIk4IPB86p/mLEISN/+1iDcpsz0NwYGF2knXWZvSD1KQSWPNO4ALv6";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void runOpMode() throws InterruptedException
    {
        //Initialization of hardware and setting starting positions of servos, as well as reversing motor directions
        //Initializing Motors
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        FL = hardwareMap.dcMotor.get("FL");
        FR = hardwareMap.dcMotor.get("FR");
        LA = hardwareMap.dcMotor.get("LA");
        CG = hardwareMap.dcMotor.get("CG");
        CL = hardwareMap.dcMotor.get("CL");
        CR = hardwareMap.dcMotor.get("CR");

        //Initializing Servos
        HK = hardwareMap.servo.get("HK");

        //Reversing Motors
        BL.setDirection(DcMotor.Direction.REVERSE);
        FL.setDirection(DcMotor.Direction.REVERSE);
        CR.setDirection(DcMotor.Direction.REVERSE);

        //vuforia initialization
        initVuforia();
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();

        waitForStart();



    }
    //All below this will be our methods, methods are a big part of our autonomous program as it makes it much more neat and uniform.
    //Vuforia initialization of object detection
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FOUNDATION);
    }

    //Vuforia initialization
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }
}
