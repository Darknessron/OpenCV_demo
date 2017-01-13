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

public class Main {

  public static int thresh = 40;


  public static void main(String[] args) {
    System.out.println("Welcome to OpenCV " + Core.VERSION);
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    Mat src = null;
    Mat gray = new Mat();
    // Load source image
    src = Imgcodecs.imread("data/car-3.jpg");
    
    

    // Convert image to gray and blur it
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    Imgproc.blur(gray, gray, new Size(3, 3));
    Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);
    //Imgproc.Laplacian(src, dst, src.depth());
    
    

    Mat drawing = threshCallback(gray);
    
    Mat origin = Imgcodecs.imread("data/car-2.jpg");
    
    Mat dest =  Mat.zeros( origin.size(), CvType.CV_8UC3 );
    Imgproc.resize(drawing, drawing, dest.size());
    
    origin.copyTo(dest, drawing);
    
    Imgcodecs.imwrite("data/test1.jpg", dest);
    //Imgcodecs.imwrite("data/car-4.jpg", drawing);
    
    /*
    //Show img
    ImageGui gui = new ImageGui(drawing, "drawing");
    gui.imshow();
    */
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
    drawing.setTo(new Scalar(0,0,0));
    for( int i = 0; i< contours.size(); i++ )
       {
         Scalar color = new Scalar(255, 255, 255);
         Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
       }

    return drawing;
    
  }

}
