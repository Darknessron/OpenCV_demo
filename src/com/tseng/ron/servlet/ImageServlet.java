package com.tseng.ron.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tseng.ron.opencv.CutParts;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
@MultipartConfig
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader = null;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageServlet() {
		super();
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		fileFactory.setRepository(FileUtils.getTempDirectory());
		this.uploader = new ServletFileUpload(fileFactory);
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		if (true) return;
		
		
		
		
		
		
		
		
		Mat matrix1 = Imgcodecs.imread("D:\\Project\\OpenCV_demo\\src\\data\\car-1.jpg");
		Mat matrix2 = Imgcodecs.imread("D:\\Project\\OpenCV_demo\\src\\data\\car-2.jpg");
		Mat matrix3 = Imgcodecs.imread("D:\\Project\\OpenCV_demo\\src\\data\\car-3.jpg");
		
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		ObjectNode objectNode1 = mapper.createObjectNode();
		objectNode1.put("imageType", "jpeg");
		objectNode1.put("base64", parseImageToBase64(matToBufferedImage(matrix1)));

		ObjectNode objectNode2 = mapper.createObjectNode();
		objectNode2.put("imageType", "jpeg");
		objectNode2.put("base64", parseImageToBase64(matToBufferedImage(matrix2)));

		ObjectNode objectNode3 = mapper.createObjectNode();
		objectNode3.put("imageType", "jpeg");
		objectNode3.put("base64", parseImageToBase64(matToBufferedImage(matrix3)));

		/**
		 * Array contains JSON Objects
		 */
		arrayNode.add(objectNode1);
		arrayNode.add(objectNode2);
		arrayNode.add(objectNode3);

		response.setContentType("application/json");
		response.getWriter().write(arrayNode.toString());
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart)	{
			Collection<Part> uploadFile = request.getParts();
			byte[] data = null;
			File tempFile = null;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode arrayNode = mapper.createArrayNode();
			CutParts cutParts = new CutParts();
			for (Part part : uploadFile)	{
				tempFile = File.createTempFile("temp+prefix", "surfix", FileUtils.getTempDirectory());
				data = IOUtils.toByteArray(part.getInputStream());
				FileUtils.writeByteArrayToFile(tempFile, data);
				List<Mat> parts = cutParts.cutROI(tempFile);
				ObjectNode node = null;
				for (Mat partImage : parts)	{
					node = mapper.createObjectNode();
					node.put("imageType", "jpeg");
					node.put("base64", parseImageToBase64(matToBufferedImage(partImage)));
					
					arrayNode.add(node);
				}
			}
			String result = arrayNode.toString();
			response.setContentType("application/json");
			response.getWriter().write(result);
		}
	}
	
	public static String parseImageToBase64(BufferedImage image) throws IOException	{
		String result = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", baos);
		result = DatatypeConverter.printBase64Binary(baos.toByteArray());
		return result;
	}

	public static BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;

		matrix.get(0, 0, data);

		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;

		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;

			// bgr to rgb
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;

		default:
			return null;
		}

		BufferedImage image = new BufferedImage(cols, rows, type);
		image.getRaster().setDataElements(0, 0, cols, rows, data);

		return image;
	}

}
