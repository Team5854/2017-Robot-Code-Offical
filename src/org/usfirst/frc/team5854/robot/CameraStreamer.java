package org.usfirst.frc.team5854.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;

public class CameraStreamer
{
  private static UsbCamera usbCamera0=  new UsbCamera("USB Camera 0", 0);
  private static UsbCamera usbCamera1=  new UsbCamera("USB Camera 1", 1);
  private static UsbCamera usbCamera2=  new UsbCamera("USB Camera 2", 2);
  private MjpegServer mjpegServer1;
  private CvSink cvSink;
  private CvSource outputStream;
  private MjpegServer mjpegServer2;
  
  public void setCameraNumber(int id) {
	  switch (id) {
	  case 0:
		  mjpegServer1.setSource(usbCamera0);
		  break;
	  case 1:
		  mjpegServer1.setSource(usbCamera1);
		  break;
	  case 2:
		  mjpegServer1.setSource(usbCamera2);
		  break;

	  }
  }
  
  public CameraStreamer(int port)
  {
    mjpegServer1 = new MjpegServer("serve_USB Camera " + port, port);
    
    cvSink = new CvSink("opencv_USB Camera " + port);
    
    cvSink.setSource(usbCamera0);
    
    outputStream = new CvSource("Blur", VideoMode.PixelFormat.kMJPEG, 640, 480, 30);
    
    mjpegServer2 = new MjpegServer("serve_Blur " + port , port + 1);
    
    mjpegServer2.setSource(outputStream);
  }
  
  public static void setBrightness(int brightness)
  {
	usbCamera0.setBrightness(brightness);
    usbCamera1.setBrightness(brightness);
    usbCamera2.setBrightness(brightness);
  }  
  public void setResolution()
  {
	usbCamera0.setResolution(640, 400);
    usbCamera1.setResolution(640, 400);
    usbCamera2.setResolution(640, 400);
  }
  
}
