package com.tenghe.utils.model;

import java.util.List;

/**
 * author: teng.he
 * time: 17:34 2017/10/19
 * desc:
 */
public class TableInfo {

  private String name;
  private List<TableColumnInfo> tableColumnInfoList;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<TableColumnInfo> getTableColumnInfoList() {
    return tableColumnInfoList;
  }

  public void setTableColumnInfoList(List<TableColumnInfo> tableColumnInfoList) {
    this.tableColumnInfoList = tableColumnInfoList;
  }

  public static class TableColumnInfo {
    private String columnClassName;
    private String columnTypeName;
    private String columnName;
    private Integer columnDisplaySize;

    public String getColumnClassName() {
      return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
      this.columnClassName = columnClassName;
    }

    public String getColumnTypeName() {
      return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
      this.columnTypeName = columnTypeName;
    }

    public String getColumnName() {
      return columnName;
    }

    public void setColumnName(String columnName) {
      this.columnName = columnName;
    }

    public Integer getColumnDisplaySize() {
      return columnDisplaySize;
    }

    public void setColumnDisplaySize(Integer columnDisplaySize) {
      this.columnDisplaySize = columnDisplaySize;
    }
  }
}

