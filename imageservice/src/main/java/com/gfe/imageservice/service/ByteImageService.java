package com.gfe.imageservice.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.gfe.imageservice.model.ByteImage;
import com.gfe.imageservice.model.Image;
import com.gfe.imageservice.model.Image.ImageFormat;
import com.gfe.imageservice.model.ImageDto;
import com.gfe.imageservice.persistence.ImageRepository;
import com.gfe.imageservice.utils.PaginatedRequest;

@Service(value="imageService")
public class ByteImageService implements ImageService{

	Logger log = LoggerFactory.getLogger(ByteImageService.class);
	
	@Autowired
	ImageRepository imageRepository;
	
	@Override
	public Image find(BigDecimal id) {
		Image image = imageRepository.find(id);
		return image;
	}

	@Override
	public List<Image> findAll() {
		List<Image> images = imageRepository.findAll();
		return images;
	}

	@Override
	public List<Image> findAllLike(Image image) {
		List<Image> images = imageRepository.findAllLike(image);
		return images;
	}
	
	@Override
	public List<Image> filter(Image image, PaginatedRequest request) throws Exception {
		List<Image> images = imageRepository.filter(image, request);
		return images;
	}
	
	@Override
	public Image insert(Image image) {
		try {
			image = imageRepository.insert(image);
		} catch (InvalidDataAccessApiUsageException | IOException e) {
			ByteImage.Builder builder = new ByteImage.Builder();
			image = builder.build();
			log.error("Error al construir la imagen insertada. El registro podría haberse insertado en la base de datos");
			log.error(e.getMessage());
		}
		return image;
	}

	@Override
	public Image update(Image image) {
		try {
			image = imageRepository.update(image);
		} catch (IOException e) {
			log.error("Error al generar el byte[] de la imagen. No se ha actualizado en la base de datos.");
			log.error(e.getMessage());
		}
		return image;
	}

	@Override
	public void delete(BigDecimal[] ids) {
		for(BigDecimal id : ids) {
			imageRepository.delete(id);
		}
	}

	@Override
	public Image dtoToImage(ImageDto dto) {
		ByteImage.Builder imageBuilder = new ByteImage.Builder();
		ByteImage image = imageBuilder.setId(dto.getId())
										.setName(dto.getName())
										.setDescription(dto.getDescription())
										.setFormat(ImageFormat.getIfPresent(dto.getFormat() != null ? dto.getFormat().toUpperCase() : null))
										.setImageBytes(dto.getImageBytes())
										.build();
		return image;		
	}
	
	@Override
	public ImageDto imageToDto(Image image) {
		ImageDto dto = new ImageDto();
		dto.setId(image.getId()); 
		dto.setName(image.getName()); 
		dto.setDescription(image.getDescription()); 
		dto.setFormat(image.getFormat() != null ? image.getFormat().name() : null);
		dto.setWidth(image.getWidth());
		dto.setHeight(image.getHeight()); 
		dto.setUrl(image.getUrl()); 
		try {
			dto.setImageBytes(image.getImageBytes());
		} catch (IOException e) {
			log.error("Error al trasformar la imagen en DTO");
			log.info(e.getMessage());
		}
		return dto;
	}
}
