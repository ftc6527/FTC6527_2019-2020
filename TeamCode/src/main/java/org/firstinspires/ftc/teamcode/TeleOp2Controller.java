package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TeleOp2Controller")
public class TeleOp2Controller extends LinearOpMode
{

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

        while (opModeIsActive())
        {
            FL.setPower(gamepad1.left_stick_y+gamepad1.right_stick_x);
            BL.setPower(gamepad1.left_stick_y+gamepad1.right_stick_x);
            BR.setPower(gamepad1.left_stick_y-gamepad1.right_stick_x);
            FR.setPower(gamepad1.left_stick_y-gamepad1.right_stick_x);

            if(gamepad2.dpad_right)
            {
                HK.setPosition(0.4);
            }
            else if (gamepad2.dpad_left)
            {
                HK.setPosition(.6);
            }
            else
            {
                HK.setPosition(0.5);
            }

            CL.setPower(gamepad2.left_stick_y);
            CL.setPower(-gamepad2.left_stick_y);

            if(gamepad2.left_bumper)
            {
                CG.setPower(1);
            }
            else
            {
                CG.setPower(0);
            }

            if (gamepad2.right_bumper)
            {
                CG.setPower(-1);
            }
            else
            {
                CG.setPower(0);
            }
            if(gamepad2.a)
            {
                LA.setPower(1);
            }
            else
            {
                LA.setPower(0);
            }

            if(gamepad2.b)
            {
                LA.setPower(-1);
            }
            else
            {
                LA.setPower(0);
            }

            if(gamepad2.dpad_up)
            {
                CL.setPower(1);
                CR.setPower(1);
            }
            else
            {
                CL.setPower(0);
                CR.setPower(0);
            }

            if(gamepad2.dpad_down)
            {
                CL.setPower(-1);
                CR.setPower(-1);
            }
            else
            {
                CL.setPower(0);
                CR.setPower(0);
            }
            idle();
        }
    }
}
