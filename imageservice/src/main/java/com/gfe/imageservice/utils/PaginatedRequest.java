package com.gfe.imageservice.utils;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PaginatedRequest<T> implements Pageable {

	private static final long serialVersionUID = 6524644424685228417L;
	
	int pageNumber;
	int pageSize;
	long offset;
	Sort sort;
	T filter;
	
	public PaginatedRequest(int page, int size, T filter) {
		this.pageNumber = page >= 0 ? page : 0;
		this.pageSize = size >= 1 ? size : 10;
		this.sort = Sort.by(Direction.ASC, "IMAGE_ID");
		this.filter = filter;
	}
	
	@JsonCreator
	public PaginatedRequest(Map<String, Integer> requestParams, T filter) {
		this(requestParams.get("page") != null ? requestParams.get("page") : 0,
								requestParams.get("size") != null ? requestParams.get("size") : 10,
								filter);
	}
	
	public T getFilter() {
		return filter;
	}

	public void setFilter(T filter) {
		this.filter = filter;
	}

	@Override
	public int getPageNumber() {
		return this.pageNumber;
	}

	@Override
	public int getPageSize() {
		return this.pageSize;
	}

	@Override
	public long getOffset() {
		return this.offset;
	}

	@Override
	public Sort getSort() {
		return this.sort;
	}

	@Override
	public Pageable next() {
		return new PaginatedRequest<T>(this.pageNumber + 1, this.pageSize, this.filter);
	}

	@Override
	public Pageable previousOrFirst() {
		if(this.pageNumber > 0) {
			return new PaginatedRequest<T>(this.pageNumber -1, this.pageSize, this.filter);
		}
		return this;
	}

	@Override
	public Pageable first() {
		if(this.pageNumber > 0) {
			return new PaginatedRequest<T>(0, this.pageSize, this.filter);
		}
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return this.pageNumber > 0;
	}
}
