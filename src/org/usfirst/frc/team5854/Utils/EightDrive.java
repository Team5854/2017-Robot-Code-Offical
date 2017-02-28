package org.usfirst.frc.team5854.Utils;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;

public class EightDrive {
	private RobotDrive mecanumDriveOne;
	private CANTalon leftFrontMotorOne;
	private CANTalon rightFrontMotorOne;
	private CANTalon leftRearMotorOne;
	private CANTalon rightRearMotorOne;
	private RobotDrive mecanumDriveTwo;
	private CANTalon leftFrontMotorTwo;
	private CANTalon rightFrontMotorTwo;
	private CANTalon leftRearMotorTwo;
	private CANTalon rightRearMotorTwo;

	/**
	 * @param leftFrontMotorOneValue
	 * @param rightFrontMotorOneValue
	 * @param leftRearMotorOneValue
	 * @param rightRearMotorOneValue
	 * @param leftFrontMotorTwoValue
	 * @param rightFrontMotorTwoValue
	 * @param leftRearMotorTwoValue
	 * @param rightRearMotorTwoValue
	 */
	public EightDrive(int leftFrontMotorOneValue, int rightFrontMotorOneValue, int leftRearMotorOneValue,
			int rightRearMotorOneValue, int leftFrontMotorTwoValue, int rightFrontMotorTwoValue,
			int leftRearMotorTwoValue, int rightRearMotorTwoValue) {
		leftFrontMotorOne = new CANTalon(leftFrontMotorOneValue);
		rightFrontMotorOne = new CANTalon(rightFrontMotorOneValue);
		leftRearMotorOne = new CANTalon(leftRearMotorOneValue);
		rightRearMotorOne = new CANTalon(rightRearMotorOneValue);

		leftFrontMotorTwo = new CANTalon(leftFrontMotorTwoValue);
		rightFrontMotorTwo = new CANTalon(rightFrontMotorTwoValue);
		leftRearMotorTwo = new CANTalon(leftRearMotorTwoValue);
		rightRearMotorTwo = new CANTalon(rightRearMotorTwoValue);

		leftFrontMotorOne.setInverted(true);
		rightFrontMotorOne.setInverted(false);
		leftRearMotorOne.setInverted(true);
		rightRearMotorOne.setInverted(false);

		leftFrontMotorTwo.setInverted(true);
		rightFrontMotorTwo.setInverted(false);
		leftRearMotorTwo.setInverted(true);
		rightRearMotorTwo.setInverted(false);

		mecanumDriveOne = new RobotDrive(leftFrontMotorOne, leftRearMotorOne, rightFrontMotorOne, rightRearMotorOne);
		mecanumDriveTwo = new RobotDrive(leftFrontMotorTwo, leftRearMotorTwo, rightFrontMotorTwo, rightRearMotorTwo);
	}

	public EightDrive(CANTalon leftFrontMotorOne, CANTalon rightFrontMotorOne, CANTalon leftRearMotorOne, CANTalon rightRearMotorOne, CANTalon leftFrontMotorTwo, CANTalon rightFrontMotorTwo, CANTalon leftRearMotorTwo, CANTalon rightRearMotorTwo) {
		this.leftFrontMotorOne = leftFrontMotorOne;
		this.rightFrontMotorOne = rightFrontMotorOne;
		this.leftRearMotorOne = leftRearMotorOne;
		this.rightRearMotorOne = rightRearMotorOne;
		this.leftFrontMotorTwo = leftFrontMotorTwo;
		this.rightFrontMotorTwo = rightFrontMotorTwo;
		this.leftRearMotorTwo = leftRearMotorTwo;
		this.rightRearMotorTwo = rightRearMotorTwo;

		mecanumDriveOne = new RobotDrive(leftFrontMotorOne, leftRearMotorOne, rightFrontMotorOne, rightRearMotorOne);
		mecanumDriveTwo = new RobotDrive(leftFrontMotorTwo, leftRearMotorTwo, rightFrontMotorTwo, rightRearMotorTwo);
	}

	public void setCANTalonDriveMode(CANTalon.TalonControlMode control) {
		leftFrontMotorOne.changeControlMode(control);
		rightFrontMotorOne.changeControlMode(control);
		leftRearMotorOne.changeControlMode(control);
		leftRearMotorOne.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		rightRearMotorOne.changeControlMode(control);
		rightRearMotorOne.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		leftFrontMotorTwo.changeControlMode(control);
		rightFrontMotorTwo.changeControlMode(control);
		leftRearMotorTwo.changeControlMode(control);
		rightRearMotorTwo.changeControlMode(control);
	}

	private double multiplyer = 1.0D;
	private double twistMult = 1.0D;
	private double deadzone = 0.0D;

	public void mecanumDrive_Cartesian(double xAxis, double yAxis, double rotation, double gyroAngle) {
		double xValue = 0.0D;
		double yValue = 0.0D;
		double rValue = 0.0D;
		if ((xAxis > deadzone) || (xAxis < -deadzone)) {
			xValue = xAxis * -1.0D * multiplyer;
		}
		if ((yAxis > deadzone) || (yAxis < -deadzone)) {
			yValue = yAxis * -1.0D * multiplyer;
		}
		if ((rotation > deadzone) || (rotation < -deadzone)) {
			rValue = rotation * -1.0D * twistMult;
		}
		mecanumDriveOne.mecanumDrive_Cartesian(xValue, yValue, rValue, gyroAngle);
		mecanumDriveTwo.mecanumDrive_Cartesian(xValue, yValue, rValue, gyroAngle);
	}

	public void mecanumDrive_Polar(double magnitude, double direction, double rotation) {
		mecanumDriveOne.mecanumDrive_Polar(magnitude, direction, rotation);
		mecanumDriveTwo.mecanumDrive_Polar(magnitude, direction, rotation);
	}

	public void setSpeedMultiplyer(double multiplyer) {
		this.multiplyer = multiplyer;
	}

	public void setTwistMultiplyer(double multiplyer) {
		twistMult = multiplyer;
	}

	public void setDeadband(double deadband) {
		deadzone = deadband;
	}

	public void setBrakeMode(boolean braker) {
		leftFrontMotorOne.enableBrakeMode(braker);
		rightFrontMotorOne.enableBrakeMode(braker);
		leftRearMotorOne.enableBrakeMode(braker);

		rightRearMotorOne.enableBrakeMode(braker);

		leftFrontMotorTwo.enableBrakeMode(braker);
		rightFrontMotorTwo.enableBrakeMode(braker);
		leftRearMotorTwo.enableBrakeMode(braker);
		rightRearMotorTwo.enableBrakeMode(braker);
	}

	public void moveRightSide(double speed) {
		rightFrontMotorOne.set(speed);
		rightRearMotorOne.set(speed);
		rightFrontMotorTwo.set(speed);
		rightRearMotorTwo.set(speed);
	}

	public void moveLeftSide(double speed) {
		leftFrontMotorOne.set(speed);
		leftRearMotorOne.set(speed);
		leftFrontMotorTwo.set(speed);
		leftRearMotorTwo.set(speed);
	}

	public void resetEncoders() {
		rightRearMotorOne.setEncPosition(0);
		leftRearMotorOne.setEncPosition(0);
	}

	public double getEncValueRight() {
		return Math.abs(rightRearMotorOne.getEncPosition());
	}

	public double getEncValueLeft() {
		return Math.abs(leftRearMotorOne.getEncPosition());
	}

	public void stop() {
		moveLeftSide(0);
		moveRightSide(0);
	}
}
