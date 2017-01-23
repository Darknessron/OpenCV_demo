package com.tseng.ron.opencv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 
 */

/**
 * @author Ron
 *
 */
public class CutParts {
	public static int thresh = 40;
	public static Point[] PARTS = new Point[11];
	public static Mat MASK = null;
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		initPartsPoint();
	}

	private void initMask() {
		URL maskUrl = getClass().getClassLoader().getResource("data/car-mask.jpg");
		MASK = Imgcodecs.imread(maskUrl.getFile().replaceFirst("/", ""), 0);
	}

	private static void initPartsPoint() {

		PARTS[0] = new Point(445, 665);
		PARTS[1] = new Point(677, 553);
		PARTS[2] = new Point(665, 1105);
		PARTS[3] = new Point(917, 953);
		PARTS[4] = new Point(441, 1525);
		PARTS[5] = new Point(825, 1641);
		PARTS[6] = new Point(821, 1861);
		PARTS[7] = new Point(969, 2085);
		PARTS[8] = new Point(1169, 1337);
		PARTS[9] = new Point(1229, 1857);
		PARTS[10] = new Point(757, 2153);
	}

	public static void main(String[] args) throws IOException {
		URL sourceUrl = ClassLoader.getSystemResource("data/test.jpg");

		Mat src = Imgcodecs.imread(sourceUrl.getFile().replaceFirst("/", ""));
		CutParts cutParts = new CutParts();
		List<Mat> partList = cutParts.cutROI(src);

		for (int i = 0; i < partList.size(); i++) {
			Imgcodecs.imwrite("D:/data/part-" + i + ".jpg", partList.get(i));
		}

	}
	
	public List<Mat> cutROI(File imageFile) {
		Mat src = Imgcodecs.imread(imageFile.getAbsolutePath());
		return cutROI(src);
	}

	public List<Mat> cutROI(Mat src) {
		if (MASK == null) initMask();
		List<Mat> resultList = new ArrayList<Mat>();
		Mat hierarchy = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Scalar black = new Scalar(0, 0, 0);
		Scalar white = new Scalar(255, 255, 255);
		boolean isInside = false;
		Mat result = null;

		// setting mask
		Imgproc.resize(src, src, MASK.size());
		//Imgproc.cvtColor(MASK, MASK, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(MASK, MASK, new Size(3, 3));

		// Convert image to Binary (only black and with)
		Imgproc.threshold(MASK, MASK, 128, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

		Imgproc.Canny(MASK, MASK, thresh, thresh * 2);

		// init target part
		Mat targetPart = null;

		// Find contours from mask
		Imgproc.findContours(MASK, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,
				new Point(0, 0));

		MatOfPoint2f shape = null;
		Rect rect = null;

		for (Point target : PARTS) {
			targetPart = Mat.zeros(MASK.size(), CvType.CV_8UC3);
			targetPart.setTo(black);
			for (MatOfPoint mop : contours) {
				shape = new MatOfPoint2f(mop.toArray());
				isInside = Imgproc.pointPolygonTest(shape, target, false) >= 0;

				if (isInside) {
					result = new Mat();
					rect = Imgproc.boundingRect(mop);
					// show more image than just shape
					rect.x = rect.x - 10;
					rect.y = rect.y - 10;
					rect.height = rect.height + 200;
					rect.width = rect.width + 200;
					// end of show more image than just shape
					Imgproc.rectangle(targetPart, rect.tl(), rect.br(), white, Core.FILLED);
					// Imgproc.fillConvexPoly(targetPart, mop, white);
					src.copyTo(result, targetPart);
					// cut black edges
					result = new Mat(result, rect);
					resultList.add(result);
					break;
				}
			}
		}
		return resultList;
	}
}
