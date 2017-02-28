package org.usfirst.frc.team5854.robot;

import static org.usfirst.frc.team5854.robot.AutoMethods.moveForward;
import static org.usfirst.frc.team5854.robot.AutoMethods.moveBackward;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnLeftGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnRightGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafeLeft;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafeRight;
import static org.usfirst.frc.team5854.robot.AutoMethods.shootFor;

import static org.usfirst.frc.team5854.Utils.SpecialFunctions.currentColor;
import static org.usfirst.frc.team5854.Utils.SpecialFunctions.map;

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

	// Setup Timer
	Timer trackTime;

	// Setup driver array (Caleb)(Abby)(Aeron)
	int driverArray[][] = { { 2, 3, 0 }, { 0, 1, 2 }, { 0, 1, 2 } };
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
		cameraserver1 = new CameraStreamer(1181);
		cameraserver1.setResolution();
		cameraserver1.setCameraNumber(1);

		// Setup Timer
		trackTime = new Timer();
	}

	boolean autoOnce = true;

	public void autonomousInit() {
		autoSelected = ((String) chooser.getSelected()); // select autonomous
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);
		gyro.reset(); // reset gyroscope
		mecanumDrive.resetEncoders(); // resetEncoders
		gearManager(false); // keep gear servos closed
		autoOnce = true; // ensure go equals false
	}

	public void autonomousPeriodic() {
		if (autoOnce) {
			switch (autoSelected) {
				case "Objective0":
				moveForward(110.0); // move forward for 110 inches at 1.0 speed
				break;
				///////////////////////////////////////
				case "Objective1":
				moveForward(94.75); // move forward for 94.75 inches at 1.0 speed
				gearManager(true);  // open gear servos
				break;
				///////////////////////////////////////
				case "Objective2":
				moveForward(68.234); // move forward for 68.234 inches at 1.0 speed
				turnRightGyro(30.0); // turn right to 30 degree
				moveForward(66.217); // move forward 66.22 inches at 1.0 speed
				gearManager(true); // open gear servos
				break;
				///////////////////////////////////////
				case "Objective3":
				moveForward(68.234); // move forward for 68.234 inches at 1.0 speed
				turnLeftGyro(30.0); // turn left to 30 degree
				moveForward(66.217); // move forward 66.22 inches at 1.0 speed
				gearManager(true); // open gear servos
				break;
				///////////////////////////////////////
				case "Objective4":
				case "Objective45":
				case "Objective46":
				moveBackward(13.0); // move backward for 13 in at 1.0 speed
				if(currentColor() == "Blue") turnRightGyro(23.0); //if blue turn right
				else turnLeftGyro(23.0); //if red turn left
				shootFor(2.0, true, false); // spin up shooter for 2 seconds
				shootFor(5.0, true, true); // shoot balls for 5 seconds

				if(autoSelected == "Objective45"){
					if(currentColor() == "Blue") turnRightGyro(148.0); //if blue turn right
					else turnLeftGyro(148.0); //if red turn left
					moveForward(100.0); //move forward for 100 inches at 1.0 speed
					gearManager(true); // open gear servos
					moveBackward(8.0); // move backward for 8 inches at 1.0 speed
					gearManager(false); // close gear servos
				} else if(autoSelected == "Objective46"){
					if(currentColor() == "Blue") turnRightGyro(69.25); //if blue turn right
					else turnLeftGyro(69.25); //if red turn left
					moveBackward(51.5); // move backward for 51 inches at 1.0 speed
					if(currentColor() == "Blue") strafeRight(4.0); //if blue strafe right
					else strafeLeft(4.0); //if red strafe left
					Timer.delay(2.0); // wait 2 seconds
					if(currentColor() == "Blue") strafeLeft(4.0); //if blue strafe left
					else strafeRight(4.0); //if red strafe right
					moveForward(32.555); // move forward for 32.56 inches at 1.0 speed
					if(currentColor() == "Blue") turnRightGyro(35.48); //if blue turn right
					else turnLeftGyro(35.48); //if red turn left
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
		autoOnce = false;
	}

	double gyroAngle = 0.0;
	boolean joyOne = true;
	boolean fullSpeed = false;
	
	
	public void teleopPeriodic() {
		if(driver.getSelected() == Abby) j = 1;
		else if(driver.getSelected() == Aeron) j = 2;
		else j = 0;

		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);

		// sets the deadband for the drive system.
		mecanumDrive.setDeadband(0.1);

		// sets how fast you can rotate the robot
		if (mainJoystick.getRawButton(2) || altJoystick.getRawButton(2)) {
			while(mainJoystick.getRawButton(2) || altJoystick.getRawButton(2)){}
			fullSpeed = !fullSpeed;
			
		}
		int Climber= 4;
		if (mainJoystick.getRawButton(Climber) || altJoystick.getRawButton(Climber)) {
			while(mainJoystick.getRawButton(Climber) || altJoystick.getRawButton(Climber)){}
			cameraserver1.setCameraNumber(0);
			
		}
		
		int gear= 3;
		if (mainJoystick.getRawButton(gear) || altJoystick.getRawButton(gear)) {
			while(mainJoystick.getRawButton(gear) || altJoystick.getRawButton(gear)){}
			cameraserver1.setCameraNumber(1);
			
		}
		int shooter = 1;
		if (mainJoystick.getRawButton(shooter) || altJoystick.getRawButton(shooter)) {
			while(mainJoystick.getRawButton(shooter) || altJoystick.getRawButton(shooter)){}
			cameraserver1.setCameraNumber(2);
			
		}
		
		// sets how fast your robot strafes and drives.
		if(fullSpeed) {
			mecanumDrive.setSpeedMultiplyer(1.0);
			mecanumDrive.setTwistMultiplyer(0.8);
		} else {
			mecanumDrive.setSpeedMultiplyer(0.5);
			mecanumDrive.setTwistMultiplyer(0.3);
		}
		
		//set how the robot drive is manipulated
		if (mainJoystick.getName().startsWith("FRC")) {
			mecanumDrive.mecanumDrive_Cartesian(mainJoystick.getRawAxis(0), mainJoystick.getRawAxis(1), mainJoystick.getRawAxis(2), 0);
		} else if (!altJoystick.getName().equals("")) {
			mecanumDrive.mecanumDrive_Cartesian(altJoystick.getRawAxis(driverArray[j][0]), altJoystick.getRawAxis(driverArray[j][1]), altJoystick.getRawAxis(driverArray[j][2]), 0);
		} else {
			mecanumDrive.mecanumDrive_Cartesian(buttonJoystick.getX(), buttonJoystick.getY(), buttonJoystick.getTwist(), 0);
		}

		// set how to operate gear
		gearManager(buttonJoystick.getRawButton(3));

		// set how to activate climber
		climberManager(buttonJoystick.getRawButton(11));

		// set how to operate agitator and shooter
		shooterManager(buttonJoystick.getRawButton(2), buttonJoystick.getRawButton(1));
		
		//set how to operate
		harvesterManager(buttonJoystick.getRawButton(7), buttonJoystick.getRawButton(8));
	}

	public void gearManager(boolean go) {
		if (go) {
			leftgearservo.setAngle(0.0);
			rightgearservo.setAngle(90.0);
		} else {
			leftgearservo.setAngle(90.0);
			rightgearservo.setAngle(0.0);
		}
	}

	public void harvesterManager(boolean go, boolean reverse) {
		if (reverse) {
			harvestermotor.setSpeed(1.0);
		} else if (go) {
			harvestermotor.setSpeed(-1.0);
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
		if(second && !go) {
			agitatormotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, -1, -.25));

		} else {
		if (go) {
			shootermotor.setSpeed(-1.0);
			if(second && DriverStation.getInstance().isAutonomous()) 
				agitatormotor.setSpeed(0.7);
			else if (second) 
				agitatormotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, 1, .25));
			else 
				agitatormotor.setSpeed(0.0);
		} else {
			shootermotor.setSpeed(0.0);
			agitatormotor.setSpeed(0.0);
		}
		}
	}

	boolean once = false;

	public void testInit() {
		mecanumDrive.resetEncoders();
	}
	public void testPeriodic() {
		if (buttonJoystick.getRawButton(4)) {
			while (buttonJoystick.getRawButton(4)) {
			}
			mecanumDrive.resetEncoders();

			once = false;
		}
		if (!once) { // BEGIN TEST
			//mecanumDrive.resetEncoders();
			//moveForward(10);
			strafeLeft(5);

			
		} // END TEST
		once = true;
	}
}
