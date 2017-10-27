package com.tenghe.utils.model;

/**
 * author: teng.he
 * time: 16:31 2017/9/5
 * desc: 数据资源说明
 */
public class DataResourceDesc {
  //名称
  private String name;
  //注册人
  private String registerPerson;
  //注册单位
  private String registerUnit;
  //数据注册时间
  private String dataRegisterTime;
  //数据权限
  private String dataAuth;
  //数据字段
  private String dataItem;
  //数据来源
  private String dataSource;
  //有效时间范围
  private String validTimeRange;
  //数据说明
  private String dataExplain;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegisterPerson() {
    return registerPerson;
  }

  public void setRegisterPerson(String registerPerson) {
    this.registerPerson = registerPerson;
  }

  public String getRegisterUnit() {
    return registerUnit;
  }

  public void setRegisterUnit(String registerUnit) {
    this.registerUnit = registerUnit;
  }

  public String getDataRegisterTime() {
    return dataRegisterTime;
  }

  public void setDataRegisterTime(String dataRegisterTime) {
    this.dataRegisterTime = dataRegisterTime;
  }

  public String getDataAuth() {
    return dataAuth;
  }

  public void setDataAuth(String dataAuth) {
    this.dataAuth = dataAuth;
  }

  public String getDataItem() {
    return dataItem;
  }

  public void setDataItem(String dataItem) {
    this.dataItem = dataItem;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public String getValidTimeRange() {
    return validTimeRange;
  }

  public void setValidTimeRange(String validTimeRange) {
    this.validTimeRange = validTimeRange;
  }

  public String getDataExplain() {
    return dataExplain;
  }

  public void setDataExplain(String dataExplain) {
    this.dataExplain = dataExplain;
  }

  @Override
  public String toString() {
    return "DataResourceDesc{" +
        "name='" + name + '\'' +
        ", registerPerson='" + registerPerson + '\'' +
        ", registerUnit='" + registerUnit + '\'' +
        ", dataRegisterTime='" + dataRegisterTime + '\'' +
        ", dataAuth='" + dataAuth + '\'' +
        ", dataItem='" + dataItem + '\'' +
        ", dataSource='" + dataSource + '\'' +
        ", validTimeRange='" + validTimeRange + '\'' +
        ", dataExplain='" + dataExplain + '\'' +
        '}' ;
  }
}
