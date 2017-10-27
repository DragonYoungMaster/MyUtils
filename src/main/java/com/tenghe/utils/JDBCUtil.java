package com.tenghe.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.tenghe.utils.model.DatabaseVO;
import com.tenghe.utils.model.TableInfo;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * author: teng.he
 * time: 20:16 2017/8/21
 * desc: jdbc工具类
 */
public final class JDBCUtil {
  private static final Logger LOGGER = Logger.getLogger(JDBCUtil.class);
  private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
  private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
  private static final String DB_URL = "jdbc:";
  private static final String MYSQL = "mysql";
  private static final String ORACLE = "oracle";
  private static Connection con;

  private JDBCUtil() {

  }

  public static synchronized Connection openConnection(DatabaseVO databaseVO) {
    try {
      Class.forName(getDriverClassName(databaseVO.getType()));
      con = DriverManager.getConnection(createUrl(databaseVO.getType(), databaseVO.getIp()
          , databaseVO.getPort(), databaseVO.getDatabaseName()), databaseVO.getUser(), databaseVO.getPassword());
    } catch (ClassNotFoundException e1) {
      LOGGER.warn("JDBCUtil openConnection ClassNotFoundException", e1);
    } catch (SQLException e2) {
      LOGGER.warn("JDBCUtil openConnection SQLException", e2);
    }
    return con;
  }

  private static String getDriverClassName(String dbType) {
    if (MYSQL.equals(dbType)) {
      return MYSQL_DRIVER;
    }
    if (ORACLE.equals(dbType)) {
      return ORACLE_DRIVER;
    }
    return StringUtils.EMPTY;
  }

  private static String createUrl(String dbType, String ip, String port, String dbName) {
    if (MYSQL.equals(dbType)) {
      return DB_URL + dbType + "://" + ip + ":" + port + "/" + dbName + "?useUnicode=true" +
          "&characterEncoding=UTF-8";
    }
    if (ORACLE.equals(dbType)) {
      return DB_URL + dbType + ":thin:@" + ip + ":" + port + ":" + dbName + "?useUnicode=true" +
          "&characterEncoding=UTF-8";
    }

    return StringUtils.EMPTY;
  }

  public static List<Map<String, Object>> convert(ResultSet resultSet) {
    List<Map<String, Object>> resultList = Lists.newArrayList();
    try {
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      while (resultSet.next()) {
        Map<String, Object> values = Maps.newHashMap();
        for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
          int column = i + 1;
          String columnLabel = resultSetMetaData.getColumnLabel(column);
          Object columnValue = resultSet.getObject(column);
          values.put(columnLabel, columnValue);
        }
        resultList.add(values);
      }
    } catch (SQLException e) {
      LOGGER.warn("JDBCUtil convert SQLException", e);
    }
    return resultList;
  }

  public static TableInfo queryTableInfo(Connection con, String tableName) {
    TableInfo tableInfo = new TableInfo();
    tableInfo.setName(tableName);
    String sql = "select * from " + tableName + " limit 1";
    try (PreparedStatement pst = con.prepareStatement(sql)){
      ResultSet result = pst.executeQuery();
      ResultSetMetaData rsd = result.getMetaData();
      List<TableInfo.TableColumnInfo> tableColumnInfoList = Lists.newArrayList();
      for(int i = 0; i < rsd.getColumnCount(); i++) {
        TableInfo.TableColumnInfo tableColumnInfo = new TableInfo.TableColumnInfo();
        tableColumnInfo.setColumnClassName(rsd.getColumnClassName(i + 1));
        tableColumnInfo.setColumnTypeName(rsd.getColumnTypeName(i + 1));
        tableColumnInfo.setColumnName(rsd.getColumnName(i + 1));
        tableColumnInfo.setColumnDisplaySize(rsd.getColumnDisplaySize(i + 1));
        tableColumnInfoList.add(tableColumnInfo);
      }
      tableInfo.setTableColumnInfoList(tableColumnInfoList);
    } catch(SQLException e) {
      throw new RuntimeException(e);
    }
    return tableInfo;
  }

  public static int getTotalNum(Connection con, String tableName
      , TableInfo tableInfo, String keyword) {
    int total = 0;
    Sql sql = new SqlFormat().SELECT("count(1)").FROM(tableName);
    List<String> conditions = createConditions(tableInfo, keyword);
    if (CollectionUtils.isNotEmpty(conditions)) {
      sql.WHERE(Joiner.on(" or ").join(conditions));
    }
    try (PreparedStatement pst = con.prepareStatement(sql.toString())){
      ResultSet rs = pst.executeQuery();
      while(rs.next()) {
        total = rs.getInt("count(1)");
      }
    } catch(SQLException e) {
      throw new RuntimeException(e);
    }
    return total;
  }

  private static List<String> createConditions(TableInfo tableInfo, String keyword) {
    List<String> conditions = Lists.newArrayList();
    if (StringUtils.isNotBlank(keyword)) {
      List<TableInfo.TableColumnInfo> tableColumnInfoList = tableInfo.getTableColumnInfoList();
      for (TableInfo.TableColumnInfo tableColumnInfo : tableColumnInfoList) {
        String className = tableColumnInfo.getColumnClassName();
        String columnName = tableColumnInfo.getColumnName();
        if (DataCheckUtil.isNumber(keyword)) {
          if ("java.lang.Integer".equals(className)
              || "java.lang.Long".equals(className)
              || "java.lang.Double".equals(className)
              || "java.lang.Float".equals(className)) {
            conditions.add(columnName + " = " + keyword);
          }
        } else {
          if ("java.lang.String".equals(className)) {
            conditions.add(columnName + " like '%" + keyword + "%'");
          }
        }
      }
    }
    return conditions;
  }

  public static String createQuerySql(String tableName
      , TableInfo tableInfo, String keyword, Integer pageNum, Integer pageSize) {
    Sql sql = new SqlFormat().SELECT("*").FROM(tableName);
    List<String> conditions = createConditions(tableInfo, keyword);
    if (CollectionUtils.isNotEmpty(conditions)) {
      sql.WHERE(Joiner.on(" or ").join(conditions));
    }
    String sqlStr = sql.toString();
    if (pageNum != null && pageSize != null) {
      sqlStr += String.format(" limit %d, %d", (pageNum - 1) * pageSize, pageSize);
    }
    return sqlStr;
  }

  public static void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void closeResultSet(ResultSet resultSet) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void closeStatement(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
