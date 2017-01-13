import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
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
public class Rotation {
	public static void main(String[] args) {
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	    /// Load the image
	    Mat src = Imgcodecs.imread("data/car-2.jpg");

	    MatOfPoint2f srcTri = new MatOfPoint2f();
	    MatOfPoint2f dstTri = new MatOfPoint2f();

	    Mat rot_mat = new Mat( 2, 3, CvType.CV_32FC1 );
	    Mat warp_mat = new Mat( 2, 3, CvType.CV_32FC1 );
	    Mat warp_rotate_dst = new Mat();


	    /// Set the dst image the same type and size as src
	    Mat warp_dst = Mat.zeros( src.rows(), src.cols(), src.type() );

	    /// Set your 3 points to calculate the  Affine Transform
	    srcTri.fromArray(new Point(0,0), new Point(src.cols() - 1, 0), new Point(0, src.rows() - 1));
	    dstTri.fromArray(new Point(src.cols()*0.0, src.rows()*0.33), new Point(src.cols()*0.85, src.rows()*0.25), new Point(src.cols()*0.15, src.rows()*0.7));


	    /// Get the Affine Transform
	    warp_mat = Imgproc.getAffineTransform( srcTri, dstTri );

	    /// Apply the Affine Transform just found to the src image
	    Imgproc.warpAffine( src, warp_dst, warp_mat, warp_dst.size() );

	    /** Rotating the image after Warp */

	    /// Compute a rotation matrix with respect to the center of the image
	    Point center = new Point( warp_dst.cols()/2, warp_dst.rows()/2 );
	    double angle = -50.0;
	    double scale = 0.6;

	    /// Get the rotation matrix with the specifications above
	    rot_mat = Imgproc.getRotationMatrix2D( center, angle, scale );

	    /// Rotate the warped image
	    Imgproc.warpAffine( warp_dst, warp_rotate_dst, rot_mat, warp_dst.size() );
	    
	    Imgcodecs.imwrite("data/test7.jpg", warp_rotate_dst);
	}
}
