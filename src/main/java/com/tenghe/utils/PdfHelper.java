package com.tenghe.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.tenghe.utils.model.DataResourceDesc;
import com.tenghe.utils.model.InterfaceDesc;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author: teng.he
 * time: 9:51 2017/9/5
 * desc: PDF辅助类
 */
@Component
public final class PdfHelper {
  private static final Logger LOGGER = Logger.getLogger(PdfHelper.class);
  private Document document = new Document();
  private static Font titleFont;
  private static Font keyFont;
  private static Font textFont;
  private static final int MAX_WIDTH = 520;
  private static final int THREE = 3;
  private static final int FOUR = 4;
  private static final int EIGHT = 8;
  private static final int TEN = 10;
  private static final float THREE_F = 3.0F;
  private static final float EIGHT_F = 8.0F;
  private static final float FIFTEEN_F = 15.0F;
  private static final String REGISTER_PERSON = "注册人";
  private static final String REGISTER_UNIT = "注册单位";
  private static final String DATA_REGISTER_TIME = "数据注册时间";
  private static final String DATA_AUTH = "数据权限";
  private static final String DATA_ITEM = "数据字段";
  private static final String DATA_SOURCE = "数据来源";
  private static final String VALID_TIME_RANGE = "使用期限";
  private static final String DATA_EXPLAIN = "数据说明";
  private static final String INTERFACE_TAG = "接口标签";
  private static final String CREATED_TIME = "创建时间";
  private static final String OPEN_AUTH = "公开方式";
  private static final String INPUT = "输入参数";
  private static final String OUTPUT = "输出参数";
  private static final String INTERFACE_EXPLAIN = "接口说明";

  static{
    BaseFont bfChinese;
    try {
      bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
      titleFont = new Font(bfChinese, TEN, Font.BOLD, BaseColor.RED);
      keyFont = new Font(bfChinese, EIGHT, Font.BOLD);
      textFont = new Font(bfChinese, EIGHT, Font.NORMAL);
    } catch (DocumentException de) {
      LOGGER.warn("PdfHelper createFont DocumentException", de);
    } catch (IOException e) {
      LOGGER.warn("PdfHelper createFont IOException", e);
    }
  }

  public void initDocument(File file) {
    document.setPageSize(PageSize.A4);
    try {
      PdfWriter.getInstance(document, new FileOutputStream(file));
    } catch (DocumentException de) {
      LOGGER.warn("PdfHelper getInstance DocumentException", de);
    } catch (FileNotFoundException e) {
      LOGGER.warn("PdfHelper getInstance FileNotFoundException", e);
    }
    document.open();
  }

  private static PdfPCell createCell(String value, Font font, int align, int colspan, boolean borderFlag){
    PdfPCell cell = new PdfPCell();
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(align);
    cell.setColspan(colspan);
    cell.setPhrase(new Phrase(value, font));
    cell.setPadding(THREE_F);
    if(!borderFlag){
      cell.setBorder(0);
      cell.setPaddingTop(FIFTEEN_F);
      cell.setPaddingBottom(EIGHT_F);
    }
    return cell;
  }

  private static PdfPTable createTable(int colNumber){
    PdfPTable table = new PdfPTable(colNumber);
    table.setTotalWidth(MAX_WIDTH);
    table.setLockedWidth(true);
    table.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.getDefaultCell().setBorder(0);
    return table;
  }

  private void drawLine() throws DocumentException{
    Paragraph p1 = new Paragraph();
    p1.add(new Chunk(new LineSeparator()));
    document.add(p1);
  }

  private void createTitle(String name) throws DocumentException {
    Paragraph paragraph = new Paragraph("《" + name + "》说明", titleFont);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    document.add(paragraph);
  }

  private void drawTableOfDataResource(DataResourceDesc dataResourceDesc) throws DocumentException{
    PdfPTable table = createTable(FOUR);
    table.addCell(createCell(REGISTER_PERSON, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getRegisterPerson(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(REGISTER_UNIT, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getRegisterUnit(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(DATA_REGISTER_TIME, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getDataRegisterTime(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(DATA_AUTH, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getDataAuth(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(DATA_ITEM, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getDataItem(), textFont
        , Element.ALIGN_LEFT,THREE,false));
    table.addCell(createCell(DATA_SOURCE, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getDataSource(), textFont, Element.ALIGN_LEFT, 1,
        false));
    table.addCell(createCell(VALID_TIME_RANGE, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getValidTimeRange(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(DATA_EXPLAIN, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(dataResourceDesc.getDataExplain(), textFont, Element.ALIGN_LEFT,THREE,false));
    document.add(table);
  }

  private void drawTableOfInterface(InterfaceDesc interfaceDesc) throws DocumentException{
    PdfPTable table = createTable(FOUR);
    table.addCell(createCell(REGISTER_PERSON, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getRegisterPerson(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(REGISTER_UNIT, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getRegisterUnit(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(INTERFACE_TAG, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getInterfaceTag(), textFont, Element.ALIGN_LEFT, 1,false));
    table.addCell(createCell(CREATED_TIME, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getCreatedTime(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(OPEN_AUTH, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getOpenAuth(), textFont
        , Element.ALIGN_LEFT,THREE,false));
    table.addCell(createCell(INPUT, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getInput(), textFont, Element.ALIGN_LEFT, 1,
        false));
    table.addCell(createCell(OUTPUT, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getOutput(), textFont, Element.ALIGN_LEFT, 1, false));
    table.addCell(createCell(INTERFACE_EXPLAIN, keyFont, Element.ALIGN_RIGHT, 1, false));
    table.addCell(createCell(interfaceDesc.getInterfaceExplain(), textFont, Element.ALIGN_LEFT,THREE,false));
    document.add(table);
  }

  public void generatePDFOfDataResource(DataResourceDesc dataResourceDesc) throws DocumentException {
    createTitle(dataResourceDesc.getName());
    drawLine();
    drawTableOfDataResource(dataResourceDesc);
    drawLine();
    document.close();
  }

  public void generatePDFOfInterface(InterfaceDesc interfaceDesc) throws DocumentException {
    createTitle(interfaceDesc.getName());
    drawLine();
    drawTableOfInterface(interfaceDesc);
    drawLine();
    document.close();
  }

  public static void main(String[] args)  {
    File file = new File("D:\\text.pdf");
    try {
      if (!file.createNewFile()) {
        LOGGER.warn("file already exists");
      }
    } catch (IOException e) {
      LOGGER.warn("createNewFile IOException", e);
    }

    try {
      PdfHelper pdfHelper = new PdfHelper();
      pdfHelper.initDocument(file);
      DataResourceDesc dataResourceDesc = new DataResourceDesc();
      dataResourceDesc.setName("在逃人员信息库说明");
      dataResourceDesc.setRegisterPerson("张三");
      dataResourceDesc.setRegisterUnit("公安部");
      dataResourceDesc.setDataRegisterTime("2016-05-05 00:00:00");
      dataResourceDesc.setDataAuth("内网非公开");
      dataResourceDesc.setDataItem("在逃人员编号、姓名、身份证号、性别、户籍、所犯罪行、出逃时间");
      dataResourceDesc.setDataSource("公安部内网");
      dataResourceDesc.setValidTimeRange("2017-08-25至2018-08-25");
      dataResourceDesc.setDataExplain("在逃人员的数据查询");
      pdfHelper.generatePDFOfDataResource(dataResourceDesc);
    } catch (DocumentException e) {
      LOGGER.warn("generatePDFOfDataResource Exception", e);
    }
  }
}
