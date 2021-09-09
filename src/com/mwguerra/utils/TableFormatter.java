package com.mwguerra.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableFormatter {
  private String cellFormat;
  private boolean alignLeft = false;
  private boolean fitContents = false;
  private List<String[]> tableRows = new ArrayList<>();

  public TableFormatter fitContents() {
    fitContents = true;
    return this;
  }

  public TableFormatter alignLeft() {
    alignLeft = true;
    return this;
  }

  public TableFormatter alignRight() {
    alignLeft = false;
    return this;
  }

  public TableFormatter addHeader(String[] headerTitles) {
    tableRows.add(headerTitles);
    return this;
  }

  private void handleDefaultWidth(String[] rowData) {
    int totalTitles = rowData.length;
    int dataWidth = 8;
    int maxTotalWidth = 80;
    int currentWidth = 0;
    cellFormat = "";
    String alignLeftMarker = alignLeft ? "-" : "";
    boolean isTheFirst = true;
    for (String title: rowData) {
      if (!isTheFirst) {
        int headerWidth = Math.max(title.length(), dataWidth);
        cellFormat = cellFormat.concat(" %" + alignLeftMarker + headerWidth + "s");
        currentWidth += headerWidth;
      }
      isTheFirst = false;
    }
    int remainingWidth = maxTotalWidth - currentWidth;
    remainingWidth = Math.max(remainingWidth, dataWidth);
    cellFormat = "%" + alignLeftMarker + remainingWidth + "s" + cellFormat;
  }

  public TableFormatter addRow(String[] rowData) {
    tableRows.add(rowData);
    return this;
  }

  public TableFormatter addAll(List<List<String>> table) {
    for (List<String> row : table) {
      this.addRow(row.toArray(new String[0]));
    }
    return this;
  }

  private void handleFitContents(String[] rowData) {
    if (fitContents) {
      int index = 0;
      for (String item: rowData) {
        String[] cellFormatItems = cellFormat.split(" ");

        if (item == null) {
          item = "";
        }

        int contentLength = Math.max(
            Integer.parseInt(
              cellFormatItems[index]
                .replace("%", "")
                .replace("-", "")
                .replace("s", "")
            ),
            item.length()
        );
        String alignLeftMarker = alignLeft ? "-" : "";
        String length = "%" + alignLeftMarker + contentLength + "s";
        cellFormatItems[index] = length;
        cellFormat = String.join(" ", cellFormatItems);
        index++;
      }
    }
  }

  public TableFormatter build() {
    setTableCellWidth();

    for (String[] row: tableRows) {
      System.out.printf((cellFormat) + "%n", row);
    }

    return this;
  }

  private void setTableCellWidth() {
    boolean isFirst = true;
    for (String[] row: tableRows) {
      if (isFirst) {
        if (fitContents) {
          setMinimumContentWidth(row, 2);
          handleFitContents(row);
        } else {
          handleDefaultWidth(row);
        }
      }
      handleFitContents(row);
      // System.out.println(cellFormat);
      isFirst = false;
    }
  }

  private void setMinimumContentWidth(String[] row, int contentLength) {
    List<String> cellFormatItems = new ArrayList<>();
    for (String item: row) {
      String alignLeftMarker = alignLeft ? "-" : "";
      String length = "%" + alignLeftMarker + contentLength + "s";
      cellFormatItems.add(length);
    }
    cellFormat = String.join(" ", cellFormatItems);
  }

  // Para testes
  // public static void main(String[] args) {
  //   TableFormatter table = new TableFormatter();
  //
  //   String[] headers = {"", "Teste 2", "Teste 3", "Teste 4", "Teste 5", "dd/mm/yy", "dd/mm/yy"};
  //   String[] data1 = {"Marcelo Guerra", "Ausente", "Teste 3", "Teste 4", "Presente", "dd/mm/yy", "dd/mm/yy"};
  //   String[] data2 = {"Mentorama", "Teste 2", "Presente", "Teste 4", "Ausente", "dd/mm/yy", "Opa"};
  //
  //   table
  //       .fitContents()
  //       .alignLeft()
  //       .addHeader(headers)
  //       .addRow(data1)
  //       .addRow(data2)
  //       .build();
  // }
}
