package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StockTable {
  public StockTable() {

  }

  public TableView<Stock> createStockTable() {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

		TableColumn<Stock, String> symbolColumn = new TableColumn<>("Symbol");
		symbolColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getSymbol()));

		TableColumn<Stock, String> companyColumn = new TableColumn<>("Company");
		companyColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getCompany()));

		TableColumn<Stock, String> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(data ->
				new SimpleStringProperty(currencyFormat.format(data.getValue().getSalesPrice())));

		symbolColumn.setPrefWidth(120);
		companyColumn.setPrefWidth(540);
		priceColumn.setPrefWidth(160);

		TableView<Stock> stockTable = new TableView<>();
		stockTable.getColumns().add(symbolColumn);
		stockTable.getColumns().add(companyColumn);
		stockTable.getColumns().add(priceColumn);
		stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

		return stockTable;
	}
}
