package org.firstinspires.ftc.teamcode.Tests;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.opencv.features2d.BRISK;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor FLeft = hardwareMap.dcMotor.get("FLeft");
        DcMotor BLeft = hardwareMap.dcMotor.get("BLeft");
        DcMotor FRight = hardwareMap.dcMotor.get("FRight");
        DcMotor BRight = hardwareMap.dcMotor.get("BRight");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        FLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BRight.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        if (isStopRequested( )) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double FLeftPower = (y + x + rx) / denominator;
            double BLeftPower = (y - x + rx) / denominator;
            double FRightPower = (y - x - rx) / denominator;
            double BRightPower = (y + x - rx) / denominator;





            FLeft.setPower(FLeftPower);
            BLeft.setPower(BLeftPower);
            FRight.setPower(FRightPower);
            BRight.setPower(BRightPower);
        }
    }
}

