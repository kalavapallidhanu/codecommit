package com.impactsure.artnook.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.impactsure.artnook.dto.CustomerData;
 

 
public class ExcelUtils {
 
  public static ByteArrayInputStream customersToExcel(List<CustomerData> employees) throws IOException {
    String[] COLUMNs = {"Id", "First Name", "Middle Name","Last Name", "Last Login Time","Gender","DOB","Blood Group","Mobile Number","Mail Id","Class","Division","Branch","User Group"};
    try(
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    ){
      CreationHelper createHelper = workbook.getCreationHelper();
   
      Sheet sheet = workbook.createSheet("School_Details");
   
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setColor(IndexedColors.BLUE.getIndex());
   
      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFont(headerFont);
         // Row for Header
      Row headerRow = sheet.createRow(0);
   
      // Header
      for (int col = 0; col < COLUMNs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(COLUMNs[col]);
        cell.setCellStyle(headerCellStyle);
      }
   
      // CellStyle for Age
      CellStyle ageCellStyle = workbook.createCellStyle();
      ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
   
      int rowIdx = 1;
      for (CustomerData emp : employees) {
        Row row = sheet.createRow(rowIdx++);
   
        row.createCell(0).setCellValue(emp.getId());
        row.createCell(1).setCellValue(emp.getFirstName());
        row.createCell(2).setCellValue(emp.getMiddleName());
        row.createCell(3).setCellValue(emp.getLastName());
        row.createCell(4).setCellValue(emp.getLoginTime());
        row.createCell(5).setCellValue(emp.getGender()); 
        row.createCell(6).setCellValue(emp.getDob());
        row.createCell(7).setCellValue(emp.getBloodGroup());
        row.createCell(8).setCellValue(emp.getMobileNumber());
        row.createCell(9).setCellValue(emp.getMailId());
        row.createCell(10).setCellValue(emp.getClassName());
        row.createCell(11).setCellValue(emp.getDivision());
        row.createCell(12).setCellValue(emp.getBranch());
        row.createCell(13).setCellValue(emp.getUserGroup());
//        row.createCell(14).setCellValue(emp.getStatus());
       
        
   
      }
   
      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }
  

}