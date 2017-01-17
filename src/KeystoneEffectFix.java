import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class KeystoneEffectFix {
	static	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		Mat origin = Imgcodecs.imread("data/test.jpg");
		
		MatOfPoint2f pointsPerspective = new MatOfPoint2f(new Point(2850, 825), new Point(2850, 1760), new Point(3425, 1225), new Point(3425, 290) );
		RotatedRect boxPerspective = Imgproc.minAreaRect(pointsPerspective);
		
		MatOfPoint2f dstPerspective = new MatOfPoint2f(new Point(0, 0), new Point(0, boxPerspective.boundingRect().height -1), new Point(boxPerspective.boundingRect().width-1, boxPerspective.boundingRect().height-1), new Point(boxPerspective.boundingRect().width-1, 0));
		Mat warpPerspectiveMatrix  = Imgproc.getPerspectiveTransform(pointsPerspective, dstPerspective);
		
		Mat rotatedPerspective = new Mat();
		Size sizePerspective = new Size(boxPerspective.boundingRect().width, boxPerspective.boundingRect().height);
		
		Imgproc.warpPerspective(origin, rotatedPerspective, warpPerspectiveMatrix, sizePerspective, Imgproc.INTER_LINEAR);
		
		Imgcodecs.imwrite("data/keystone_effect_Perspective.jpg", rotatedPerspective);
		

		MatOfPoint2f pointsAffine = new MatOfPoint2f(new Point(2850, 825), new Point(2850, 1760), new Point(3425, 290) );
		RotatedRect boxAffine = Imgproc.minAreaRect(pointsAffine);
		
		MatOfPoint2f dstAffine = new MatOfPoint2f(new Point(0, 0), new Point(0, boxPerspective.boundingRect().height -1), new Point(boxPerspective.boundingRect().width-1, 0));
		Mat warpAffineMatrix  = Imgproc.getAffineTransform(pointsAffine, dstAffine);
		
		Mat rotatedAffine = new Mat();
		Size sizeAffine = new Size(boxAffine.boundingRect().width, boxAffine.boundingRect().height);
		
		Imgproc.warpAffine(origin, rotatedAffine, warpAffineMatrix, sizeAffine, Imgproc.INTER_LINEAR);
		
		Imgcodecs.imwrite("data/keystone_effect_Affine.jpg", rotatedAffine);
		
		
	}
}
