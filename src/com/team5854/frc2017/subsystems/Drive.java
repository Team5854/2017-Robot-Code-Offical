package com.team5854.frc2017.subsystems;

import java.lang.invoke.ConstantCallSite;

import com.ctre.CANTalon;
import com.team5854.frc2017.Constants;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Drive extends Subsystem {
	
	private static Drive driveInstance = new Drive();
	
	public static Drive getInstance() {
		return driveInstance;
	}
	
	// objects representing components of the drivertrain
	private final CANTalon leftFrontMaster, rightFrontMaster, leftRearMaster, rightRearMaster; // declare the master motor controllers
	private final CANTalon leftFrontSlave, rightFrontSlave, leftRearSlave, rightRearSlave;  // delcare slave motor controllers
	
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Initializes all components of the drivetrain.
	 */
	private Drive() {
		//set up the ports
		leftFrontMaster = new CANTalon(Constants.LEFT_FRONT_MASTER_ID);
		rightFrontMaster = new CANTalon(Constants.RIGHT_FRONT_MASTER_ID);
		leftRearMaster = new CANTalon(Constants.LEFT_REAR_MASTER_ID);
		rightRearMaster = new CANTalon(Constants.RIGHT_REAR_MASTER_ID);
		leftFrontSlave = new CANTalon(Constants.LEFT_FRONT_SLAVE_ID);
		rightFrontSlave = new CANTalon(Constants.RIGHT_FRONT_SLAVE_ID);
		leftRearSlave = new CANTalon(Constants.LEFT_REAR_SLAVE_ID);
		rightRearSlave = new CANTalon(Constants.RIGHT_REAR_SLAVE_ID);
		
		
		// set the initial control modes for each Talon
		
		//sets the initial control mode for the master talons to PercentVBus and stopped.
		leftFrontMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightFrontMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		leftRearMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightRearMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		leftFrontMaster.set(0.0);
		rightFrontMaster.set(0.0);
		leftRearMaster.set(0.0);
		rightRearMaster.set(0.0);
		
		// sets the initial control mode for the slave talons to Follower and the ids they should follow
		leftFrontSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftFrontSlave.set(Constants.LEFT_FRONT_MASTER_ID);
		rightFrontSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightFrontSlave.set(Constants.RIGHT_FRONT_MASTER_ID);
		leftRearSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftRearSlave.set(Constants.LEFT_REAR_MASTER_ID);
		rightRearSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightRearSlave.set(Constants.RIGHT_REAR_MASTER_ID);	
	}
	
}
