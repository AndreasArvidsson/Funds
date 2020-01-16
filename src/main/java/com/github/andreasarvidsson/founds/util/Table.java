package com.github.andreasarvidsson.founds.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class Table {

    private final static int CELL_SPACING = 3;
    private final List<Row> rows = new ArrayList();
    private final List<Integer> colWidths = new ArrayList();

    public void reset() {
        rows.clear();
        colWidths.clear();
    }

    public Table addRow(final List<String> cells) {
        return addRow(rows.size(), cells);
    }

    public Table addRow(final String... cells) {
        return addRow(rows.size(), Arrays.asList(cells));
    }

    public Table addRow(final int rowIndex, final String... cells) {
        return addRow(rowIndex, Arrays.asList(cells));
    }

    public Table addRow(final int rowIndex, final List<String> cells) {
        while (rows.size() <= rowIndex) {
            rows.add(new Row());
        }
        int colIndex = rows.get(rowIndex).cells.size();
        rows.get(rowIndex).cells.addAll(cells);
        updateColWidths(colIndex, rows.get(rowIndex).cells);
        return this;
    }

    public Table addHR() {
        rows.add(new Row(true));
        return this;
    }

    public int numRows() {
        return rows.size();
    }

    public void print() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        rows.forEach(row -> {
            if (row.hr) {
                addHR(sb);
            }
            else if (row.cells != null) {
                for (int i = 0; i < row.cells.size(); i++) {
                    addCell(sb, row.cells.get(i), getColWidth(i, row.cells.size()));
                }
                sb.append(System.lineSeparator());
            }
        });
        return sb.toString();
    }

    private void addCell(final StringBuilder sb, final String cell, final int colWidth) {
        sb.append(cell);
        pad(sb, colWidth - cell.length(), ' ');
    }

    private void addHR(final StringBuilder sb) {
        for (int i = 0; i < colWidths.size(); i++) {
            final int colWidth = getColWidth(i, colWidths.size());
            pad(sb, colWidth, '-');
        }
        sb.append(System.lineSeparator());
    }

    private void updateColWidths(int colIndex, final List<String> cells) {
        while (colWidths.size() < cells.size()) {
            colWidths.add(0);
        }
        while (colIndex < cells.size()) {
            colWidths.set(colIndex,
                    Math.max(colWidths.get(colIndex),
                            cells.get(colIndex).length()
                    )
            );
            ++colIndex;
        }
    }

    private int getColWidth(final int index, final int size) {
        return colWidths.get(index) + (index < size - 1 ? CELL_SPACING : 0);
    }

    private void pad(final StringBuilder sb, int colWidth, final char character) {
        while (colWidth > 0) {
            sb.append(character);
            --colWidth;
        }
    }

}

class Row {

    public final List<String> cells;
    public final boolean hr;

    public Row() {
        this.cells = new ArrayList();
        this.hr = false;
    }

    public Row(final boolean hr) {
        this.cells = null;
        this.hr = hr;
    }

}
