package org.springframework.samples.petclinic.web;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.web.abstractview.AbstractExcelView;

public class VetExcelView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        Sheet sheet = workbook.createSheet("Vet List");

        @SuppressWarnings("unchecked")
        Vets vets = (Vets) model.get("vets");
        List<Vet> vetList = vets.getVetList();

        Row row = null;
        Cell cell = null;
        int r = 0;
        int c = 0;

        //Style for header cell
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);

        //Create header cells
        row = sheet.createRow(r++);

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("Name");

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("Specialities");

        //Create data cell
        for (Vet vet : vetList) {
            row = sheet.createRow(r++);
            c = 0;
            row.createCell(c++).setCellValue(vet.getFirstName() + " " + vet.getLastName());
            row.createCell(c++).setCellValue(vet.getSpecialties().toString());
        }
        for (int i = 0; i < 2; i++)
            sheet.autoSizeColumn(i, true);

    }

}
