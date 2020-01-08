package com.github.andreasarvidsson.founds;

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
                for (int i = 0; i < colWidths.size(); i++) {
                    final int colWidth = getCellWidth(i, colWidths.size());
                    pad(sb, colWidth, '-');
                }
            }
            else if (row.cells != null) {
                for (int i = 0; i < row.cells.size(); i++) {
                    final int colWidth = getCellWidth(i, row.cells.size());
                    final String cell = row.cells.get(i);
                    sb.append(cell);
                    pad(sb, colWidth - cell.length(), ' ');
                }
            }
            sb.append(System.lineSeparator());
        });
        return sb.toString();
    }

    private int getCellWidth(final int index, final int size) {
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
        this.cells = cells;
        this.hr = false;
    }

    public Row(final boolean hr) {
        this.cells = null;
        this.hr = hr;
    }

}
