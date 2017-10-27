package com.tenghe.utils.model;

import org.apache.commons.lang3.StringUtils;

/**
 * author: teng.he
 * time: 10:49 2017/9/4
 * desc: 数据检测规则
 */
public class DataCheckRule {
  private String type;
  private Integer len;
  private Integer min;
  private Integer max;
  private Integer decimal;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getLen() {
    return len;
  }

  public void setLen(String lenStr) {
    this.len = StringUtils.isBlank(lenStr) ? null : Integer.valueOf(lenStr);
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(String minStr) {
    this.min = StringUtils.isBlank(minStr) ? null : Integer.valueOf(minStr);
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(String maxStr) {
    this.max = StringUtils.isBlank(maxStr) ? null : Integer.valueOf(maxStr);
  }

  public Integer getDecimal() {
    return decimal;
  }

  public void setDecimal(String decimalStr) {
    this.decimal = StringUtils.isBlank(decimalStr) ? null : Integer.valueOf(decimalStr);
  }

  @Override
  public String toString() {
    return "DataCheckRule{" +
        "type='" + type + '\'' +
        ", len=" + len +
        ", min=" + min +
        ", max=" + max +
        ", decimal=" + decimal +
        '}' ;
  }
}
