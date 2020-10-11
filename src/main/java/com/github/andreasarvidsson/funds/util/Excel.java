package com.github.andreasarvidsson.funds.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Andreas Arvidsson
 */
public class Excel {

    private final Workbook wb = new HSSFWorkbook();
    private final CellStyle boldStyle;
    private final CellStyle hrStyle;

    public Excel() {
        final Font font = wb.createFont();
        font.setBold(true);
        boldStyle = wb.createCellStyle();
        boldStyle.setFont(font);
        boldStyle.setVerticalAlignment(VerticalAlignment.TOP);
        hrStyle = wb.createCellStyle();
        hrStyle.setBorderBottom(BorderStyle.THIN);
    }

    public void save(final File file) throws FileNotFoundException, IOException {
        file.getParentFile().mkdirs();
        wb.write(new FileOutputStream(file));
    }

    public boolean hasTable(final String name) {
        return wb.getSheet(name) != null;
    }

    public ExcelTable getTable(final String name) {
        Sheet sheet = wb.getSheet(name);
        if (sheet == null) {
            sheet = wb.createSheet(name);
        }
        return new ExcelTable(sheet, boldStyle, hrStyle);
    }

    public static class ExcelTable {

        private final Sheet sheet;
        final CellStyle boldStyle, hrStyle;

        private ExcelTable(
                final Sheet sheet,
                final CellStyle boldStyle,
                final CellStyle hrStyle) {
            this.sheet = sheet;
            this.boldStyle = boldStyle;
            this.hrStyle = hrStyle;
        }

        public void addRow() {
            addRow(Arrays.asList());
        }

        public void addRow(final List<String> cells) {
            final Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            for (int i = 0; i < cells.size(); ++i) {
                final Cell cell = row.createCell(i);
                cell.setCellValue(cells.get(i));
            }
        }

        public void addTitle(final String title) {
            addRow(Arrays.asList(title));
            final int rowIndex = sheet.getPhysicalNumberOfRows() - 1;
            final Row row = sheet.getRow(rowIndex);
            for (int i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
                row.getCell(i).setCellStyle(boldStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
        }

        public void addHR() {
            final Row row = sheet.getRow(sheet.getPhysicalNumberOfRows() - 1);
            for (int i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
                row.getCell(i).setCellStyle(hrStyle);
            }
        }

        public void addRows(final List<List<String>> rows) {
            rows.forEach(row -> {
                addRow(row);
            });
        }

        public void autoSizeColumns(final int numColums) {
            for (int i = 0; i < numColums; ++i) {
                sheet.autoSizeColumn(i);
            }
        }

    }

}
