package com.tseng.ron.opencv;
/**
 * 
 */


import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @author Ron
 *
 */
public class CarFrame {
	public static int thresh = 40;
	public static int LINE_SIZE = 4;

	public static int max = 255;
	public static int min = 0;
	
	static	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static Mat getCarFrame(Mat src)	{
		Mat result = null;
		
		// Convert image to gray and blur it
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(gray, gray, new Size(3, 3));
		//Convert image to Binary (only black and with)
		Imgproc.threshold(gray, gray, 128, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		
		Mat canny = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();

		// Detect edges using canny
		Imgproc.Canny(gray, canny, thresh, thresh * 2);
		// Find contours
		Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,
				new Point(0, 0));

		/// Draw contours
		result = Mat.zeros(canny.size(), CvType.CV_8UC3);
		Scalar black = new Scalar(0, 0, 0);
		Scalar white = new Scalar(255, 255, 255);
		result.setTo(black);
		for (MatOfPoint mop : contours)	{
			Imgproc.fillConvexPoly(result, mop, white);
		}
		
		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(result, contours, i, black, LINE_SIZE, Core.LINE_8, hierarchy, 0, new Point());
		}
		
		return result;
	}

	public static void main(String[] args) {
		Mat src = Imgcodecs.imread("data/car-mask.jpg");
		Mat result = getCarFrame(src);
		Imgcodecs.imwrite("data/test2.jpg", result);
	}
}
