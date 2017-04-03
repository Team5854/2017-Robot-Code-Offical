package org.usfirst.frc.team5854.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Ultrasonar {
	
	
	// The double returned will be in meters
	public static double getDistance(){
		
		double voltage = Robot.input.getVoltage();
		double distance = voltage / .977;
		return distance;
	}
}