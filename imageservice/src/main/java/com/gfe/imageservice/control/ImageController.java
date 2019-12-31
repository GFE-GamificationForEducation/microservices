package com.gfe.imageservice.control;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gfe.imageservice.model.Image;
import com.gfe.imageservice.model.Image.ImageFormat;
import com.gfe.imageservice.model.ImageDto;
import com.gfe.imageservice.service.ImageService;
import com.gfe.imageservice.utils.PaginatedRequest;
import com.google.common.io.Files;

@RestController
@CrossOrigin
@RequestMapping("images")
public class ImageController {
	
	Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	ImageService imageService;
	
	@GetMapping("/{id}")
	public ImageDto get(@PathVariable BigDecimal id) throws IOException {
		return imageService.imageToDto(imageService.find(id));
	}
	
	@GetMapping("/")
	public List<ImageDto> getAll() throws IOException {
		List<ImageDto> images = new ArrayList<>();
		for (Image image : imageService.findAll()){
			images.add(imageService.imageToDto(image));
		}
		return images;		
	}
	
	@PostMapping("/filter")
	public List<ImageDto> filter(@RequestBody(required=false) PaginatedRequest<ImageDto> request) throws Exception {
		List<ImageDto> images = new ArrayList<>();
		for (Image img: imageService.filter(imageService.dtoToImage(request.getFilter()), request)) {
			images.add(imageService.imageToDto(img));
		}
		return images;
	}
	
	@PostMapping("/")
	public ImageDto add(@RequestPart MultipartFile imagebytes, 
						@RequestPart(required=false) String name,
						@RequestPart(required=false) String description,
						@RequestPart(required=false)  String format) throws Exception {
		ImageDto dto = new ImageDto();
		dto.setName(name);
		dto.setDescription(description);
		if(format != null) {
			dto.setFormat(format);
		} else {
			format = Files.getFileExtension(imagebytes.getOriginalFilename());
			if(ImageFormat.getIfPresent(format.toUpperCase()) != null) {
				dto.setFormat(format.toUpperCase());
			} else {
				throw new Exception("Unknown file format");
			}
		}
			
		dto.setImageBytes(imagebytes.getBytes());
		Image image = imageService.dtoToImage(dto);
		return imageService.imageToDto(imageService.insert(image));
	}
	
	@PutMapping("/")
	public ImageDto edit(@RequestBody ImageDto dto) throws IOException {
		Image image = imageService.dtoToImage(dto);
		return imageService.imageToDto(imageService.update(image));
	}
	
	@DeleteMapping("/")
	public void delete(@RequestBody BigDecimal[] ids) {
		imageService.delete(ids);
	}
}
