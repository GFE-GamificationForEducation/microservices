package com.gfe.imageservice.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.gfe.imageservice.model.Image;
import com.gfe.imageservice.model.ImageDto;
import com.gfe.imageservice.utils.PaginatedRequest;

public interface ImageService {
	
	Image find(BigDecimal id);
	List<Image> findAll();
	List<Image> findAllLike(Image image);
	List<Image> filter(Image image, PaginatedRequest request) throws Exception;
	Image insert(Image image);
	Image update(Image image);
	void delete(BigDecimal[] ids);
	Image dtoToImage(ImageDto dto);
	ImageDto imageToDto(Image image) throws IOException;
}
