package com.devin.dezhi.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 2025/12/26 23:59.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class VectorTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(
            final PreparedStatement ps, final int i, final String parameter, final JdbcType jdbcType
    ) throws SQLException {
        PGobject pGobject = new PGobject();
        pGobject.setType("vector");
        pGobject.setValue(parameter);
        ps.setObject(i, pGobject);
    }

    @Override
    public String getNullableResult(
            final ResultSet rs, final String columnName
    ) throws SQLException {
        Object object = rs.getObject(columnName);
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public String getNullableResult(
            final ResultSet rs, final int columnIndex
    ) throws SQLException {
        Object object = rs.getObject(columnIndex);
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public String getNullableResult(
            final CallableStatement cs, final int columnIndex
    ) throws SQLException {
        Object object = cs.getObject(columnIndex);
        if (object == null) {
            return null;
        }
        return object.toString();
    }
}
