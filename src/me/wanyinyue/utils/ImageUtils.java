package me.wanyinyue.utils;

import com.mortennobel.imagescaling.ResampleOp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "imageUtils")
@Scope(value = "singleton")
public class ImageUtils {

	public void saveSourceFile(File srcFile, File toFile) {
		try {
			InputStream is = new FileInputStream(srcFile);
			OutputStream os = new FileOutputStream(toFile);
			byte buffer[] = new byte[1024];
			for (int length = 0; (length = is.read(buffer)) > 0;)
				os.write(buffer, 0, length);

			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveThumb(int width, int height, File srcFile, File toFile)
			throws Exception {
		BufferedImage bufferedImage = ImageIO.read(srcFile);
		BufferedImage distin = new BufferedImage(width, height, 1);
		int srcW = bufferedImage.getWidth();
		int srcH = bufferedImage.getHeight();
		int x = 0;
		int y = 0;
		if (srcH >= 2000)
			bufferedImage = zoomToHalf(bufferedImage, 1000);
		ResampleOp resampleOp = null;
		if (srcW > width || srcH > height) {
			if ((double) srcW / (double) srcH >= (double) width
					/ (double) height) {
				int edge = (int) Math.ceil(((double) srcW * (double) height)
						/ (double) srcH);
				resampleOp = new ResampleOp(edge, height);
			}
			if ((double) srcW / (double) srcH < (double) width
					/ (double) height) {
				int edge = (int) Math.ceil(((double) srcH * (double) width)
						/ (double) srcW);
				resampleOp = new ResampleOp(width, edge);
			}
			BufferedImage rescaledImage = resampleOp
					.filter(bufferedImage, null);
			System.out.println(rescaledImage);
			srcW = rescaledImage.getWidth();
			srcH = rescaledImage.getHeight();
			x = (int) Math.ceil((double) srcW / 2D - (double) width / 2D);
			y = (int) Math.ceil((double) srcH / 2D - (double) height / 2D);
			srcW = (int) Math.ceil((double) srcW / 2D + (double) width / 2D);
			srcH = (int) Math.ceil((double) srcH / 2D + (double) height / 2D);
			Graphics g = distin.getGraphics();
			g.drawImage(rescaledImage, 0, 0, width, height, x, y, srcW, srcH,
					null);
			ImageIO.write(distin, "JPEG", toFile);
		} else {
			System.out
					.println("图片太小，无需缩放！");
		}
	}

	public BufferedImage zoomToHalf(BufferedImage Bi, int height)
			throws Exception {
		int srcW = Bi.getWidth();
		int srcH = Bi.getHeight();
		double Ratio = (double) height / (double) srcH;
		int width = (int) ((double) srcW * Ratio);
		BufferedImage distin = new BufferedImage(width, height, 1);
		Image Itemp = Bi.getScaledInstance(width, height, 1);
		int x = 0;
		int y = 0;
		AffineTransformOp op = new AffineTransformOp(AffineTransform
				.getScaleInstance(Ratio, Ratio), null);
		Itemp = op.filter(Bi, null);
		srcW = Itemp.getWidth(null);
		srcH = Itemp.getHeight(null);
		x = (int) Math.ceil((double) srcW / 2D - (double) width / 2D);
		y = (int) Math.ceil((double) srcH / 2D - (double) height / 2D);
		srcW = (int) Math.ceil((double) srcW / 2D + (double) width / 2D);
		srcH = (int) Math.ceil((double) srcH / 2D + (double) height / 2D);
		Graphics g = distin.getGraphics();
		g.drawImage(Itemp, 0, 0, width, height, x, y, srcW, srcH, null);
		return distin;
	}
}