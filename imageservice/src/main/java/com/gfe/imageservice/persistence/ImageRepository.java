package com.gfe.imageservice.persistence;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.gfe.imageservice.model.Image;
import com.gfe.imageservice.utils.PaginatedRequest;

public interface ImageRepository {

	Image find(BigDecimal id);
	List<Image> findAll();
	List<Image> findAllLike(Image image);
	List<Image> filter(Image image, PaginatedRequest request) throws Exception;
	Image insert(Image image) throws InvalidDataAccessApiUsageException, IOException;
	List<Image> insert(Iterable<? extends Image> images) throws InvalidDataAccessApiUsageException, IOException;
	Image update(Image image) throws IOException;
	void delete(BigDecimal id);
	void delete(Image image);
	void delete(Iterable<? extends Image> images);
}
