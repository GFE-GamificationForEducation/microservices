package com.gfe.imageservice.model;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class UrlImage extends Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1201505732918784533L;
	
	protected String url;
	
	UrlImage() {
	}

	@Override
	public String getUrl() {
		return url;
	}
	
	@Override
	public byte[] getImageBytes() {
		if (this.url != null) {
			byte[] imageBytes = new byte[0];
			InputStream is = null;
			try {
				URL streamedUrl = new URL(this.url);
				URLConnection conn = streamedUrl.openConnection();
		        conn.setConnectTimeout(5000);
		        conn.setReadTimeout(5000);
		        conn.connect(); 
		        
		        is = streamedUrl.openStream();
		        imageBytes = IOUtils.toByteArray(is);
			} catch (IOException e) {
				e.printStackTrace();
				return imageBytes;
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return imageBytes;
		}
		return new byte[0];
	}
	
	@Override
	public ImageType getType() {
		return ImageType.URL;
	}
	
	public static class Builder implements SimpleBuilder<UrlImage>{

		private UrlImage imageToBuild;
		
		public Builder () {
			if(imageToBuild == null) {
				imageToBuild = new UrlImage();
			}
		}

		public Builder setId(BigDecimal id) {
			imageToBuild.id = id;
			return(this);
		}
		
		public Builder setName(String name) {
			imageToBuild.name = name;
			return(this);
		}

		public Builder setDescription(String description) {
			imageToBuild.description = description;
			return(this);
		}

		public Builder setFormat(ImageFormat format) {
			imageToBuild.format = format;
			return(this);
		}

		public Builder setImageUrl(String url) {
			imageToBuild.url = url;
			return(this);
		}
		 
		@Override
		public UrlImage build() {
			UrlImage builtImage = imageToBuild;
			imageToBuild = new UrlImage();
			return builtImage;
		}
	}
}
