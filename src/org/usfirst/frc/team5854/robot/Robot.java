package org.usfirst.frc.team5854.robot;

//AutoMethods
import static org.usfirst.frc.team5854.robot.AutoMethods.moveForwardWithMap;
import static org.usfirst.frc.team5854.robot.AutoMethods.strafe;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnGyro;
import static org.usfirst.frc.team5854.robot.AutoMethods.visionTurn;
import static org.usfirst.frc.team5854.robot.AutoMethods.turnGyroRight;

import org.usfirst.frc.team5854.Utils.EightDrive;
import org.usfirst.frc.team5854.Utils.SpecialFunctions;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
	//Setup robot 
	public static EightDrive mecanumDrive;
	Encoder backLeftEnc;
	VictorSP shootermotor;
	VictorSP agitatormotor;
	Servo leftgearservo;
	Servo rightgearservo;
	Joystick mainJoystick;
	Joystick buttonJoystick;
	Joystick secondJoystick;
	static ADXRS450_Gyro gyro;
	VictorSP climbermotor;
	VictorSP harvestermotor;
	
	//Test variables
	final String driveForward = "driveforward";
	final String spin = "spin";
	final String visionTrackingTest = "Vision Test";
	
	// Setup variables for autonomous
	final String objective1 = "Objective1";
	final String objective2 = "Objective2";
	final String objective3 = "Objective3";
	final String objective4 = "Objective4";
	final String objective45 = "Objective45";
	final String objective46 = "Objective46";
	String autoSelected;
	SendableChooser<String> chooser;
	
	//Setup camera variables
	CameraStreamer cameraserver1;
	CameraStreamer cameraserver2;
	CameraStreamer cameraserver3;

	public void robotInit() {
		//driving creation. hover to find out more.
		mecanumDrive = new EightDrive(2, 3, 1, 4, 7, 5, 8, 6);
		
		//PWM motors for non-drive mechanics.
		harvestermotor = new VictorSP(0);
		rightgearservo = new Servo(1);
		leftgearservo = new Servo(2);
		shootermotor = new VictorSP(3);
		agitatormotor = new VictorSP(4);
		climbermotor = new VictorSP(5);

		
		//ports for each joystick.
		mainJoystick = new Joystick(1); //main joy is the driving joystick
		buttonJoystick = new Joystick(0); //button joy is the black joystick for controlling non-drive mechanics
		secondJoystick = new Joystick(2);

		//setup gyro
		gyro = new ADXRS450_Gyro();

		//smartdashboard for which autonomous method we use.
		chooser = new SendableChooser<String>();
		chooser.addDefault("Drive forward", "driveforward");
		chooser.addObject("spin", "spin");
		chooser.addObject("Vision Test", "Vision Test");
		chooser.addObject("Objective #1", "Objective1");
		chooser.addObject("Objective #2", "Objective2");
		chooser.addDefault("Objective #3", "Objective3");
		chooser.addDefault("Objective #4", "Objective4");
		chooser.addDefault("Objective #4 and #5", "Objective45");
		chooser.addDefault("Objective #6", "Objective6");
		chooser.addDefault("Objective #6 and #7", "Objective67");
		SmartDashboard.putData("Auto choices", chooser);

		cameraserver1 = new CameraStreamer(0, 1181);
		cameraserver1.setResolution(440, 200);
		cameraserver1.setBrightness(1);
		
		cameraserver2 = new CameraStreamer(1, 1185);
		cameraserver2.setResolution(440, 200);
		
		cameraserver3 = new CameraStreamer(2, 1190);
		cameraserver3.setResolution(440, 200);
		
		System.out.println("Done Initializing.");
		
	}

	public void autonomousInit() {
		autoSelected = ((String) chooser.getSelected());
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);
		gyro.reset();
		go = true;
	}

	boolean go = true;

	public void autonomousPeriodic() {
		Timer myTime = new Timer();
		
		if (buttonJoystick.getRawButton(7)) 
		{
			go = true;
			gyro.reset();
		}
		
		if (go) {
			switch (autoSelected) {
			case "Vision Test":
				visionTurn();
				go = false;
				break;
			case "spin":
				turnGyro('R', 90.0, true);
				go = false;
				break;
				////////////////////////
			case "driveforward":
				moveForwardWithMap(30.0);
				go = false;
				break;
			case "Objective1":
				moveForwardWithMap(94.75);
				gearManager(true);
				go = false;
				break;
			case "Objective2":
				moveForwardWithMap(68.234);
				turnGyro('R', 30.0, true);
				moveForwardWithMap(66.217);
				gearManager(true);
				go = false;
				break;
			case "Objective3":
				moveForwardWithMap(68.234);
				turnGyro('L', 30.0, true);
				moveForwardWithMap(66.217);
				gearManager(true);
				go = false;
				break;
			case "Objective46":
			case "Objective45":
			case "Objective4":
				moveForwardWithMap(-13.0);
				
				if (DriverStation.Alliance.Blue != null) {
					turnGyro('R', 23.0, true);
				} else {
					turnGyro('L', 23.0, true);
				}
				
				for (int i = 0; i < 3000; i++) {
					shooterManager(true, false, false);
				}
				for (int i = 0; i < 7000; i++) {
					shooterManager(true, true, false);
				}
				
				if(autoSelected == objective45){
					if (DriverStation.Alliance.Blue != null) {
						turnGyro('R', 148.0, true);
					} else {
						turnGyro('L', 148.0, true);
					}
					moveForwardWithMap(100.0);
					gearManager(true);
					moveForwardWithMap(-8.0);
					go = false;
				}
				else if (autoSelected == objective46) {
					if(DriverStation.Alliance.Blue != null){
						turnGyro('L', 69.25, true);
					}
					else{
						turnGyro('R', 69.25, true);
					}
					moveForwardWithMap(-51.5);
					if(DriverStation.Alliance.Blue != null){
						strafe('R', 3.0);
						for(int i = 0; i <= (1 * 1000); i++){}
						strafe('L', 0.5);
					}
					else{
						strafe('L', 3.0);
						myTime.delay(1.0);
						strafe('R', 0.5);
					}
					moveForwardWithMap(32.555);
					 
					if(DriverStation.Alliance.Blue != null){
						turnGyro('L', 35.68, true);
					}else { 
						turnGyro('R', 35.68, true);
					}
					
				}
				else {
					go = false;
				}
				
				go = false;
				break;
		
			}
		}
	}

	public int secToTicks(double secs) {
		return (int) (secs * 500.0);
	}

	double gyroAngle = 0.0;
	boolean joyOne = true;
	
	public void teleopPeriodic() {
		mecanumDrive.setCANTalonDriveMode(CANTalon.TalonControlMode.PercentVbus);

		//sets the deadband for the drive system.
		mecanumDrive.setDeadband(0.1);

		//sets how fast you can rotate the robot
		mecanumDrive.setTwistMultiplyer(0.3);

		//sets how fast your robot strafe and drives.
		mecanumDrive.setSpeedMultiplyer(.5);
		
		//Selects which controller we are using for driving.
	
		if (!(secondJoystick.getName().startsWith("Logitech")) && !(mainJoystick.getName().startsWith("FRC"))) {
			mecanumDrive.mecanumDrive_Cartesian(buttonJoystick.getX(), buttonJoystick.getY(), buttonJoystick.getTwist(), 0.0);
		} 
		else if (mainJoystick.getName().startsWith("FRC")) {
			mecanumDrive.mecanumDrive_Cartesian(mainJoystick.getRawAxis(1), mainJoystick.getRawAxis(2), mainJoystick.getRawAxis(3), 0);
		}
		else {
			if (joyOne) {
				if (secondJoystick.getRawButton(2)) {
					joyOne = false;
				}
				mecanumDrive.mecanumDrive_Cartesian(secondJoystick.getRawAxis(0), secondJoystick.getRawAxis(1), secondJoystick.getRawAxis(2), 0);
			} else {
				if (secondJoystick.getRawButton(2)) {
					joyOne = true;
				}
				mecanumDrive.mecanumDrive_Cartesian(secondJoystick.getRawAxis(2), secondJoystick.getRawAxis(3), secondJoystick.getRawAxis(0), 0);
			}
		}
		
		//mecanumDrive.mecanumDrive_Cartesian(buttonJoystick.getX(), buttonJoystick.getY(), buttonJoystick.getTwist(), 0.0);

		gearManager(buttonJoystick.getRawButton(3));
		
		climberManager(buttonJoystick.getRawButton(11));
		
		shooterManager(buttonJoystick.getRawButton(2), buttonJoystick.getRawButton(1), buttonJoystick.getRawButton(4));
		
		harvesterManager(buttonJoystick.getRawButton(7), buttonJoystick.getRawButton(8));

		gyroAngle = gyro.getAngle();
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

	public void shooterManager(boolean go, boolean second, boolean third) {
		if (go) {
			shootermotor.setSpeed(-1.0);
			if (second) {
				if (third) {
					agitatormotor.setSpeed(SpecialFunctions.map(buttonJoystick.getThrottle(), -1, 1, -1, -0.25));
				} else {
					agitatormotor.setSpeed(SpecialFunctions.map(buttonJoystick.getThrottle(), -1, 1, 1, 0.25));

				}
			} else {
				agitatormotor.setSpeed(0.0);
			}
		} else {
			shootermotor.setSpeed(0.0);
			agitatormotor.setSpeed(0.0);
		}
	}


	double r = 0.0;
	double prevR = 0.0;
	double l = 0.0;
	double prevL = 0.0;
	boolean reset = true;
	boolean once = true;
	int i = 0;

	public void testPeriodic() {
		gearManager(false);
		if (buttonJoystick.getRawButton(4)) {
			once = true;
		}
		if (once) {
			mecanumDrive.resetEncoders();
			moveForwardWithMap(90);
			//turnGyroRight(360.0);
			once = false;
		}
	}
}
