package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Sale;
import fr.valentinle.logiciel_coiffure.model.TypeSale;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class SaleListCell extends ListCell<Sale> {

    private Dashboard dashboard;
    
    // la liste que contient la listView
    private List<Sale> list;
    
    private boolean showQuantity;
    
    @FXML
    private Label quantity;
    
    @FXML
    private Label name;
    
    @FXML
    private Label type;
    
    @FXML
    private Label price;
    
    @FXML
    private GridPane gridpane;
    
    public SaleListCell(Dashboard dashboard, List<Sale> list, boolean showQuantity) {
	loadFXML();
	this.dashboard = dashboard;
	this.list = list;
	this.showQuantity = showQuantity;
    }

    public List<Sale> getSalesList() {
	return this.list;
    }

    /**
     * Charge la structure d'une cellule representant un client
     */
    private void loadFXML() {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/SaleCell.fxml"));
	    loader.setController(this);
	    loader.load();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    protected void updateItem(Sale sale, boolean empty) {
	super.updateItem(sale, empty);
	if (!empty) {
	    if (this.showQuantity && sale.getType() == TypeSale.PRODUCT) {
		this.quantity.setText("" + sale.getQuantity() + "x");
		this.quantity.setMinWidth(40);
		if (sale.getQuantity() <= 2) {
		    if (sale.getQuantity() <= 1) {
			// red
			this.quantity.setStyle("-fx-text-fill: red;");
		    } else {
			// orange
			this.quantity.setStyle("-fx-text-fill: orange;");
		    }
		} else {
		    this.quantity.setStyle("-fx-text-fill: black;");
		}
	    }
	    this.name.setText(sale.getName());
	    this.price.setText("" + sale.getPrice() + " €");
	    this.type.setText("" + sale.getType());
	    prefWidthProperty().bind(getListView().widthProperty().subtract(Integer.MAX_VALUE));
	    setGraphic(this.gridpane);
	} else {
	    // sinon on affiche rien
	    this.name.setText("");
	    this.price.setText("");
	    this.type.setText("");
	}
    }
}

