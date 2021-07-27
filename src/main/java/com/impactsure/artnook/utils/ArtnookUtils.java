package com.impactsure.artnook.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.springframework.web.multipart.MultipartFile;

public class ArtnookUtils {
	public static String convertToBase64String(MultipartFile file) {
		StringBuilder sb = new StringBuilder();
		String base64str = null;
		try {
			base64str = DatatypeConverter.printBase64Binary(file.getBytes());

			sb.append("data:");
			sb.append(file.getContentType());
			sb.append(";base64,");
			sb.append(base64str);

			System.out.println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String convertToBase64String(String url) {
		StringBuilder sb = new StringBuilder();
		try {

			URL imageUrl = new URL(url);
			URLConnection ucon = imageUrl.openConnection();
			InputStream is = ucon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			baos.flush(); // Converting Image byte array into Base64 String
			String imageDataString = DatatypeConverter.printBase64Binary(baos.toByteArray());
			System.out.println("imageDataString : " + imageDataString);

			sb.append("data:");
			sb.append("image/png");
			sb.append(";base64,");
			sb.append(imageDataString);

			System.out.println("Image Successfully Manipulated!");
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return sb.toString();
	}

	public static String convertDateFormat(String date) {
		String resultDate = "";
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date1 = null;
		try {
			date1 = format1.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(format2.format(date1));
		resultDate = format2.format(date1);
		return resultDate;
	}

}