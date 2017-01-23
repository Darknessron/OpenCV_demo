package com.tseng.ron.opencv;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Test {
	static	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static void main(String[] args) {
		//set Angle
		float rotx = 0;
		float roty = 45;
		float rotz = 90;
		
		int f = 2; // this is also configurable, f=2 should be about 50mm focal length
		
		Mat img = Imgcodecs.imread("data/test.jpg");
		
		int h = img.rows();
		int w = img.cols();

		float cx = (float) Math.cos(Math.toRadians(rotx));
		float sx = (float) Math.sin(Math.toRadians(rotx));
		float cy = (float) Math.cos(Math.toRadians(roty));
		float sy = (float) Math.sin(Math.toRadians(roty));
		float cz = (float) Math.cos(Math.toRadians(rotz));
		float sz = (float) Math.sin(Math.toRadians(rotz));

		// last column not needed, our vector has z=0
		float[][] roto = new float[][]{
			{ cz * cy, cz * sy * sx - sz * cx },
			{ sz * cy, sz * sy * sx + cz * cx },
			{ -sy, cy * sx }
		};

		float[][] pt = new float[][]{
			{ -w / 2, -h / 2 },
			{ w / 2, -h / 2 },
			{ w / 2, h / 2 },
			{ -w / 2, h / 2 }
		};
		float[][] ptt = new float[4][2];
		float pz = 0;
		for (int i = 0; i < 4; i++) {
		    pz = pt[i][0] * roto[2][0] + pt[i][1] * roto[2][1];
		    ptt[i][0] = w / 2 + (pt[i][0] * roto[0][0] + pt[i][1] * roto[0][1]) * f * h / (f * h + pz);
		    ptt[i][1] = h / 2 + (pt[i][0] * roto[1][0] + pt[i][1] * roto[1][1]) * f * h / (f * h + pz);
		}
		Mat in_pt = new MatOfPoint2f(new Point(0, 0), new Point(w, 0), new Point(w, h), new Point(0, h));
		Mat out_pt = new MatOfPoint2f(new Point(ptt[0][0], ptt[0][1]), new Point(ptt[1][0], ptt[1][1]), new Point(ptt[2][0], ptt[2][1]), new Point(ptt[3][0], ptt[3][1]));

		Mat transform = Imgproc.getPerspectiveTransform(in_pt, out_pt);

		Mat img_in = img.clone();
		Imgproc.warpPerspective(img_in, img, transform, img_in.size());
		
		Imgcodecs.imwrite("data/Test123.jpg", img);
		
	}
}
