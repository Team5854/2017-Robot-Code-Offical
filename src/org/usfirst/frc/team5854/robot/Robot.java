package org.usfirst.frc.team5854.robot;

import static org.usfirst.frc.team5854.robot.AutoMethods.visionTurn;
import static org.usfirst.frc.team5854.robot.AutoMethods.moveForward;
import static org.usfirst.frc.team5854.robot.AutoMethods.moveBackward;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnLeftGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnRightGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafeLeft;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafeRight;
import static org.usfirst.frc.team5854.robot.AutoMethods.shootFor;

import org.usfirst.frc.team5854.Utils.EightDrive;
import org.usfirst.frc.team5854.Utils.SpecialFunctions;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	// Setup robot
	public static EightDrive mecanumDrive;
	Encoder backLeftEnc;
	Encoder backRightEnc;
	static VictorSP shootermotor;
	static VictorSP agitatormotor;
	VictorSP climbermotor;
	VictorSP harvestermotor;
	Servo leftgearservo;
	Servo rightgearservo;
	Joystick mainJoystick;
	static Joystick buttonJoystick;
	Joystick altJoystick;
	static ADXRS450_Gyro gyro;

	// Setup variables for autonomous
	String autoSelected;
	SendableChooser<String> chooser;

	// Setup variables for driver
	final String Caleb = "driverCaleb";
	final String Abby = "driverAbby";
	final String Aeron = "driverAeron";
	String driverChooser;
	SendableChooser<String> driver;

	// Setup camera variables
	CameraStreamer cameraserver1; // Gear Cam
	CameraStreamer cameraserver2; // Shoorter Cam
	CameraStreamer cameraserver3; // Climb Cam

	// Setup Timer
	Timer trackTime;

	// Setup driver array (Caleb)(Abby)(Aeron)
	int driverArray[][] = { { 1, 2, 3 }, { 2, 3, 4 }, { 4, 1, 2 } };
	int j ;

	public void robotInit() {
		// Drive Train Creation
		mecanumDrive = new EightDrive(2, 3, 1, 4, 7, 5, 8, 6);

		// PWM motors for non-drive mechanics
		harvestermotor = new VictorSP(0);
		rightgearservo = new Servo(1);
		leftgearservo = new Servo(2);
		shootermotor = new VictorSP(3);
		agitatormotor = new VictorSP(4);
		climbermotor = new VictorSP(5);

		// Configure ports for each joystick.
		buttonJoystick = new Joystick(0); // Buttons Joystick
		mainJoystick = new Joystick(1); // Main Joystick
		altJoystick = new Joystick(2); // Alternate Joystick

		// Setup gyro
		gyro = new ADXRS450_Gyro();

		// Setup SmartDashboard - Driver
		driver = new SendableChooser<String>();
		driver.addDefault("Caleb", "driverCaleb");
		driver.addObject("Abby", "driverAbby");
		driver.addObject("Aeron", "driverAeron");
		SmartDashboard.putData("Driver Choices", driver);

		// Setup SmartDashboard - Autonomous
		chooser = new SendableChooser<String>();
		chooser.addObject("Objective #-1", "Objective-1");
		chooser.addDefault("Objective #0", "Objective0");
		chooser.addObject("Objective #1", "Objective1");
		chooser.addObject("Objective #2", "Objective2");
		chooser.addObject("Objective #3", "Objective3");
		chooser.addObject("Objective #4", "Objective4");
		chooser.addObject("Objective #4 + #5", "Objective45");
		chooser.addObject("Objective #4 + #6", "Objective46");
		SmartDashboard.putData("Auto choices", chooser);

		// Setup Camera #1 - Gear Camera
		cameraserver1 = new CameraStreamer(0, 1181);
		cameraserver1.setResolution(640, 400);
		cameraserver1.setBrightness(1);

		// Setup Camera #2 - Shooter Camera
		cameraserver2 = new CameraStreamer(1, 1185);
		cameraserver2.setResolution(640, 400);

		// Setup Camera #3 - Climber Camera
		cameraserver3 = new CameraStreamer(2, 1190);
		cameraserver3.setResolution(640, 400);

		// Setup Timer
		trackTime = new Timer();
	}

	boolean go = true;

	public void autonomousInit() {
		autoSelected = ((String) chooser.getSelected()); // select auton
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);
		gyro.reset(); // reset gyro
		mecanumDrive.resetEncoders(); // resetEncoders
		gearManager(false); // keep gear servos closed
		go = true; // ensure go equals false
	}

	public void autonomousPeriodic() {
		if (go) {
			switch (autoSelected) {
				case "Objective0":
				moveForward(90.0); // move forward for 90 inches at 1.0 speed
				break;
				///////////////////////////////////////
				case "Objective1":
				moveForward(94.75); // move forward for 94.75 inches at 1.0 speed
				gearManager(true);  // open gear servos
				break;
				///////////////////////////////////////
				case "Objective2":
				moveForward(68.234); // move forward for 68.24 inches at 1.0 speed
				turnRightGyro(30.0); // turn right to 30 deg
				moveForward(66.217); // move forward 66.22 inches at 1.0 speed
				gearManager(true); // open gear servos
				break;
				///////////////////////////////////////
				case "Objective3":
				moveForward(68.234); // move forward for 68.24 inches at 1.0 speed
				turnLeftGyro(30.0); // turn left to 30 deg
				moveForward(66.217); // move forward 66.22 inches at 1.0 speed
				gearManager(true); // open gear servos
				break;
				///////////////////////////////////////
				case "Objective4":
				case "Objective45":
				case "Objective46":
				moveBackward(13.0); // move backward for 13 in at 1.0 speed
				colorPicker_BR(turnRightGyro(23.0), turnLeftGyro(23.0); // turn 23 deg
				shootFor(2.0, true, false); // spin up shooter for 2 seconds
				shootFor(5.0, true, true); // shoot balls for 5 seconds

				if(autoSelected == "Objective45"){
					colorPicker_BR(turnRightGyro(148.0), turnLeftGyro(148.0); // turn 148 deg
					moveForward(100.0); //move forward for 100 inches at 1.0 speed
					gearManager(true); // open gear servos
					moveBackward(8.0); // move backward for 8 inches at 1.0 speed
					gearManager(false); // close gear servos
				} else if(autoSelected == "Objective46"){
					colorPicker_BR(turnLeftGyro(69.25), turnRightGyro(69.25)); // turn 69 deg
					moveBackward(51.5); // move backward for 51 inches at 1.0 speed
					colorPicker_BR(strafeRight(4.0), strafeLeft(4.0)); // strafe for 4 secs
					Timer.delay(2.0); // wait 2 seconds
					colorPicker_BR(strafeLeft(1.0), strafeRight(1.0); // strafeLeft for 1 second
					moveForward(32.555); // move forward for 32.56 inches at 1.0 speed
					colorPicker_BR(turnLeftGyro(35.48), turnRightGyro(35.48)); // turn 35 deg
					shootFor(2.0, true, false); // spin up shooter for 2 seconds
					shootFor(5.0, true, true); // shot balls for 5 seconds
				}
				break;
				//////////////////////////////////
				default:
				mecanumDrive.stop(); // Stop all motor movement
				break;
			}
		}
		go = false;
	}

	double gyroAngle = 0.0;
	boolean joyOne = true;

	public void teleopPeriodic() {
		if(driver.getSelected() == Abby) j = 1;
		else if(driver.getSelected() == Aeron) j = 2;
		else j = 0;

		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);

		// sets the deadband for the drive system.
		mecanumDrive.setDeadband(0.1);

		// sets how fast you can rotate the robot
		mecanumDrive.setTwistMultiplyer(0.3);

		// sets how fast your robot strafes and drives.
		mecanumDrive.setSpeedMultiplyer(0.5);

		//set how the robot drive is manipulated
		if (mainJoystick.getName().startsWith("FRC")) {
			mecanumDrive.mecanumDrive_Cartesian(mainJoystick.getRawAxis(0), mainJoystick.getRawAxis(1), mainJoystick.getRawAxis(2), 0);
		} else if (!altJoystick.getName().equals("")) {
			mecanumDrive.mecanumDrive_Cartesian(altJoystick.getRawAxis(driverArray[j][0]), altJoystick.getRawAxis(driverArray[j][1]), altJoystick.getRawAxis(driverArray[j][2]), 0);
		} else {
			mecanumDrive.mecanumDrive_Cartesian(buttonJoystick.getX(), buttonJoystick.getY(), buttonJoystick.getTwist(), 0);
		}

		gearManager(buttonJoystick.getRawButton(3));

		climberManager(buttonJoystick.getRawButton(11));

		shooterManager(buttonJoystick.getRawButton(2), buttonJoystick.getRawButton(1));

		harvesterManager(buttonJoystick.getRawButton(7), buttonJoystick.getRawButton(8));

	}

	public void gearManager(boolean go) {
		if (go == true) {
			leftgearservo.setAngle(0.0);
			rightgearservo.setAngle(90.0);
		} else {
			leftgearservo.setAngle(90.0);
			rightgearservo.setAngle(0.0);
		}
	}

	public void harvesterManager(boolean go, boolean reverse) {
		if (go) {
			harvestermotor.setSpeed(-1.0);
		} else if (reverse) {
			harvestermotor.setSpeed(1.0);
		} else {
			harvestermotor.setSpeed(0.0);
		}
	}

	public void climberManager(boolean go) {
		if (go) {
			climbermotor.setSpeed(0.7);
		} else {
			climbermotor.setSpeed(0.0);
		}
	}

	public static void shooterManager(boolean go, boolean second) {
		if (go) {
			shootermotor.setSpeed(-1.0);
			if(second && DriverStation.getInstance().isAutonomous()) 
				agitatormotor.setSpeed(0.7);
			else if (second) 
				agitatormotor.setSpeed(SpecialFunctions.map(buttonJoystick.getThrottle(), -1, 1, 1, .25));
			else 
				agitatormotor.setSpeed(0.0);
		} else {
			shootermotor.setSpeed(0.0);
			agitatormotor.setSpeed(0.0);
		}
	}

	boolean once = false;

	public void testPeriodic() {
		if (buttonJoystick.getRawButton(4)) {
			once = false;
			while (buttonJoystick.getRawButton(4)) {
			}
		}
		if (!once) { // BEGIN TEST

		} // END TEST
		once = true;
	}
}
