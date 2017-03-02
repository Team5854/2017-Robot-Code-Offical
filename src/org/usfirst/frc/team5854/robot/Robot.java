package org.usfirst.frc.team5854.robot;

import static org.usfirst.frc.team5854.robot.AutoMethods.moveForward;
import static org.usfirst.frc.team5854.robot.AutoMethods.moveBackward;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnLeftGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnRightGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafeLeft;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafeRight;
import static org.usfirst.frc.team5854.robot.AutoMethods.shootFor;
import static org.usfirst.frc.team5854.robot.AutoMethods.visionTurn;

import static org.usfirst.frc.team5854.Utils.SpecialFunctions.currentColor;
import static org.usfirst.frc.team5854.Utils.SpecialFunctions.map;

import org.usfirst.frc.team5854.Utils.EightDrive;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
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

	// Setup variables for drivers and operaters
	final String Default = "driverDefault";
	final String Caleb = "driverCaleb";
	final String Abby = "driverAbby";
	final String Aeron = "driverAeron";
	SendableChooser<String> operator;
	SendableChooser<String> driver;

	// Setup camera variables
	CameraStreamer cameraServer;

	// Setup driver array (Default)(Caleb)(Abby)(Aeron)
	int pDriverArray[][] = { { 0, 1, 2, 4, 3, 1, 2 }, { 2, 3, 0, 4, 3, 1, 2 }, { 0, 1, 2, 3, 4, 1, 2 }, { 0, 1, 2, 3, 2, 4, 11 } };

	// setup driver array (Default)(Abby)(Aeron)(Jacob)(Josh)
	int sDriverArray[][] = { { 7, 8, 11, 3, 6, 4, 5 } };

	// store the value of the drivers
	int d, o;

	// Setup debug boolean
	boolean debug = false;
	
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
		buttonJoystick = new Joystick(0); // Buttons joystick
		mainJoystick = new Joystick(1); // Main joystick
		altJoystick = new Joystick(2); // Alternate joystick

		// Setup gyro
		gyro = new ADXRS450_Gyro();

		// Setup SmartDashboard - Driver
		driver = new SendableChooser<String>();
		driver.addDefault("Caleb", Caleb);
		driver.addObject("Abby", Abby);
		driver.addObject("Aeron", Aeron);
		driver.addObject("Default", Default);
		SmartDashboard.putData("Driver", driver);

		// Setup SmartDashboard - Operator
		operator = new SendableChooser<String>();
		operator.addDefault("Default", Default);
		SmartDashboard.putData("Operator", operator);

		// Setup SmartDashboard - Autonomous
		chooser = new SendableChooser<String>();
		chooser.addObject("Dont Move", "Objective");
		chooser.addDefault("Objective #0", "Objective0");
		chooser.addObject("Objective #1", "Objective1");
		chooser.addObject("Objective #2", "Objective2");
		chooser.addObject("Objective #3", "Objective3");
		chooser.addObject("Objective #4", "Objective4");
		chooser.addObject("Objective #4 + #5", "Objective45");
		chooser.addObject("Objective #4 + #6", "Objective46");
		SmartDashboard.putData("Autonomous choices", chooser);

		// Setup Camera - Default to Gear Camera
		cameraServer = new CameraStreamer(1181);
		cameraServer.setResolution();
		cameraServer.setCameraNumber(0);
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
		autoSelected = ((String) chooser.getSelected()); // select autonomous
		if (autoOnce) {
			switch (autoSelected) {
			case "Objective0":
				moveForward(110.0); // move forward for 110 inches at 1.0 speed
				break;
			///////////////////////////////////////
			case "Objective1":
				moveForward(94.75); // move forward for 94.75 inches at 1.0 speed
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
				if (currentColor() == "Blue") turnRightGyro(23.0); // if blue turn right
				else turnLeftGyro(23.0); // if red turn left
				shootFor(2.0, true, false); // spin up shooter for 2 seconds
				shootFor(5.0, true, true); // shoot balls for 5 seconds
				shooterManager(false, false); // disable shooter
				if (autoSelected == "Objective45") {
					if (currentColor() == "Blue") turnRightGyro(148.0); // if blue turn right
					else turnLeftGyro(148.0); // if red turn left
					moveForward(100.0); // move forward for 100 inches at 1.0 speed
					gearManager(true); // open gear servos
					moveBackward(8.0); // move backward for 8 inches at 1.0  speed
					gearManager(false); // close gear servos
				} else if (autoSelected == "Objective46") {
					if (currentColor() == "Blue") turnRightGyro(69.25); // if blue turn right
					else turnLeftGyro(69.25); // if red turn left
					moveBackward(51.5); // move backward for 51 inches at 1.0 speed
					if (currentColor() == "Blue") strafeRight(4.0); // if blue strafe right
					else strafeLeft(4.0); // if red strafe left
					Timer.delay(2.0); // wait 2 seconds
					if (currentColor() == "Blue") strafeLeft(4.0); // if blue strafe left
					else strafeRight(4.0); // if red strafe right
					moveForward(32.555); // move forward for 32.56 inches at 1.0 speed
					if (currentColor() == "Blue") turnRightGyro(35.48); // if blue turn right
					else turnLeftGyro(35.48); // if red turn left
					shootFor(2.0, true, false); // spin up shooter for 2 seconds
					shootFor(5.0, true, true); // shot balls for 5 seconds
					shooterManager(false, false);
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
	
	boolean fullSpeed = false;

	public void teleopInit() {
		// stop all motor movement
		mecanumDrive.stop();

		// initiate mecanum drive mode
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);

		// sets the deadband for the drive system.
		mecanumDrive.setDeadband(0.1);

		// ensure that full speed starts as false
		fullSpeed = false;

		// sets how fast the robot strafes and drives
		mecanumDrive.setSpeedMultiplyer(0.5);

		// set how fast the robot rotates
		mecanumDrive.setTwistMultiplyer(0.3);
	}
	
	public void teleopPeriodic() {
		// set primary - driver
		if (driver.getSelected() == Caleb)
			d = 1;
		else if (driver.getSelected() == Abby)
			d = 2;
		else if (driver.getSelected() == Aeron)
			d = 3;
		else
			d = 0;
	
		// set secondary - operator
		if (operator.getSelected() == Default) 
			o = 0;
		else
			o = 0;
	
		// sets how fast you can rotate the robot
		if (mainJoystick.getRawButton(pDriverArray[d][6]) || altJoystick.getRawButton(pDriverArray[d][6])) {
			while (mainJoystick.getRawButton(pDriverArray[d][6]) || altJoystick.getRawButton(pDriverArray[d][6])) {
			}
			fullSpeed = !fullSpeed;

			if (fullSpeed) {
				mecanumDrive.setSpeedMultiplyer(1.0);
				mecanumDrive.setTwistMultiplyer(0.8);
			} else {
				mecanumDrive.setSpeedMultiplyer(0.5);
				mecanumDrive.setTwistMultiplyer(0.3);
			}
		}

		// manage the toggle between cameras
		if (mainJoystick.getRawButton(pDriverArray[d][4]) || altJoystick.getRawButton(pDriverArray[d][4]) || buttonJoystick.getRawButton(sDriverArray[o][5])) {
			while (mainJoystick.getRawButton(pDriverArray[d][4]) || altJoystick.getRawButton(pDriverArray[d][4]) || buttonJoystick.getRawButton(sDriverArray[o][5])) {
			}
			cameraServer.setCameraNumber(1); // change camera to gear
		} else if (mainJoystick.getRawButton(pDriverArray[d][3]) || altJoystick.getRawButton(pDriverArray[d][3]) || buttonJoystick.getRawButton(sDriverArray[o][4])) {
			while (mainJoystick.getRawButton(pDriverArray[d][3]) || altJoystick.getRawButton(pDriverArray[d][3]) || buttonJoystick.getRawButton(sDriverArray[o][4])) {
			}
			cameraServer.setCameraNumber(0); // change camera to climber
		} else if (mainJoystick.getRawButton(pDriverArray[d][5]) || altJoystick.getRawButton(pDriverArray[d][5]) || buttonJoystick.getRawButton(sDriverArray[o][6])) {
			while (mainJoystick.getRawButton(pDriverArray[d][5]) || altJoystick.getRawButton(pDriverArray[d][5]) || buttonJoystick.getRawButton(sDriverArray[o][6])) {
			}
			cameraServer.setCameraNumber(2); // change camera to shooter
		}

		// set how the robot drive is manipulated
		if (mainJoystick.getName().startsWith("FR")) {
			mecanumDrive.mecanumDrive_Cartesian(mainJoystick.getRawAxis(2), mainJoystick.getRawAxis(3), mainJoystick.getRawAxis(0), 0);
		} else if (!altJoystick.getName().equals("")) {
			mecanumDrive.mecanumDrive_Cartesian(altJoystick.getRawAxis(pDriverArray[d][0]), altJoystick.getRawAxis(pDriverArray[d][1]), altJoystick.getRawAxis(pDriverArray[d][2]), 0);
		} else {
			mecanumDrive.mecanumDrive_Cartesian(buttonJoystick.getX(), buttonJoystick.getY(), buttonJoystick.getTwist(), 0);
		}

		// set how to operate gear
		gearManager(buttonJoystick.getRawButton(sDriverArray[o][3]));

		// set how to activate climber
		if (Timer.getMatchTime() > 60.0) {
			climberManager(buttonJoystick.getRawButton(sDriverArray[o][2]));
		} else {
			climberManager(false);
		}

		// set how to operate agitator and shooter
		shooterManager(buttonJoystick.getRawButton(2), buttonJoystick.getRawButton(1));

		// set how to operate
		harvesterManager(buttonJoystick.getRawButton(sDriverArray[o][0]), buttonJoystick.getRawButton(sDriverArray[o][1]));
	}
	
	public void gearManager(boolean open) {
		if (open) {
			leftgearservo.setAngle(0.0);
			rightgearservo.setAngle(90.0);
		} else {
			leftgearservo.setAngle(90.0);
			rightgearservo.setAngle(0.0);
		}
	}

	public void harvesterManager(boolean intake, boolean outtake) {
		if (outtake) {
			harvestermotor.setSpeed(1.0);
		} else if (intake) {
			harvestermotor.setSpeed(-1.0);
		} else {
			harvestermotor.setSpeed(0.0);
		}
	}

	public void climberManager(boolean climb) {
		if (climb) {
			climbermotor.setSpeed(0.7);
		} else {
			climbermotor.setSpeed(0.0);
		}
	}

	public static void shooterManager(boolean shoot, boolean agitate) {
		double shooterSpeed = -1.0;
		double agitatorSpeed = 0.7;
		boolean isAuton = DriverStation.getInstance().isAutonomous();
		if (!shoot && agitate) { // reverse agitator
			shootermotor.setSpeed(0.0);
			if (isAuton)
				agitatormotor.setSpeed(-agitatorSpeed);
			else
				agitatormotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, -1, -.25));
		} else if (shoot && !agitate) { // spin up shooter
			shootermotor.setSpeed(shooterSpeed);
			agitatormotor.setSpeed(0.0);
		} else if (shoot && agitate) { // shoot and agitate
			shootermotor.setSpeed(shooterSpeed);
			if (isAuton)
				agitatormotor.setSpeed(agitatorSpeed);
			else
				agitatormotor.setSpeed(map(buttonJoystick.getThrottle(), -1, 1, 1, .25));
		} else { // stop both shooter and agitator
			shootermotor.setSpeed(0.0);
			agitatormotor.setSpeed(0.0);
		}
	}

	boolean once = false;
	
	public void testInit() {
		mecanumDrive.resetEncoders(); // reset encoders
		gyro.reset(); // reset gyro
		gearManager(false); // keep gear servos closed
		climberManager(false); // keep climber off
		shooterManager(false, false); // dont shoot
		harvesterManager(false, false); //dont harvest
		once = false;
	}
	
	public void testPeriodic() {
		if (buttonJoystick.getRawButton(4)) {
			while (buttonJoystick.getRawButton(4)) {
				mecanumDrive.resetEncoders();
				gyro.reset();
			}
			once = false;
		}
		if (!once) { // BEGIN TEST

			// mecanumDrive.resetEncoders();
			// moveForward(10);
			// strafeLeft(5);
			moveForward(10.0);

		} // END TEST
		mecanumDrive.stop();
		once = true;
	}
}