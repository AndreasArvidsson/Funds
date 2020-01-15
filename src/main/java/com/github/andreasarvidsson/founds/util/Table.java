package com.github.andreasarvidsson.founds.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class Table {

    private final List<Row> rows = new ArrayList();
    private final List<Integer> colWidths = new ArrayList();

    public void reset() {
        rows.clear();
        colWidths.clear();
    }

    public Table addRow(final List<String> cells) {
        rows.add(new Row(cells));
        updateColWidths(cells);
        return this;
    }

    public Table addRow(final String... row) {
        return addRow(Arrays.asList(row));
    }

    public Table addHR() {
        rows.add(new Row(true));
        return this;
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

    private void updateColWidths(final List<String> cells) {
        while (colWidths.size() < cells.size()) {
            colWidths.add(0);
        }
        for (int i = 0; i < cells.size(); ++i) {
            colWidths.set(i,
                    Math.max(colWidths.get(i),
                            cells.get(i).length()
                    )
            );
        }
    }

    private int getColWidth(final int index, final int size) {
        return colWidths.get(index) + (index < size - 1 ? 3 : 0);
    }

    private void pad(final StringBuilder sb, final int colWidth, final char character) {
        for (int i = 0; i < colWidth; ++i) {
            sb.append(character);
        }
    }

}

class Row {

    public final List<String> cells;
    public final boolean hr;

    public Row(final List<String> cells) {
        this.cells = new ArrayList(cells);
        this.hr = false;
    }

    public Row(final boolean hr) {
        this.cells = null;
        this.hr = hr;
    }

}
