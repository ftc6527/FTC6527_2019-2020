package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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

        waitForStart();


    }
    //All below this will be our methods, methods are a big part of our autonomous program as it makes it much more neat and uniform.

}
