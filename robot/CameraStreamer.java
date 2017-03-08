package org.usfirst.frc.team5854.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoMode.PixelFormat;

public class CameraStreamer
{
  private UsbCamera usbCamera;
  private MjpegServer mjpegServer1;
  private CvSink cvSink;
  private CvSource outputStream;
  private MjpegServer mjpegServer2;
  
  public CameraStreamer(int cameraId, int port)
  {
    usbCamera = new UsbCamera("USB Camera " + port, cameraId);
    usbCamera.setResolution(640, 400);
    usbCamera.setBrightness(1);
    
    mjpegServer1 = new MjpegServer("serve_USB Camera " + port, port);
    
    mjpegServer1.setSource(usbCamera);
    
    cvSink = new CvSink("opencv_USB Camera " + port);
    
    cvSink.setSource(usbCamera);
    
    outputStream = new CvSource("Blur", VideoMode.PixelFormat.kMJPEG, 640, 480, 30);
    
    mjpegServer2 = new MjpegServer("serve_Blur " + port , port + 1);
    
    mjpegServer2.setSource(outputStream);
  }
  
  public void setBrightness(int brightness)
  {
    usbCamera.setBrightness(brightness);
  }
  
  public void setResolution(int width, int height)
  {
    usbCamera.setResolution(640, 400);
  }
  
}
