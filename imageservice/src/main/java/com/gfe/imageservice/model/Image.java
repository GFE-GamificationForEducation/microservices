package com.gfe.imageservice.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

import com.google.common.base.Enums;
import com.google.common.base.Strings;

public abstract class Image implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2193407234492453498L;
	
	protected BigDecimal id;	
	protected String name;
	protected String description;
	protected ImageFormat format;
	
	public BigDecimal getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ImageFormat getFormat() {
		return format;
	}
	
	public BigDecimal getWidth() {
		byte[] imageBytes = new byte[30];
		try {
			imageBytes = this.getImageBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(imageBytes != null) {
			InputStream is = new ByteArrayInputStream(imageBytes);
			BufferedImage buf = null;
			try {
				buf = ImageIO.read(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(buf == null) {
				return BigDecimal.ZERO;
			}
			BigDecimal width = new BigDecimal(buf.getWidth());
			return width;
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getHeight() {
		byte[] imageBytes = new byte[30];
		try {
			imageBytes = this.getImageBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(imageBytes != null) {
			InputStream is = new ByteArrayInputStream(imageBytes);
			BufferedImage buf = null;
			try {
				buf = ImageIO.read(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(buf == null) {
				return BigDecimal.ZERO;
			}
			BigDecimal height = new BigDecimal(buf.getHeight());
			return height;
		}
		return BigDecimal.ZERO;
	}
	
	public abstract ImageType getType();
	public abstract String getUrl();
	public abstract byte[] getImageBytes() throws IOException;
	
	public enum ImageType {
		SIMPLE("simple"),
		URL("url");
		
		private final String value;
		
		ImageType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public static ImageType getIfPresent(String type) {
			ImageType imageType = Enums.getIfPresent(ImageType.class, Strings.nullToEmpty(type)).orNull();
			return imageType;
		}
	}
	
	public enum ImageFormat {
		JPG("jpg"),
		PNG("png"),
		GIF("gif"),
		NOT_SUPPORTED("not supported");
		
		@SuppressWarnings("unused")
		private String value;
		
		ImageFormat(String value) {	
			this.value = value;
		}
				
		public static ImageFormat getIfPresent(String format) {
				ImageFormat imageFormat = Enums.getIfPresent(ImageFormat.class, Strings.nullToEmpty(format)).orNull();
				return imageFormat;
		}
	}
}
