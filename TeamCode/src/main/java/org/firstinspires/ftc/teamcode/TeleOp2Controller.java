package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TeleOp2Controller")
public class  TeleOp2Controller extends LinearOpMode
{
    //Declaring Motors
    public DcMotor BR;
    public DcMotor BL;
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor SM;
    public DcMotor Arm;
    public DcMotor Spool;

    //Declaring Servos
    public Servo RS;
    public Servo LS;
    public Servo Claw;
    public Servo Align;

    public void runOpMode() throws InterruptedException
    {

        //Initializing Motors
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        FL = hardwareMap.dcMotor.get("FL");
        FR = hardwareMap.dcMotor.get("FR");
        SM = hardwareMap.dcMotor.get("SM");
        Arm = hardwareMap.dcMotor.get("Arm");
        Spool =hardwareMap.dcMotor.get("Spool");

        //Initializing Servos
        RS = hardwareMap.servo.get("RS");
        LS = hardwareMap.servo.get("LS");
        Claw = hardwareMap.servo.get("Claw");
        Align =hardwareMap.servo.get("Align");

        //Reversing Motors
        BR.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Arm Location", Arm.getCurrentPosition());
        telemetry.update();


        waitForStart();

        while (opModeIsActive())
        {
            FL.setPower(gamepad1.left_stick_y-gamepad1.right_stick_x);
            BL.setPower(gamepad1.left_stick_y-gamepad1.right_stick_x);
            BR.setPower(gamepad1.left_stick_y+gamepad1.right_stick_x);
            FR.setPower(gamepad1.left_stick_y+gamepad1.right_stick_x);
            Spool.setPower(gamepad2.right_stick_y);

            telemetry.addData("Arm Location:", Arm.getCurrentPosition());
            telemetry.update();


            Arm.setPower(gamepad2.left_stick_y);

            if(gamepad2.a)
            {
                Claw.setPosition(.8);
            }
            else if(gamepad2.b)
            {
                Claw.setPosition(0);
            }

            if(gamepad2.x)
            {
                Align.setPosition(.8);
            }
            else if(gamepad2.y)
            {
                Align.setPosition(.2);
            }


            /**if(gamepad1.dpad_up)
             {
             Arm.setPower(-1);
             }
             else if(gamepad1.dpad_down)
             Arm.setPower(1);
             else
             Arm.setPower(0);
             **/
            if(gamepad2.a)
            {
                RS.setPosition(1);
                LS.setPosition(1);
            }
            else if (gamepad2.b)
            {
                RS.setPosition(0);
                LS.setPosition(0);
            }

            if(gamepad1.dpad_left)
            {
                SM.setPower(1);
            }
            else if (gamepad1.dpad_right)
            {
                SM.setPower(-1);
            }
            else
            {
                SM.setPower(0);
            }
            idle();
        }
    }
}
