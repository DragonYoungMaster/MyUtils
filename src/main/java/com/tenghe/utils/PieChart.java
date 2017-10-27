package com.tenghe.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * author: teng.he
 * time: 17:20 2017/9/13
 * desc:
 */
public final class PieChart {
  private static final double LABEL_GAP = 0.02D;
  private static final int FONT_SIZE_24 = 24;
  private static final int FONT_SIZE_16 = 16;
  private static final int WIDTH = 400;
  private static final int HEIGHT = 300;

  private PieChart() {
  }

  private static DefaultPieDataset getDataSet(Map<String, Double> dataMap) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    for(Map.Entry<String, Double> entry : dataMap.entrySet()) {
      dataset.setValue(entry.getKey(), entry.getValue());
    }
    return dataset;
  }

  public static byte[] draw(Map<String, Double> dataMap) {
    DefaultPieDataset data = getDataSet(dataMap);
    JFreeChart chart = ChartFactory.createPieChart("可用性占比",data,true,false,false);
    //设置百分比
    PiePlot pieplot = (PiePlot)chart.getPlot();
    DecimalFormat df = new DecimalFormat("0.00%");//获得一个DecimalFormat对象，主要是设置小数问题
    NumberFormat nf = NumberFormat.getNumberInstance();//获得一个NumberFormat对象
    StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);
    pieplot.setLabelGenerator(sp1);//设置饼图显示百分比

    //没有数据的时候显示的内容
    pieplot.setNoDataMessage("无数据显示");
    pieplot.setCircular(false);
    pieplot.setLabelGap(LABEL_GAP);

    pieplot.setIgnoreNullValues(true);//设置不显示空值
    pieplot.setIgnoreZeroValues(true);//设置不显示负值
    chart.getTitle().setFont(new Font("宋体", Font.PLAIN, FONT_SIZE_24));//设置标题字体
    PiePlot piePlot= (PiePlot)chart.getPlot();//获取图表区域对象
    piePlot.setLabelFont(new Font("宋体", Font.PLAIN, FONT_SIZE_16));//解决乱码
    chart.getLegend().setItemFont(new Font("黑体", Font.PLAIN, FONT_SIZE_16));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ChartUtilities.writeChartAsPNG(baos, chart, WIDTH, HEIGHT);
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
