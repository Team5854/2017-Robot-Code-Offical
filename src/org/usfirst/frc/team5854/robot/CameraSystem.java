package org.usfirst.frc.team5854.robot;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Class for implementing a camera system.
 */
public final class CameraSystem implements Runnable {
	
	private int cameraState, maxCameraState, joystickButtonListen;; // used for changing the state of the camera, should it be camera 0 or 1 broadcasting?
	private UsbCamera currentCamera; // list all the cameras you want here
	private CvSink cvsink; // for getting the camera to a Mat object
	private CvSource cameraBroadcast; // for setting up a stream that will broadcast back to the robot
	private Mat cameraImageMat; // the mat object for creating the drawings over the image
	private Joystick cameraControl;
	
	private final int RESOLUTION_X = 320, RESOLUTION_Y = 240, FPS = 25;
	private final Scalar WHITE = new Scalar(255, 255, 255); 
	private final Scalar BLACK = new Scalar(0, 0, 0);
	private final Scalar BLUE = new Scalar(255, 0, 0);
	private final Scalar GREEN = new Scalar(0, 255, 0);
	private final Scalar RED = new Scalar(0, 0, 255);
	
	// call this in Robot.java to initialize the camera system. At the end start the separate thread for the actual broadcasting.
	public CameraSystem(int amountOfCameras, Joystick controlStick, int listenToButton) {
		cameraControl = controlStick;
		joystickButtonListen = listenToButton;
		maxCameraState = amountOfCameras - 1;
		cameraState = 0;
		String cameraName = "Camera " + cameraState;
		currentCamera = new UsbCamera(cameraName, cameraState);
		currentCamera.setResolution(RESOLUTION_X, RESOLUTION_Y); // lower res might be preferred 
		currentCamera.setFPS(FPS);
		cvsink = CameraServer.getInstance().getVideo(currentCamera);
		cameraBroadcast = CameraServer.getInstance().putVideo("Camera Broadcast", RESOLUTION_X, RESOLUTION_Y);
		cameraImageMat = new Mat();
	}
	
	// call this method for running the camera system. After calling initCameraSystem!
	public void run() {
//		System.out.println("STARTING CAMERA THREAD!");
		while (!Thread.interrupted()) {
			if (cameraControl.getRawButton(joystickButtonListen)) { 
				if (cameraState == maxCameraState) cameraState = 0;
				else cameraState++;
//				for (int x = 0; x < 10; x++) System.out.println("CAMERA STATE: " + cameraState);
				currentCamera.free();
//				currentCamera = CameraServer.getInstance().startAutomaticCapture(cameraState);
				String cameraName = "Camera " + cameraState;
				currentCamera = new UsbCamera(cameraName, cameraState);
				currentCamera.setResolution(RESOLUTION_X, RESOLUTION_Y);
				cvsink = CameraServer.getInstance().getVideo(currentCamera);
//				System.out.println("Changed cameras");
			} else {
				if (cvsink.grabFrame(cameraImageMat) == 0) continue;
				
				switch (cameraState) {
					case 0: 
						cameraShootingCrosshairsOverlay(cameraImageMat);
						break;
					case 1: 
						cameraShootingCrosshairsOverlay(cameraImageMat);
						break;
					default:
						break;
				}
				
				cameraBroadcast.putFrame(cameraImageMat);
			}
		}
		
		cameraImageMat.release();
	}
	
	private void cameraShootingCrosshairsOverlay(Mat originalCameraImage) {
		// list cross hair lines here. Just copy and past each set of three lines and modify color, thickness, and coordinates	
		int thickness = 0;
		
		thickness = 5;
		org.opencv.core.Point lineZeroPoint0 = new org.opencv.core.Point(0, 0);
		org.opencv.core.Point lineZeroPoint1 = new org.opencv.core.Point(320, 240);
		Imgproc.line(originalCameraImage, lineZeroPoint0, lineZeroPoint1, WHITE, thickness);
		
		thickness = 5;
		org.opencv.core.Point lineOnePoint0 = new org.opencv.core.Point(320, 0);
		org.opencv.core.Point lineOnePoint1 = new org.opencv.core.Point(0, 240);
		Imgproc.line(originalCameraImage, lineOnePoint0, lineOnePoint1, BLACK, thickness);
	}
	
}
