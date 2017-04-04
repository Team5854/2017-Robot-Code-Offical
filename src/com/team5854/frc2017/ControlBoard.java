package com.team5854.frc2017;

import edu.wpi.first.wpilibj.Joystick;

public class ControlBoard {
	private static ControlBoard controlboardInstance = new ControlBoard();
	
	public static ControlBoard getInstance() {
		return controlboardInstance;
	}
	
	// joystick objects used for reading directly from the joysticks
	private final Joystick primaryJoystick, secondaryJoystick;
	
	
	private ControlBoard() {
		primaryJoystick = new Joystick(Constants.PRIMARY_JOYSTICK_PORT);
		secondaryJoystick = new Joystick(Constants.SECONDARY_JOYSTICK_PORT);
	}
	
	// method for getting the driver controls. Abstracts the joystick buttons away 
}
