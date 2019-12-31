package com.gfe.imageservice.persistence;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gfe.imageservice.model.ByteImage;
import com.gfe.imageservice.model.Image;
import com.gfe.imageservice.model.Image.ImageFormat;
import com.gfe.imageservice.utils.PaginatedRequest;

@Repository
public class SimpleImageRepository implements ImageRepository {
	
	private final String[] sortableProps = {"ID", "NAME", "FORMAT", "WIDTH", "HEIGHT"};
	
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	
	private RowMapper<Image> rowMap = new RowMapper<Image>() {
		@Override
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			ByteImage.Builder builder = new ByteImage.Builder();
			Image image = builder.setId(rs.getBigDecimal("ID"))
									.setName(rs.getString("NAME"))
									.setDescription(rs.getString("DESCRIPTION"))
									.setFormat(rs.getString("FORMAT") != null ? ImageFormat.valueOf(rs.getString("FORMAT")) : null)
									.setImageBytes(rs.getBytes("IMAGEBYTES")).build();
	    	return image;
	    }
	};

	@Override
	public Image find(BigDecimal id) {
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.FIND);
		List<Object> params = new ArrayList<>();
		params.add(id);
		return jdbcTemplate.queryForObject(query.toString(), params.toArray(), rowMap);
	}

	@Override
	public List<Image> findAll() {
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.FIND_ALL);
		return jdbcTemplate.query(query.toString(), rowMap);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Image> findAllLike(Image image) {
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.FIND_ALL_LIKE);
		Map<String, Object> whereMap = getWhereLikeMap(image);
		query.append(whereMap.get("where"));
		List<Object> params = (List<Object>)whereMap.get("params");
		return jdbcTemplate.query(query.toString(), params.toArray(), rowMap);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Image> filter(Image image, PaginatedRequest request) throws Exception {
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.FIND_ALL_LIKE);
		Map<String, Object> whereMap = getWhereLikeMap(image);
		query.append(whereMap.get("where"));
		List<Object> params = (List<Object>)whereMap.get("params");
		if(request != null) {
			if(request.getSort() != null) {
				for(String property : sortableProps) {
					if(request.getSort().getOrderFor(property) != null) {
						query.append(request.getSort().getOrderFor(property).getDirection() == Direction.ASC ?
								SimpleImageRepositorySql.SORT_ASC : SimpleImageRepositorySql.SORT_DESC);
						params.add(property);
					}
				}
			}
			query.append(SimpleImageRepositorySql.PAGINATION);
			params.add(request.getPageSize() > 1 ? request.getPageSize() : 10);
			params.add(request.getPageNumber() > 0 ? request.getPageNumber(): 0);
		}
		
		return jdbcTemplate.query(query.toString(), params.toArray(), rowMap);
	}
	
	@Override
	public Image insert(Image image) throws InvalidDataAccessApiUsageException, IOException {
		
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.INSERT);

		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, image.getName());
				ps.setString(2, image.getDescription());
				ps.setString(3, image.getFormat() != null ? image.getFormat().name() : null);
				ps.setString(4, image.getUrl());
				try {
					ps.setBytes(5, image.getImageBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ps;
			}
		};
		KeyHolder keyHolder = new GeneratedKeyHolder();		
		jdbcTemplate.update(psc, keyHolder);
		ByteImage.Builder builder = new ByteImage.Builder();
		Image completeImage = builder.setId(new BigDecimal(keyHolder.getKey().toString()))
										.setName(image.getName())
										.setDescription(image.getDescription())
										.setFormat(image.getFormat())
										.setImageBytes(image.getImageBytes()).build();
		return completeImage;
	}

	@Override
	public List<Image> insert(Iterable<? extends Image> images) throws InvalidDataAccessApiUsageException, IOException {
		List<Image> completeImages = new ArrayList<Image>();
		for(Image image: images) {
			completeImages.add(insert(image));
		}
		return completeImages;
	}
	
	@Override
	public Image update(Image image) throws IOException {
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.UPDATE);
		List<Object> params = new ArrayList<>();

		params.add(image.getName());
		params.add(image.getDescription());
		params.add(image.getFormat() != null ? image.getFormat().name() : null);
		params.add(image.getUrl());
		params.add(image.getId());
		
		jdbcTemplate.update(query.toString(), params.toArray());
		
		return image;
	}

	@Override
	public void delete(BigDecimal id) {
		StringBuilder query = new StringBuilder(SimpleImageRepositorySql.DELETE);
		List<Object> params = new ArrayList<>();
		
		params.add(id);
		
		jdbcTemplate.update(query.toString(), params.toArray());
	}
	
	@Override
	public void delete(Image image) {
		delete(image.getId());
	}
	
	@Override
	public void delete(Iterable<? extends Image> images) {
		for(Image image: images) {
			delete(image);
		}
	}
	
	private Map<String, Object> getWhereLikeMap(Image image) {
		Map<String, Object> whereMap = new HashMap<>();
		StringBuilder where = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		if(image.getId() != null) {
			where.append(" AND " + SimpleImageRepositorySql.ALIAS + ".ID = ? " );
			params.add(image.getId());
		}
		if(image.getName() != null) {
			where.append(" AND UPPER(" + SimpleImageRepositorySql.ALIAS + ".NAME) LIKE UPPER(?) " );
			params.add("%" + image.getName().replace('*', '%').toUpperCase() + "%");
		}
		if(image.getDescription() != null) {
			where.append(" AND UPPER(" + SimpleImageRepositorySql.ALIAS + ".DESCRIPTION) LIKE UPPER(?) " );
			params.add("%" + image.getDescription().replace('*', '%').toUpperCase() + "%");
		}
		if(image.getFormat() != null && image.getFormat().name() != null) {
			where.append(" AND UPPER(" + SimpleImageRepositorySql.ALIAS + ".FORMAT) LIKE UPPER(?) " );
			params.add("%" + image.getFormat().name().replace('*', '%').toUpperCase() + "%");
		}
		if(image.getName() != null) {
			where.append(" AND " + SimpleImageRepositorySql.ALIAS + ".WIDTH >= ? " );
			params.add(image.getWidth());
		}
		if(image.getName() != null) {
			where.append(" AND " + SimpleImageRepositorySql.ALIAS + ".HEIGHT >= ? " );
			params.add(image.getHeight());
		}
		
		whereMap.put("where", where.toString());
		whereMap.put("params", params);
		return whereMap;
	}
}
