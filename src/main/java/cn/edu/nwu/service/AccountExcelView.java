package cn.edu.nwu.service;

import cn.edu.nwu.domain.Log;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class AccountExcelView extends AbstractXlsView {
    String typeFilter(int type) {

        if(type==0) {
            return "存款";
        }
        if(type==1) {
            return "取款";
        }
        if(type==2) {
            return "转出";
        }
        if(type==3) {
            return "转入";
        }
        return "";
    }
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"account_logs.xls\"");

        List<Log> logs = (List<Log>)map.get("logs");
        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Account Log");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("记录ID");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("类型");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("金额");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("对方卡号");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("操作时间");
        header.getCell(4).setCellStyle(style);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int rowCount = 1;
        for(Log log : logs){
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(log.getId());
            userRow.createCell(1).setCellValue(typeFilter(log.getType()));
            userRow.createCell(2).setCellValue(log.getAmountStr());
            userRow.createCell(3).setCellValue(log.getOppsitecard());
            userRow.createCell(4).setCellValue(sdf.format(log.getTime()));

        }

    }
}
