package com.tenghe.utils;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Id;

/**
 * Created by zhengzhigang on 2018/3/24.
 */
public abstract class BaseEntity implements Entity {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  public BaseEntity() {
  }

  public BaseEntity(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BaseEntity that = (BaseEntity) o;

    if(id == null) {
      return that.id == null;
    }

    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
  }
}
