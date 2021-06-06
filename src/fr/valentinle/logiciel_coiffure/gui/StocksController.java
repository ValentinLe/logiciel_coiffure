package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Sale;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class StocksController implements Initializable {
    
    @FXML
    private TextField quantity;
    
    @FXML
    private ListView<Sale> products;

    private Dashboard dashboard;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	// force the field to be numeric only
	quantity.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		String buildedValue = newValue;
		if (!newValue.matches("\\d*")) {
		    buildedValue = buildedValue.replaceAll("[^\\d]", "");
		}
		quantity.setText(buildedValue);
	    }
	});
    }

    public void setDashboard(Dashboard dashboard) {
	this.dashboard = dashboard;
	ObservableList<Sale> observableSales = FXCollections.observableList(this.dashboard.getProducts());
	SaleCellFactory saleCellFactory = new SaleCellFactory(this.dashboard, observableSales, true);
	this.products.setCellFactory(saleCellFactory);
	this.products.setItems((ObservableList<Sale>) observableSales);
    }
    
    @FXML
    public void addQuantity(ActionEvent e) {
	Sale selectedSale = this.products.getSelectionModel().getSelectedItem();
	if (selectedSale != null) {
	    if (!this.quantity.getText().isEmpty()) {
		try {
		    this.dashboard.addQuantityProduct(selectedSale, Integer.parseInt(this.quantity.getText()));
		    this.products.refresh();
		    this.quantity.setText("");
		    this.dashboard.saveSales();
		} catch(NumberFormatException ex) {
		    showError("Quantité trop importante.");
		}
	    } else {
		showError("La quantité de produit à ajouter n'est pas renseignée.");
	    }
	}
    }
    
    public void showError(String msg) {
	Alert alert = new Alert(Alert.AlertType.ERROR);
	alert.setTitle("Erreur");
	alert.setContentText(msg);

	alert.showAndWait();
    }
    
    @FXML
    public void removeQuantity(ActionEvent e) {
	Sale selectedSale = this.products.getSelectionModel().getSelectedItem();
	if (selectedSale != null) {
	    if (!this.quantity.getText().isEmpty()) {
		try {
		    this.dashboard.removeQuantityProduct(selectedSale, Integer.parseInt(this.quantity.getText()));
		    this.products.refresh();
		    this.quantity.setText("");
		    this.dashboard.saveSales();
		} catch(NumberFormatException ex) {
		    showError("Quantité trop importante.");
		}
	    } else {
		showError("La quantité de produit à supprimer n'est pas renseignée.");
	    }
	}
    }

}
