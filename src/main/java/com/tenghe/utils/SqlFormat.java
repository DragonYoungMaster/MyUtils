package com.tenghe.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class SqlFormat extends Sql {
  private static final String  DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

  public static String insertIntoByValues(String table, String [] fields, Object[] values) {
    return insertInto(table, fields, values, null);
  }

  public static String insertIntoByEntity(String table, String [] fields, Object entity, String[] properties) {
    return insertInto(table, fields, properties, entity);
  }

  private static String insertInto(String table, String [] fields, Object[] propertiesOrValues,
      Object entity) {
    if(fields.length != propertiesOrValues.length) {
      throw new IllegalArgumentException("fields parameter length does not match with properties or values");
    }
    Sql sql = new Sql().INSERT_INTO(table);
    for (int i = 0; i < fields.length; i++) {
      Object value = propertiesOrValues[i];
      if (entity != null) {
        value = getProperty(entity, propertiesOrValues[i].toString());
      }
      if(value != null) {
        sql.VALUES(extractFieldName(fields[i]), format(value));
      }
    }
    return sql.toString();
  }

  public static String updateEntityWhere(String table, String [] fields, Object entity,
      String[] properties, String conditions) {
    if(fields.length != properties.length) {
      throw new IllegalArgumentException("fields parameter length does not match with properties or values");
    }
    Sql sql = new SqlFormat().UPDATE(table);
    for (int i = 0; i < fields.length; i++) {
      Object value = getProperty(entity, properties[i]);
      if(value != null) {
        sql.SET(extractFieldName(fields[i]) + "=" + format(value));
      }
    }
    return sql.WHERE(conditions).toString();
  }

  public SqlFormat selectFrom(String table, String fieldString) {
    SELECT(fieldString).FROM(table);
    return this;
  }

  public SqlFormat selectFrom(String table, String [] fields) {
    SELECT(joinFields(fields)).FROM(table);
    return this;
  }

  private static String extractFieldName(String fullField) {
    return fullField.split("\\s")[0];
  }

  public static String extractFieldAlias(String fullField) {
    String [] values = fullField.split("\\s");
    return values[values.length - 1];
  }

  private static String joinFields(String[] fields) {
    if(fields == null) {
      return "*";
    }
    return StringUtils.join(fields, ", ");
  }

  public SqlFormat whereIn(String field, Iterable<String> values) {
    whereIn(field, values, false);
    return this;
  }

  public SqlFormat whereNotIn(String field, Iterable<String> values) {
    whereIn(field, values, true);
    return this;
  }

  private SqlFormat whereIn(String field, Iterable<String> values, boolean notIn) {
    String notInStr = notIn? " not " : " ";
    Iterator it = values.iterator();
    StringBuilder sb = new StringBuilder(" " +field + notInStr + "in (");
    sb.append("'").append(it.next()).append("'");
    while (it.hasNext()) {
      sb.append(", '").append(it.next()).append("'");
    }
    sb.append(")");
    WHERE(sb.toString());
    return this;

  }

  private static String format(Object parameter) {
    if(parameter == null) {
      return "NULL";
    }
    if(parameter instanceof String || parameter instanceof List) {
      return "'" + parameter.toString() + "'";
    }
    if(parameter instanceof java.util.Date) {
      DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
      return "str_to_date('" + sdf.format(parameter) + "', '%Y-%m-%d %T')";
    }
    if(parameter instanceof Enum) {
      return "'" + ((Enum)parameter).name() +"'";
    }
    return parameter.toString();
  }

  private static Object getProperty(Object object, String property) {
    Class<?> clszz = object.getClass();
    String name = StringUtils.capitalize(property);
    Method method = ReflectionUtils.findMethod(clszz,"get" + name);
    if(method == null) {
      method = ReflectionUtils.findMethod(clszz,"is" + name);
    }
    if(method != null) {
      return ReflectionUtils.invokeMethod(method, object);
    }
    Field field = ReflectionUtils.findField(clszz, property);
    if (field != null) {
      field.setAccessible(true);
      return ReflectionUtils.getField(field, object);
    }
    return null;
  }
}
