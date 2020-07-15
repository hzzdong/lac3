package com.linkallcloud.web.vc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkallcloud.core.util.Utils;

public abstract class ValidateCode {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String SECURITY_CODE_KEY = "_S_C_KEY_";

	private boolean numeric = false;
	private int securityCodeLength = 4;
	private int imageWidth = 90;
	private int imageHight = 35;

	public ValidateCode() {
		super();
	}

	public ValidateCode(boolean numeric) {
		super();
		this.numeric = numeric;
	}

	public ValidateCode(boolean numeric, int securityCodeLength, int imageWidth, int imageHight) {
		super();
		this.numeric = numeric;
		this.securityCodeLength = securityCodeLength;
		this.imageWidth = imageWidth;
		this.imageHight = imageHight;
	}

	public void generate(HttpServletRequest request, HttpServletResponse response) {
		String code = generateSecurityCodeText();
		BufferedImage codeImage = bulidSecurityCodeImage(code);
		storageAndSendSecurityCode(request, response, code, codeImage);
	}

	private void storageAndSendSecurityCode(HttpServletRequest request, HttpServletResponse response, String code,
			BufferedImage codeImage) {
		storageSecurityCode(request, response, code);

		try {
			ImageIO.write(codeImage, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected abstract void storageSecurityCode(HttpServletRequest request, HttpServletResponse response, String code);

	protected String generateSecurityCodeText() {
		if (numeric) {
			return Utils.getNumericRandomID(securityCodeLength);
		} else {
			return Utils.getRandomID(securityCodeLength);
		}
	}

	private BufferedImage bulidSecurityCodeImage(String code) {
		Random random = new Random();
		BufferedImage image = new BufferedImage(imageWidth, imageHight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, imageWidth, imageHight);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		// 随机产生干扰线底线
		for (int i = 0; i < 4; i++) {
			int x = random.nextInt(imageWidth);
			int y = random.nextInt(imageHight);
			int xl = random.nextInt(15);
			int yl = random.nextInt(15);
			g.setColor(getRandColor(10, 130));
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 随机产生干扰线
		for (int i = 0; i < 4; i++) {
			g.setColor(getRandColor(20, 140));
			g.drawLine(random.nextInt(imageWidth), random.nextInt(imageHight), random.nextInt(imageWidth),
					random.nextInt(imageHight));
		}

		double oldrot = 0;
		for (int i = 0; i < securityCodeLength; i++) {
			String vCode = String.valueOf(code.charAt(i));
			g.setColor(getRandColor(20, 140));

			double rot = (random.nextInt(60) - 30) * Math.PI / 180;
			g.rotate(-oldrot, oldrot == 0 ? 10 : (15 * (i - 1) + 10), 15);
			oldrot = rot;
			g.rotate(rot, 15 * i + 10, 15);
			float stroke = Math.abs(random.nextFloat() % 30);
			g.setStroke(new BasicStroke(stroke));

			g.drawString(vCode, 15 * i + 10, 20);
		}
		g.dispose();
		return image;
	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public int getSecurityCodeLength() {
		return securityCodeLength;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHight() {
		return imageHight;
	}

	public boolean validate(HttpServletRequest request, HttpServletResponse response, String code) {
		String storageCode = getStoragedSecurityCode(request, response);
		if (storageCode == null || storageCode.length() == 0 || code == null || code.length() == 0) {
			return false;
		}
		return storageCode.equals(code);
	}

	protected abstract String getStoragedSecurityCode(HttpServletRequest request, HttpServletResponse response);

}
