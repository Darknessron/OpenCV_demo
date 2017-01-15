import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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
	
	static	{
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static Mat maskImage(Mat src, Mat mask)	{
		Imgproc.resize(mask, mask, src.size());
		Mat result = new Mat(src.size(), CvType.CV_8UC3);
		src.copyTo(result, mask);
		
		return result;
	}
	
	
	public static void main(String[] args) {
		Mat src = Imgcodecs.imread("data/car-2.jpg");
		Mat maskFrame = Imgcodecs.imread("data/car-mask.jpg");
		Mat mask = CarFrame.getCarFrame(maskFrame);
		Mat result = maskImage(src, mask);
		

	    Imgcodecs.imwrite("data/test5.jpg", result);
	}
}
