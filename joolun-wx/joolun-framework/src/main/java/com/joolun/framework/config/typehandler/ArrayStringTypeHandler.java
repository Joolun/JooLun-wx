package com.joolun.framework.config.typehandler;

import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *  存储到数据库, 将String数组转换成字符串;
 *  从数据库获取数据, 将字符串转为LONG数组.
 */
@MappedTypes({String[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ArrayStringTypeHandler extends BaseTypeHandler<String[]> {

	private static String[] l = new String[]{};

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
									String[] parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, JSONUtil.toJsonStr(parameter));
	}

	@Override
	public String[] getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return parseValue(rs.getString(columnName));
	}

	@Override
	public String[] getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return parseValue(rs.getString(columnIndex));
	}

	@Override
	public String[] getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return parseValue(cs.getString(columnIndex));
	}

	private String[] parseValue(String value) {
		if (value == null) {
			return l;
		}
		String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return l;
		}
		try {
			if (trimmedValue.startsWith("[") && trimmedValue.endsWith("]")) {
				return JSONUtil.parseArray(trimmedValue).toArray(l);
			}
		} catch (Exception ignored) {
		}
		return Arrays.stream(trimmedValue.split("[,，]"))
				.map(String::trim)
				.filter(item -> !item.isEmpty())
				.toArray(String[]::new);
	}

}
