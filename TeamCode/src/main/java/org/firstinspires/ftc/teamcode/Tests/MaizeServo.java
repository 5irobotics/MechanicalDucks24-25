package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "MaizeServo")
public class MaizeServo extends LinearOpMode {
    private CRServo BLeft;
    private CRServo BRight;
    private CRServo FLeft;
    private CRServo FRight;



    @Override
    public void runOpMode() {
        BLeft = hardwareMap.crservo.get("BLeft");
        BRight = hardwareMap.crservo.get("BRight");
        FLeft  = hardwareMap.crservo.get("FLeft");
        FRight = hardwareMap.crservo.get("FRight");




        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                drive();
            }
        }
    }

    public void drive(){
        if (gamepad1.left_stick_y != 0.0) {
            BLeft.setPower(0.25*gamepad1.left_stick_y);
            BRight.setPower(-0.25*gamepad1.left_stick_y);
            FRight.setPower(-0.25*gamepad1.left_stick_y);
            FLeft.setPower(0.25*gamepad1.left_stick_y);
        }
        // Strafing
        else if(gamepad1.left_stick_x != 0.0) {
            BLeft.setPower(0.25*gamepad1.left_stick_x);
            BRight.setPower(0.25*gamepad1.left_stick_x);
            FRight.setPower(-0.25*gamepad1.left_stick_x);
            FLeft.setPower(-0.25*gamepad1.left_stick_x);

        }
        // Rotation
        else if (gamepad1.right_stick_x != 0.0) {
            BLeft.setPower(-0.25 * gamepad1.right_stick_x);
            BRight.setPower(-0.25 * gamepad1.right_stick_x);
            FRight.setPower(-0.25 * gamepad1.right_stick_x);
            FLeft.setPower(-0.25 * gamepad1.right_stick_x);
        }
        else {
            BLeft.setPower(0);
            BRight.setPower(0);
            FRight.setPower(0);
            FLeft.setPower(0);
        }


    }
}
