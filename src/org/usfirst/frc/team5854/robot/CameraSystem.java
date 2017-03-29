package org.usfirst.frc.team5854.robot;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5854.Utils.PixelCoord;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * Class for implementing a camera system.
 */
public final class CameraSystem implements Runnable {
	
	private static int cameraState, maxCameraState; // used for changing the state of the camera, should it be camera 0 or 1 broadcasting?
	private static UsbCamera currentCamera; // list all the cameras you want here
	private static CvSink cvsink; // for getting the camera to a Mat object
	private static CvSource cameraBroadcast; // for setting up a stream that will broadcast back to the robot
	private static Mat cameraImageMat; // the mat object for creating the drawings over the image
	private static PixelCoord[] crosshairCoordinates;

	// call this in Robot.java to initialize the camera system. At the end start the separate thread for the actual broadcasting.
	public CameraSystem(int amountOfCameras) {
		// initialize all camera listed above here
		maxCameraState = amountOfCameras - 1;
		cameraState = 0;
		currentCamera = CameraServer.getInstance().startAutomaticCapture(cameraState);
		currentCamera.setResolution(640, 480); // lower res might be preferred 
		CvSink cvsink = CameraServer.getInstance().getVideo(currentCamera);
		CvSource cameraBroadcast = CameraServer.getInstance().putVideo("Camera Broadcast", 640, 480);
		cameraImageMat = new Mat();
		
		// set up the list of coordinates for the cross hair lines by reading from text file on roboRIO
		crosshairCoordinates = new PixelCoord[100];
		File crosshairsFile = new File("~/crosshairs.txt");
		try {
			Scanner fileScanner = new Scanner(crosshairsFile);
			int crosshairIndex = 0;
			while (fileScanner.hasNextLine() && crosshairIndex < crosshairCoordinates.length) {
				// each line is a pixel coordinates for a line on the image
				String currentString = fileScanner.nextLine();
				String[] currentStringSplit = currentString.split(" ");
				crosshairCoordinates[crosshairIndex] = new PixelCoord(Integer.parseInt(currentStringSplit[0]), Integer.parseInt(currentStringSplit[1]), Integer.parseInt(currentStringSplit[2]), Integer.parseInt(currentStringSplit[3]));
				crosshairIndex++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// call this method for running the camera system. After calling initCameraSystem!
	public void run() {
		while (!Thread.interrupted()) {
			if (false) { // put a method here for reading the joysticks that equates to true
				if (cameraState == maxCameraState) cameraState = 0;
				else cameraState++;
				currentCamera = CameraServer.getInstance().startAutomaticCapture(cameraState);
				currentCamera.setResolution(640, 480);
				cvsink = CameraServer.getInstance().getVideo(currentCamera);
			} else {
				if (cvsink.grabFrame(cameraImageMat) == 0) continue;
				
				switch (cameraState) {
					case 0: 
						cameraZeroMethod(cameraImageMat);
						break;
					case 1: 
						int[][] crosshairList = new int[100][100];
						
						cameraOneMethod(cameraImageMat);
						break;
					default:
						break;
				}
				
				cameraBroadcast.putFrame(cameraImageMat);
			}
		}
	}
	
	private void cameraZeroMethodInit() {
		
	}
	
	private Mat cameraZeroMethod(Mat originalCameraImage) {
		
		return originalCameraImage;
	}
	
	private static void cameraOneMethodInit() {

	}
	
	private void cameraOneMethod(Mat originalCameraImage) {
		for (int crosshairIndex = 0; crosshairIndex < crosshairCoordinates.length; crosshairIndex++) {
			org.opencv.core.Point one = new org.opencv.core.Point(crosshairCoordinates[crosshairIndex].firstx, crosshairCoordinates[crosshairIndex].firsty);
			org.opencv.core.Point two = new org.opencv.core.Point(crosshairCoordinates[crosshairIndex].secondx, crosshairCoordinates[crosshairIndex].secondy);
			Imgproc.line(originalCameraImage, one, two, new Scalar(255, 255, 255), 1);
		}
	}
	
}
