package org.usfirst.frc.team5854.Utils;

public class SpecialFunctions {

	public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	public static int secToTicks(double secs) {
		return (int) (secs * 500.0);
	}
	public static void colorPicker_BR(Callable<bool>  methodOne, Method methodTwo)
	
}
