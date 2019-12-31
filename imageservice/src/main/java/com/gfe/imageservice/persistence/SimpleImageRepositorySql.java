package com.gfe.imageservice.persistence;

public class SimpleImageRepositorySql {

	public static final String SCHEMA = " GFE";
	public static final String TABLE = "IMAGES ";
	public static final String ALIAS = " IMG";
	public static final String ID = "IMAGE_ID ";
	
	public static final String SELECT = "SELECT "
										+ ALIAS	+ "." + "IMAGE_ID ID, "
										+ ALIAS	+ "." + "NAME NAME, "
										+ ALIAS	+ "." + "DESCRIPTION DESCRIPTION, "
										+ ALIAS	+ "." + "FORMAT FORMAT, "
										+ ALIAS	+ "." + "URL URL, "
										+ ALIAS	+ "." + "IMAGE_BYTES IMAGEBYTES ";
	
	public static final String INSERT = "INSERT INTO "
										+ SCHEMA + "." + TABLE 
										+ "( NAME, "
										+ " DESCRIPTION, "
										+ " FORMAT, "
										+ " URL, "
										+ " IMAGE_BYTES) "
										+ " VALUES "
										+ " (?, ?, ?, ?, ?)";
	
	public static final String UPDATE = "UPDATE "
										+ SCHEMA + "." + TABLE + ALIAS
										+ " SET " 
										+ " NAME = ?, "
										+ " DESCRIPTION = ?, "
										+ " FORMAT = ?, "
										+ " URL = ? "
										+ " WHERE "
										+ ALIAS + "." + ID + " = ? ";
	
	public static final String DELETE = "DELETE "
										+ " FROM "
										+ SCHEMA + "." + TABLE + ALIAS
										+ " WHERE "
										+ ALIAS + "." + ID + " = ?";
	
	public static final String FROM = " FROM " + SCHEMA + "." + TABLE + ALIAS;
	
	public static final String WHERE_ALL = " WHERE 1=1 ";
	
	public static final String WHERE_ID = " AND " + ALIAS + "." + ID + " = ?";
	
	public static final String FIND = SELECT + FROM + WHERE_ALL + WHERE_ID;
	
	public static final String FIND_ALL = SELECT + FROM;
	
	public static final String FIND_ALL_LIKE = FIND_ALL + WHERE_ALL;
	
	public static final String SORT_ASC = " ORDER BY ? ASC ";
	
	public static final String SORT_DESC = " ORDER BY ? DESC ";
	
	public static final String PAGINATION = " LIMIT ? OFFSET ? ";
}
