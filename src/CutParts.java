import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
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
	static 	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static void main(String[] args) {
		Mat src = Imgcodecs.imread("data/test.jpg");
		Mat mask = Imgcodecs.imread("data/car-mask.jpg");
		
		Point part0 = new Point(445, 665);
                Point part1 = new Point(677, 553);
                Point part2 = new Point(665, 1105);
                Point part3 = new Point(917, 953);
                Point part4 = new Point(441, 1525);
                Point part5 = new Point(825, 1641);
                Point part6 = new Point(821, 1861);
                Point part7 = new Point(969, 2085);
                Point part8 = new Point(1169, 1337);
                Point part9 = new Point(1229, 1857);
                Point part10 = new Point(757, 2153);
		
		
		
		List<Mat> parts = cutROI(src, mask, part0, part1, part2, part3, part4, part5, part6, part7, part8, part9, part10);
		
		for (int i = 0; i< parts.size(); i++)	{
			Imgcodecs.imwrite("data/part-"+i+".jpg", parts.get(i));
		}
		
		
	}
	
	public static List<Mat> cutROI(Mat src, Mat mask, Point... targetParts)	{
		List<Mat> resultList = new ArrayList<Mat>();
		Mat hierarchy = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Scalar black = new Scalar(0, 0, 0);
		Scalar white = new Scalar(255, 255, 255);
		boolean isInside = false;
		Mat result = null;

		//setting mask
		Imgproc.resize(src, src, mask.size());
		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(mask, mask, new Size(3, 3));
		
		//Convert image to Binary (only black and with)
		Imgproc.threshold(mask, mask, 128, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		
		
		Imgproc.Canny(mask, mask, thresh, thresh * 2);
		
		//init target part
		Mat targetPart = null;
		
		// Find contours from mask
		Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
		
		System.out.println(contours.size());
		
		
		MatOfPoint2f shape = null;
		
		for (Point target : targetParts)	{
			targetPart = Mat.zeros(mask.size(), CvType.CV_8UC3);
			targetPart.setTo(black);
			for (MatOfPoint mop : contours)	{
				shape = new MatOfPoint2f(mop.toArray());
				isInside = Imgproc.pointPolygonTest(shape, target, false) >=0;
				
				if (isInside)	{
					result = new Mat();
					Imgproc.fillConvexPoly(targetPart, mop, white);
					src.copyTo(result, targetPart);
					resultList.add(result);
					break;
				}
			}
		}
		return resultList;
	}
}
