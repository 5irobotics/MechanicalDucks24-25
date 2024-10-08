package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;


@TeleOp(name = "IntakeDriveCode")
public class IntakeDriveCode extends LinearOpMode {

    private ColorRangeSensor IColor;
    private Servo PixelTray;
    private Servo LeftClaw;
    private Servo RightClaw;
    private Servo shoot;
    private Servo Wrist;
    private DcMotor FrontRollers;
    private DcMotor BackRollers;
    private DcMotor BLeft;
    private DcMotor BRight;
    private DcMotor FLeft;
    private DcMotor FRight;
    private DcMotor Arm;
    private DcMotor Linear;
    private ElapsedTime runtime = new ElapsedTime();
    private int x = 1;
    private int pixelCount = 0;
    private double backPower = 0.75;
    private double frontPower = 0.7;
    private TfodProcessor tfod;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {

        initializeHardware();

        BLeft.setDirection(DcMotor.Direction.REVERSE);
        BRight.setDirection(DcMotor.Direction.REVERSE);

        //initTfod();

        waitForStart();

        runtime.reset();

        double armPower;
        double linearPower;

        while (opModeIsActive()) {

            moveRobot();

            armPower = gamepad2.left_stick_y;
            linearPower = -(gamepad2.right_stick_y);

            controlArmAndLinear(armPower, linearPower);

            controlClawsAndShoot();

            handlePixelTray();

            controlWrist();

            waitForStart();

            telemetryUpdate();
        }
    }

    private void initializeHardware() {
        IColor = hardwareMap.get(ColorRangeSensor.class, "IColor");
        PixelTray = hardwareMap.get(Servo.class, "PixelTray");
        shoot = hardwareMap.get(Servo.class, "shoot");
        LeftClaw = hardwareMap.get(Servo.class, "LeftClaw");
        RightClaw = hardwareMap.get(Servo.class, "RightClaw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        FrontRollers = hardwareMap.dcMotor.get("FrontRollers");
        BackRollers = hardwareMap.dcMotor.get("BackRollers");
        BLeft = hardwareMap.dcMotor.get("BLeft");
        BRight = hardwareMap.dcMotor.get("BRight");
        FLeft = hardwareMap.dcMotor.get("FLeft");
        FRight = hardwareMap.dcMotor.get("FRight");
        Arm = hardwareMap.get(DcMotor.class, "Arm");
        Linear = hardwareMap.get(DcMotor.class, "Linear");

        PixelTray.setPosition(0);
    }


    private void controlArmAndLinear(double armPower, double linearPower) {
        Arm.setPower(armPower);
        Linear.setPower(linearPower);
    }

    private void controlClawsAndShoot() {
        if (gamepad2.dpad_right) {
            // close
            LeftClaw.setPosition(0.4);
            RightClaw.setPosition(0.9);
        } else if (gamepad2.dpad_left) {
            // open
            LeftClaw.setPosition(1);
            RightClaw.setPosition(0.3);
        }

        if (gamepad1.y) {
            shoot.setPosition(0);
        } else if (gamepad1.b) {
            shoot.setPosition(1);
        } else if (gamepad1.x) {
            shoot.setPosition(0.5);
        }
    }

    private void handlePixelTray() {
        if (gamepad1.dpad_up) {
            //start intake
            BackRollers.setPower(backPower);
            FrontRollers.setPower(frontPower);

        } else if (gamepad1.dpad_down) {
            //stop rollers manually
            BackRollers.setPower(0);
            FrontRollers.setPower(0);

        } else if (gamepad1.dpad_left) {
            //reverse rollers manually
            BackRollers.setPower(-backPower);
            FrontRollers.setPower(-frontPower);
        }

        if (IColor.getDistance(DistanceUnit.CM) < 4) {
            if (x % 2 == 0) {
                PixelTray.setPosition(1);
            } else {
                PixelTray.setPosition(0);
            }
            x += 1;
            pixelCount += 1;
            runtime.reset();

            if (pixelCount == 3) {
                BackRollers.setPower(-0.5);
                FrontRollers.setPower(-0.5);
                pixelCount = 0;

            }
            sleep(1500);
        }
    }

    private void telemetryUpdate() {
        telemetry.addData("Servo Position: ", PixelTray.getPosition());
        telemetry.addData("Pixel Distance: ", IColor.getDistance(DistanceUnit.CM));
        telemetry.addData("Pixel Count: ", pixelCount);
        telemetry.addData("Elapsed Time: ", runtime.seconds());
        telemetry.update();
    }

    private void driveRobot() {
        double DrivePower = 0.75;
        if (gamepad1.left_stick_y != 0) {
            BRight.setPower(gamepad1.left_stick_y * DrivePower);
            BLeft.setPower(gamepad1.left_stick_y * DrivePower);
            FRight.setPower(gamepad1.left_stick_y * DrivePower);
            FLeft.setPower(gamepad1.left_stick_y * DrivePower);
        } else if (gamepad1.left_stick_x != 0) {
            BRight.setPower(gamepad1.left_stick_x * DrivePower);
            BLeft.setPower(gamepad1.left_stick_x * DrivePower);
            FRight.setPower(-(gamepad1.left_stick_x * DrivePower));
            FLeft.setPower(-(gamepad1.left_stick_x * DrivePower));
        } else if (gamepad1.right_stick_x != 0) {
            BRight.setPower(gamepad1.right_stick_x * DrivePower);
            BLeft.setPower(-(gamepad1.right_stick_x * DrivePower));
            FRight.setPower(gamepad1.right_stick_x * DrivePower);
            FLeft.setPower(-(gamepad1.right_stick_x * DrivePower));
        } else {
            BRight.setPower(0);
            BLeft.setPower(0);
            FRight.setPower(0);
            FLeft.setPower(0);
        }
    }

    public void moveRobot() {
        double vertical;
        double horizontal;
        double pivot;

        vertical = 0.5 * gamepad1.left_stick_y;
        horizontal = -0.5 * gamepad1.left_stick_x;
        pivot = -0.5 * gamepad1.right_stick_x;

        FRight.setPower((-pivot + (vertical + horizontal)));
        BRight.setPower(-pivot + (vertical - horizontal));
        FLeft.setPower((pivot + (vertical + horizontal)));
        BLeft.setPower((pivot + (vertical - horizontal)));
    }

    private void controlWrist() {
        if (gamepad2.x) {
            Wrist.setPosition(1);
        } else if (gamepad2.b) {
            Wrist.setPosition(0.7);
        }
    }

    private void initTfod() {

        // Create the TensorFlow processor the easy way.
        tfod = TfodProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.


    }   // end method initTfod()


}
