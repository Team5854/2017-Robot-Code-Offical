package org.usfirst.frc.team5854.robot;

import static org.usfirst.frc.team5854.robot.Robot.shooterManager;
import org.usfirst.frc.team5854.Utils.EightDrive;
import org.usfirst.frc.team5854.Utils.SpecialFunctions;
import static org.usfirst.frc.team5854.Utils.SpecialFunctions.map;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class AutoMethods {

	static int repeatTime = 5;
	public static boolean currentState = true;
	
	static EightDrive mecanumDrive = Robot.mecanumDrive;
	static ADXRS450_Gyro gyro = Robot.gyro;
	static double gyroAngle = 0;

	public static void visionTurn() {
		double Angle = RoboSockets.getValue();
		char directionChar = 'r';
		if (Angle < 0.0) {
			directionChar = 'L';
		} else {
			directionChar = 'R';
		}
		System.out.println(Angle);
		Angle = Math.abs(Angle);

		if (directionChar == 'L') {
			turnRightGyro(Angle);
		} else {
			turnLeftGyro(Angle);
		}
	}

	static double angleError = 2.5;

	public static void turnLeftGyro(double angle) {
		double otherNumber = 0.0;
		double speed = 0.0;

		gyro.reset();

		while ((angle - angleError > otherNumber) && currentState) {
			otherNumber = Math.abs(gyro.getAngle());
			System.out.println("Gyro : " + otherNumber);
			speed = map(otherNumber, 0, angle, .5, .2);
			mecanumDrive.mecanumDrive_Polar(0, 0, speed);
			currentState = DriverStation.getInstance().isAutonomous();
		}

		for (int i = 0; i < repeatTime; i++) {
			mecanumDrive.moveLeftSide(0);
			mecanumDrive.moveRightSide(0);
			mecanumDrive.resetEncoders();
		}
	}

	public static void turnRightGyro(double angle) {
		double otherNumber = 0.0;
		double speed = .5;

		gyro.reset();

		while ((angle - angleError > otherNumber) && currentState) {
			otherNumber = Math.abs(gyro.getAngle());
			System.out.println("Gyro : " + otherNumber);
			speed = map(otherNumber, 0, angle, .5, .2);
			mecanumDrive.mecanumDrive_Polar(0, 0, -speed);
			currentState = DriverStation.getInstance().isAutonomous();
		}

		for (int i = 0; i < repeatTime; i++) {
			mecanumDrive.moveLeftSide(0);
			mecanumDrive.moveRightSide(0);
			mecanumDrive.resetEncoders();
		}
	}

	static int ticksPerRotation = 3100;
	
	public static void moveForward(double inches) {
		double converstion = (ticksPerRotation / (6 * Math.PI));
		double desRotation = converstion * Math.abs(inches);
		System.out.println("DesRotations " + desRotation);

		double speed = 0;
		double highSpeed = 0;
		double slowSpeed = 0;
		double rotation = 0.0;
		
		mecanumDrive.resetEncoders();

		while ((rotation < desRotation) && currentState) {
			if (rotation < (desRotation / 2)) {
				speed = SpecialFunctions.map(mecanumDrive.getEncValueRight(), 0, desRotation / 2, .3, .5);
			} else {
				speed = SpecialFunctions.map(mecanumDrive.getEncValueRight(), desRotation / 2, desRotation, .5, .1);
			}
			
			highSpeed = speed;
			slowSpeed = speed - 0.1;

			rotation = Math.abs(mecanumDrive.getEncValueLeft());

			System.out.println("Rotations " + rotation);
			System.out.println("DesRotations " + desRotation);
			
			if (mecanumDrive.getEncValueLeft() < mecanumDrive.getEncValueRight()) {
				mecanumDrive.moveLeftSide(highSpeed);
				mecanumDrive.moveRightSide(slowSpeed);
			} else if (mecanumDrive.getEncValueRight() < mecanumDrive.getEncValueLeft()) {
				mecanumDrive.moveLeftSide(slowSpeed);
				mecanumDrive.moveRightSide(highSpeed);
			} else {
				mecanumDrive.moveLeftSide(highSpeed);
				mecanumDrive.moveRightSide(highSpeed);
			}

			currentState = DriverStation.getInstance().isAutonomous();
		}

		for (int i = 0; i < repeatTime; i++) {
			mecanumDrive.moveLeftSide(0);
			mecanumDrive.moveRightSide(0);
			mecanumDrive.resetEncoders();
		}
	}

	public static void moveBackward(double inches) {
		double converstion = (ticksPerRotation / (6 * Math.PI));
		double desRotation = converstion * Math.abs(inches);
		System.out.println("DesRotations " + desRotation);

		double speed = 0;
		double highSpeed = 0;
		double slowSpeed = 0;
		double rotation = 0.0;

		mecanumDrive.resetEncoders();

		while ((rotation < desRotation) && currentState) {
			if (rotation < (desRotation / 2)) {
				speed = SpecialFunctions.map(mecanumDrive.getEncValueRight(), 0, desRotation / 2, .3, .7);
			} else {
				speed = SpecialFunctions.map(mecanumDrive.getEncValueRight(), desRotation / 2, desRotation, .7, .2);
			}
			highSpeed = -speed;
			slowSpeed = -speed + 0.1;

			rotation = Math.abs(mecanumDrive.getEncValueLeft());

			System.out.println("Rotations " + rotation);
			System.out.println("DesRotations " + desRotation);
			if (mecanumDrive.getEncValueLeft() < mecanumDrive.getEncValueRight()) {
				mecanumDrive.moveLeftSide(highSpeed);
				mecanumDrive.moveRightSide(slowSpeed);
			} else if (mecanumDrive.getEncValueRight() < mecanumDrive.getEncValueLeft()) {
				mecanumDrive.moveLeftSide(slowSpeed);
				mecanumDrive.moveRightSide(highSpeed);
			} else {
				mecanumDrive.moveLeftSide(highSpeed);
				mecanumDrive.moveRightSide(highSpeed);
			}
			currentState = DriverStation.getInstance().isAutonomous();
		}

		for (int i = 0; i < repeatTime; i++) {
			mecanumDrive.moveLeftSide(0);
			mecanumDrive.moveRightSide(0);
			mecanumDrive.resetEncoders();
		}
	}

	public static void strafeRight(double seconds) {
		if(!checkAuton()) return;
		mecanumDrive.mecanumDrive_Cartesian(-1, 0, 0, 0);
		Timer.delay(seconds);
		mecanumDrive.mecanumDrive_Cartesian(0, 0, 0, 0);

	}

	public static void strafeLeft(double seconds) {
		if(!checkAuton())return;
		mecanumDrive.mecanumDrive_Cartesian(1, 0, 0, 0);
		Timer.delay(seconds);
		mecanumDrive.mecanumDrive_Cartesian(0, 0, 0, 0);

	}

	public static boolean checkAuton(){
		boolean isAuton = DriverStation.getInstance().isAutonomous(); 
		if(isAuton)
			return true;
		else
			return false;
	}
	
	public static void shootFor(double seconds, boolean shoot, boolean feed) {
		if(!checkAuton())return;
		shooterManager(shoot, feed);
		seconds = seconds < 15.0 ? seconds : 15;
		Timer.delay(seconds);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void turnGyro(char direction, double angle, boolean resetGyro) {
		double otherNumber = 0.0;
		double slowMotorSpeed = 0.3;
		double speed = .5;
		int state = 1;
		// angle /= 2.0;
		if (resetGyro) {
			gyro.reset();
		}
		while (state == 1) {
			otherNumber = Math.abs(gyro.getAngle());
			System.out.println("Gyro : " + otherNumber);
			if ((direction == 'L') || (direction == 'l')) {
				speed = map(otherNumber, 0, angle, .7, .2);
				mecanumDrive.mecanumDrive_Polar(0, 0, speed);
				if (otherNumber > angle) {
					state = 2;
				}
			} else if ((direction == 'R') || (direction == 'r')) {
				speed = map(otherNumber, 0, angle, .7, .2);
				mecanumDrive.mecanumDrive_Polar(0, 0, -speed);
				if (otherNumber > angle) {
					state = 2;
				}
			} else {
				System.out.println("This is not a direction. Usage: L or R");
			}
		}
		while (state == 2) {
			otherNumber = gyro.getAngle();
			if (otherNumber < angle) {
				System.out.println("S 1");
				mecanumDrive.mecanumDrive_Polar(0, 0, slowMotorSpeed);
			} else if (otherNumber >= angle) {
				System.out.println("S 2");
				mecanumDrive.mecanumDrive_Polar(0, 0, 0);
				state = 3;
			} else if (otherNumber > angle) {
				System.out.println("S 3");
				mecanumDrive.mecanumDrive_Polar(0, 0, -slowMotorSpeed);
				if (otherNumber <= angle) {
					System.out.println("S 4");
					mecanumDrive.mecanumDrive_Polar(0, 0, 0);
					state = 4;
				}
			} else {
				System.out.println("S 5");
				state = 3;
			}
			System.out.println("Still Running");
		}
		for (int i = 0; i < 100; i++) {
			mecanumDrive.moveLeftSide(0);
			mecanumDrive.moveRightSide(0);
			mecanumDrive.resetEncoders();
		}
	}

}