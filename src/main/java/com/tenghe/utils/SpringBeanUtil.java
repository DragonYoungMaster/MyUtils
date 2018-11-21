package com.tenghe.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by zhengzhigang on 2017/7/8.
 */

@Configuration
public class SpringBeanUtil implements ApplicationContextAware {

  private static ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext beanFactory) {
    SpringBeanUtil.context = beanFactory;
  }

  public static Object getBean(String beanName) {
    return context.getBean(beanName);
  }

  public static <T> T getBean(Class<T> klass) {
    return context.getBean(klass);
  }

  public static <T> T getBean(String beanName, Class<T> klass) {
    return context.getBean(beanName, klass);
  }

  public static <T> Map<String, T> getBeans(Class<T> klass) {
    return context.getBeansOfType(klass);
  }

  public static <T> Map<String, T> getBeans(Class<T> klass, boolean includeNonSingletons) {
    return context.getBeansOfType(klass, includeNonSingletons, true);
  }

  public static Map<String, Object> getBeansWithAnnotation(
      Class<? extends Annotation> annotationType) {
    return context.getBeansWithAnnotation(annotationType);
  }
}
