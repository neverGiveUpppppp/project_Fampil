package com.fampill.api.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.fampill.api.domain.BoardType;

public class BoardTypeHandler implements TypeHandler<BoardType> {

	@Override
	public void setParameter(PreparedStatement ps, int i, BoardType parameter, 
			JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.name());
	}

	@Override
	public BoardType getResult(ResultSet rs, String columnName) throws SQLException {
		return BoardType.valueOfCode(rs.getString(columnName));
	}

	@Override
	public BoardType getResult(ResultSet rs, int columnIndex) throws SQLException {
		return BoardType.valueOfCode(rs.getString(columnIndex));
	}

	@Override
	public BoardType getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return BoardType.valueOfCode(cs.getString(columnIndex));
	}

}
