import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 
 */

/**
 * @author Ron
 *
 */
public class ROI {
	public static void main(String[] args) {
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	    Mat car = Imgcodecs.imread("data/car-2.jpg");
	    Mat black = new Mat(car.rows(), car.cols(), CvType.CV_8UC3);
	    black.setTo(new Scalar(0, 0, 0));
	    MatOfPoint mop = new MatOfPoint();
	    mop.fromArray(new Point(100, 200), new Point(300, 200), new Point(300, 500), new Point(100, 500));
	    Imgproc.fillConvexPoly(black, mop, new Scalar(255, 255, 255));	    
	    	    
	    Mat dest = new Mat(car.size(), CvType.CV_8UC3);
	    
	    car.copyTo(dest, black);
	    
	    
	    Mat gray = new Mat();
	    Imgproc.cvtColor(dest, gray, Imgproc.COLOR_BGR2GRAY);
	    Mat withoutEdges = new Mat();
	    Imgproc.threshold(gray, withoutEdges,1,255,Imgproc.THRESH_BINARY);
	    ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	    Mat hierarchy = new Mat();
	    Imgproc.findContours(withoutEdges, contours, hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
	    MatOfPoint points = contours.get(0);
	    
	    Rect rect = Imgproc.boundingRect(points);
	    Mat crop = dest.submat(rect);	    		
	    
	    Imgcodecs.imwrite("data/test5.jpg", crop);
	    
	    
	}
}
