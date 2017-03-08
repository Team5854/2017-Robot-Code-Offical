package org.usfirst.frc.team5854.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import java.io.PrintStream;

public class IMU
{
  public static final int MAGNETOMETER_ADDRESS = 30;
  
  public static void isMagnetometer(int address)
  {
    System.out.println("checking magnetometer...");
    I2C myMag = new I2C(I2C.Port.kOnboard, 30);
    
    System.out.println(myMag.addressOnly());
  }
}
