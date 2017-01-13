import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.meng.highgui.ImageGui;

public class Main {

  public static int thresh = 20;


  public static void main(String[] args) {
    System.out.println("Welcome to OpenCV " + Core.VERSION);
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    Mat src = null;
    Mat gray = new Mat();
    Mat dst = new Mat();
    // Load source image
    src = Imgcodecs.imread("data/car-2.jpg");
    
    

    // Convert image to gray and blur it
    //Imgproc.blur(gray, gray, new Size(3, 3));
    Imgproc.Laplacian(src, dst, src.depth());
    //Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);
    Imgproc.cvtColor(dst, gray, Imgproc.COLOR_BGR2GRAY);
    
    

    Mat drawing = threshCallback(gray);
    
    //Show img
    ImageGui gui = new ImageGui(drawing, "drawing");
    gui.imshow();
  }

  public static Mat threshCallback(Mat gray) {
    Mat canny = new Mat();
    ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    Mat hierarchy = new Mat();
    
    // Detect edges using canny
    Imgproc.Canny( gray, canny, thresh, thresh*2 );
    /// Find contours
    Imgproc.findContours( canny, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );

    /// Draw contours
    
    Mat drawing = Mat.zeros( canny.size(), CvType.CV_8UC3 );
    for( int i = 0; i< contours.size(); i++ )
       {
         Scalar color = new Scalar( 255, 255, 255 );
         Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
       }

    return drawing;
    
  }

}
