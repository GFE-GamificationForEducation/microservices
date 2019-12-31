package com.gfe.imageservice.model;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class ByteImage extends Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6005943746182940901L;
	
	protected byte[] imageBytes;
	
	ByteImage() {
	}
	
	@Override
	public byte[] getImageBytes() throws IOException {
		return imageBytes;
	}

	@Override
	public String getUrl() {
		if(this.imageBytes != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("data:");
			if(this.format != null) {
				sb.append("image/" + this.format.name().toLowerCase());
			}
			sb.append(";base64,");
			sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(this.imageBytes)));
			return sb.toString();
		}
		return "";
	}

	@Override
	public ImageType getType() {
		return ImageType.SIMPLE;
	}
	
	public static class Builder implements SimpleBuilder<ByteImage>{

		private ByteImage imageToBuild;
		
		public Builder () {
			if(imageToBuild == null) {
				imageToBuild = new ByteImage();
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

		public Builder setImageBytes(byte[] imageBytes) {
			imageToBuild.imageBytes = imageBytes;
			return(this);
		}
		 
		@Override
		public ByteImage build() {
			ByteImage builtImage = imageToBuild;
			imageToBuild = new ByteImage();
			return builtImage;
		}
	}
}
