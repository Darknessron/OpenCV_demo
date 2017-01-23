package com.tseng.ron.opencv;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 
 */

/**
 * @author Ron
 *
 */
public class FindPoints {
	public static void main(String[] args) {
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    Mat carCanny =Imgcodecs.imread("data/car-4.jpg");
	    Mat gray = new Mat();
	    Imgproc.cvtColor(carCanny, gray, Imgproc.COLOR_BGR2GRAY);
	    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	    Mat hierarchy = new Mat();
	    Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
	    
	    for (MatOfPoint mop : contours)	{
	    	for (Point point : mop.toList())	{
	    		System.out.println("X === "+point.x + "\t Y===="+point.y);
	    	}
	    }
	}
}
