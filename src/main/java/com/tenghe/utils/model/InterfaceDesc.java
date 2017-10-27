package com.tenghe.utils.model;

/**
 * author: teng.he
 * time: 11:50 2017/9/29
 * desc: 接口说明
 */
public class InterfaceDesc {
  //接口名称
  private String name;
  //注册人
  private String registerPerson;
  //注册单位
  private String registerUnit;
  //接口标签
  private String interfaceTag;
  //创建时间
  private String createdTime;
  //公开方式
  private String openAuth;
  //输入参数
  private String input;
  //输出参数
  private String output;
  //接口说明
  private String interfaceExplain;

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

  public String getInterfaceTag() {
    return interfaceTag;
  }

  public void setInterfaceTag(String interfaceTag) {
    this.interfaceTag = interfaceTag;
  }

  public String getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(String createdTime) {
    this.createdTime = createdTime;
  }

  public String getOpenAuth() {
    return openAuth;
  }

  public void setOpenAuth(String openAuth) {
    this.openAuth = openAuth;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public String getInterfaceExplain() {
    return interfaceExplain;
  }

  public void setInterfaceExplain(String interfaceExplain) {
    this.interfaceExplain = interfaceExplain;
  }

  @Override
  public String toString() {
    return "InterfaceDesc{" +
        "name='" + name + '\'' +
        ", registerPerson='" + registerPerson + '\'' +
        ", registerUnit='" + registerUnit + '\'' +
        ", interfaceTag='" + interfaceTag + '\'' +
        ", createdTime='" + createdTime + '\'' +
        ", openAuth='" + openAuth + '\'' +
        ", input='" + input + '\'' +
        ", output='" + output + '\'' +
        ", interfaceExplain='" + interfaceExplain + '\'' +
        '}';
  }
}
