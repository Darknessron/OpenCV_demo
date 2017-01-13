import org.opencv.core.Core;
import org.opencv.core.Mat;
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
public class ChangeColor {
	public static void main(String[] args) {
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat input = Imgcodecs.imread("data/car-4.jpg");
		Mat gray = new Mat();
		Imgproc.cvtColor(input, gray, Imgproc.COLOR_BGR2GRAY);
		

	    Mat mask = new Mat();
	    // compute inverse thresholding (dark areas become "active" pixel in the mask) with OTSU thresholding:
	    Imgproc.threshold(gray, mask, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

	    // color all masked pixel red:
	    input.setTo(new Scalar(0,0,255), mask);

	    // compute median filter to remove the whitish black parts and darker white parts

	    Imgcodecs.imwrite("data/car-5.jpg", input);
	    
	    
	}
}
