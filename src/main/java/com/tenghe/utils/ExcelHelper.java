package com.tenghe.utils;


import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import com.tenghe.utils.common.Constants;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public final class ExcelHelper {

  private static final int ROW_HEIGHT = 40;
  private static final int TWO = 2;
  private static final int THIRTEEN = 13;

  private ExcelHelper() {
  }

  public static String[][] readExcel(InputStream inputStream) {
    return readExcel(inputStream, 0, null);
  }

  public static String[][] readExcel(InputStream inputStream, int startRow, int... excludeCols) {
    List<List<String>> rowList = readExcelToList(inputStream, startRow, excludeCols);
    return convertArrays(rowList);
  }

  public static List<List<String>> readExcelToList(InputStream inputStream, int startRow, int... excludeCols) {
    try (Workbook wb = WorkbookFactory.create(inputStream)) {
      return readExcel(wb, startRow, excludeCols);
    } catch (IOException | InvalidFormatException e) {
      throw new RuntimeException(e);
    }
  }

  public static String[][] readExcel(File excelFile) {
    return readExcel(excelFile, 0, null);
  }

  public static String[][] readExcel(File excelFile, int startRow, int... excludeCols) {
    List<List<String>> rowList = readExcelToList(excelFile, startRow, excludeCols);
    return convertArrays(rowList);
  }

  public static List<List<String>> readExcelToList(File excelFile, int startRow, int... excludeCols) {
    try (Workbook wb = WorkbookFactory.create(excelFile)) {
      return readExcel(wb, startRow, excludeCols);
    } catch (IOException | InvalidFormatException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<List<String>> readExcel(Workbook wb, int startRow, int[] excludeCols) {
    List<List<String>> rowList = new ArrayList<>();
    Sheet sheet = wb.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.iterator();
    int rows = 0;
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      if(rows++ < startRow) {
        continue;
      }
      Iterator<Cell> cellIterator = row.cellIterator();
      List<String> values = new ArrayList<>();
      int cols = 0;
      int blankCount = 0;
      while (cellIterator.hasNext()) {
        Cell cell = cellIterator.next();
        if (ArrayUtils.contains(excludeCols, cols++)) {
          continue;
        }
        String value = getCellValue(cell);
        if(StringUtils.isBlank(value)) {
          blankCount++;
        }
        values.add(value);
      }
      if(blankCount < values.size()) {
        rowList.add(values);
      }
    }
    return rowList;
  }

  private static String[][] convertArrays(List<List<String>> rowList) {
    String [][] result = new String[rowList.size()][];
    for(int i = 0; i < rowList.size(); i++) {
      List<String> values = rowList.get(i);
      result[i] = new String[values.size()];
      for(int j = 0; j < values.size(); j++) {
        result[i][j] = values.get(j);
      }
    }
    return result;
  }

  private static String getCellValue(Cell cell) {
    String value = "";
    switch (cell.getCellTypeEnum()) {
      case STRING:
        value = cell.getRichStringCellValue().getString().trim();
        break;
      case NUMERIC:
        value = readNumberValue(cell);
        break;
      case BOOLEAN:
        value = String.valueOf(cell.getBooleanCellValue());
        break;
      case FORMULA:
        value = cell.getCellFormula();
        break;
      case BLANK:
      case ERROR:
        break;
      default:
    }
    if (value != null) {
      value = value.trim();
    }
    return value;
  }

  private static String readNumberValue(Cell cell) {
    if (cell.getCellTypeEnum() != CellType.NUMERIC) {
      throw new IllegalArgumentException("Not number type of the cell value");
    }
    String value;
    if (DateUtil.isCellDateFormatted(cell)) {
      Date dateValue = cell.getDateCellValue();
      String dateFmt = cell.getCellStyle().getDataFormatString();
      value = new CellDateFormatter(dateFmt).format(dateValue);
    } else {
      double doubValue = cell.getNumericCellValue();
      value = Double.toString(doubValue);
      String cellValueInString = BigDecimal.valueOf(doubValue).toPlainString();
      String longString = Long.toString((long) doubValue);
      if (cellValueInString.equals(longString)) {
        value = longString;
      }
    }
    return parseDecimalString(value);
  }

  private static String parseDecimalString(String num) {
    if (num == null) {
      return num;
    }
    if (num.endsWith(".0")) {
      int lasDotPos = num.lastIndexOf('.');
      return num.substring(0, lasDotPos);
    }
    return num;
  }

  /**
   * 创建一行（一列）
   * @param sheet
   * @param style
   * @param rowNum
   * @param value
   */
  private static void createSheetRowForOne(HSSFSheet sheet, HSSFCellStyle style, int rowNum, String value) {
    HSSFRow sheetRow = sheet.createRow(rowNum);
    HSSFCell cell0 = sheetRow.createCell(Constants.NUM_ZERO);
    cell0.setCellValue(value);
    cell0.setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, Constants.NUM_ZERO,
        Constants.NUM_THREE));
    HSSFCell cell1 = sheetRow.createCell(Constants.NUM_ONE);
    cell1.setCellStyle(style);
    HSSFCell cell2 = sheetRow.createCell(Constants.NUM_TWO);
    cell2.setCellStyle(style);
    HSSFCell cell3 = sheetRow.createCell(Constants.NUM_THREE);
    cell3.setCellStyle(style);
  }

  /**
   * 创建一行（两列）
   * @param sheet
   * @param style1
   * @param style2
   * @param rowNum
   * @param key
   * @param value
   */
  private static void createSheetRowForTwo(HSSFSheet sheet, HSSFCellStyle style1, HSSFCellStyle style2,
      int rowNum, String key, String value) {
    HSSFRow sheetRow = sheet.createRow(rowNum);
    HSSFCell cell0 = sheetRow.createCell(Constants.NUM_ZERO);
    cell0.setCellValue(key);
    cell0.setCellStyle(style1);
    HSSFCell cell1 = sheetRow.createCell(Constants.NUM_ONE);
    cell1.setCellValue(value);
    cell1.setCellStyle(style2);
    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, Constants.NUM_ONE, Constants
        .NUM_THREE));
    HSSFCell cell2 = sheetRow.createCell(Constants.NUM_TWO);
    cell2.setCellStyle(style2);
    HSSFCell cell3 = sheetRow.createCell(Constants.NUM_THREE);
    cell3.setCellStyle(style2);
  }

  /**
   * 创建一行（4列）
   * @param sheet
   * @param cellStyleRight
   * @param cellStyleLeft
   * @param rowNum
   * @param cell4Values
   */
  private static void createSheetRowForFour(HSSFSheet sheet, HSSFCellStyle cellStyleRight,
      HSSFCellStyle cellStyleLeft, int rowNum , String [] cell4Values) {
    HSSFRow sheetRow = sheet.createRow(rowNum);
    for (int i = 0; i < cell4Values.length; i++) {
      HSSFCell cell = sheetRow.createCell(i);
      cell.setCellValue(cell4Values[i]);
      cell.setCellStyle(i % Constants.NUM_TWO == 0 ? cellStyleRight : cellStyleLeft);
    }
  }

  private static HSSFFont createFont(HSSFWorkbook workbook) {
    HSSFFont font = workbook.createFont();//创建字体样式
    font.setFontName("宋体");//使用宋体
    font.setFontHeightInPoints((short) Constants.NUM_TEN);//字体大小
    return font;
  }

  private static HSSFFont createBoldFont(HSSFWorkbook workbook) {
    HSSFFont font = workbook.createFont();//创建字体样式
    font.setFontName("宋体");//使用宋体
    font.setBold(true);
    font.setFontHeightInPoints((short) Constants.NUM_TEN);//字体大小
    return font;
  }

  private static HSSFCellStyle createCellStyleLeft(HSSFWorkbook workbook, HSSFFont font) {
    return createCellStyle(workbook, font, HorizontalAlignment.LEFT);
  }

  private static HSSFCellStyle createCellStyleRight(HSSFWorkbook workbook, HSSFFont font) {
    return createCellStyle(workbook, font, HorizontalAlignment.RIGHT);
  }

  private static HSSFCellStyle createCellStyleCenter(HSSFWorkbook workbook, HSSFFont font) {
    return createCellStyle(workbook, font, HorizontalAlignment.CENTER);
  }

  private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, HSSFFont font, HorizontalAlignment cellStyle) {
    HSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);//将字体注入
    style.setWrapText(true);// 自动换行
    style.setAlignment(cellStyle);//对齐方式
    style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    return style;
  }

  private static HSSFSheet createSheet(HSSFWorkbook workbook, String name) {
    HSSFSheet sheet = workbook.createSheet(name);
    sheet.setColumnWidth(Constants.NUM_ZERO, Constants.COLUMN_WIDTH_1);
    sheet.setColumnWidth(Constants.NUM_ONE, Constants.COLUMN_WIDTH_2);
    sheet.setColumnWidth(Constants.NUM_TWO, Constants.COLUMN_WIDTH_2);
    sheet.setColumnWidth(Constants.NUM_THREE, Constants.COLUMN_WIDTH_2);
    return sheet;
  }

  private static String getLabel(List<String> labels) {
    List<String> labelNameList = Lists.newArrayList();
    for (String label : labels) {
      labelNameList.add(label);
    }
    return Joiner.on("、").join(labelNameList);
  }

  private static HSSFSheet createSheet(HSSFWorkbook workbook, String name, int columnSize) {
    HSSFSheet sheet = workbook.createSheet(name);
    for (int i = 0; i < columnSize; i++) {
      sheet.setColumnWidth(i, Constants.COLUMN_WIDTH_2);
    }
    return sheet;
  }

  private static void writeOneRowOfDataCheckReport(HSSFSheet sheet, int rowNum, String value,
      HSSFCellStyle cellStyle, int columnSize) {
    HSSFRow sheetRow = sheet.createRow(rowNum);
    HSSFCell cell0 = sheetRow.createCell(0);
    cell0.setCellValue(value);
    cell0.setCellStyle(cellStyle);
    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, columnSize-1));
    for (int i = 1; i <= columnSize; i ++) {
      HSSFCell cell1 = sheetRow.createCell(i);
      cell1.setCellStyle(cellStyle);
    }
  }

  private static void writeSummaryOfDataCheckReport(HSSFSheet sheet, int rowNum, String value,
      HSSFCellStyle cellStyle, int columnSize) {
    HSSFRow sheetRow = sheet.createRow(rowNum);
    sheetRow.setHeightInPoints(ROW_HEIGHT);
    HSSFCell cell0 = sheetRow.createCell(0);
    cell0.setCellValue(value);
    cell0.setCellStyle(cellStyle);
    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, columnSize-1));
    for (int i = 1; i <= columnSize; i ++) {
      HSSFCell cell1 = sheetRow.createCell(i);
      cell1.setCellStyle(cellStyle);
    }
  }

  private static void insertPic(HSSFWorkbook wb, HSSFSheet sheet, int row2, byte[] bytes) {
    //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    //anchor主要用于设置图片的属性
    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 100,
        (short) 1, TWO, (short)TWO, row2);
    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_DO_RESIZE);
    //插入图片
    patriarch.createPicture(anchor, wb.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
  }

  public static void main(String[] args) throws IOException{
    List<List<String>> rows = readExcelToList(new File("D:\\信息安全及规章(6-5).xls"), 1, null);
    OutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream("D:\\信息安全及规章(6-5).txt");
      for (List<String> row : rows) {
        int index = 0;
        for (String item : row) {
          index++;
          if (1==index || 2==index || 3 ==index) {
            outputStream.write((item + "\n").getBytes());
          } else if (index % 2 == 0){
            outputStream.write((item + " ").getBytes());
          } else {
            outputStream.write((item + "\t\t").getBytes());
          }
        }
        outputStream.write("\n\n".getBytes());
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }
  }


}


