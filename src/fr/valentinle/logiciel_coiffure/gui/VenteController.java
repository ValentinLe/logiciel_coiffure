package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Sale;
import fr.valentinle.logiciel_coiffure.model.TypeSale;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class VenteController implements Initializable {
    
    private Dashboard dashboard;
    
    @FXML
    public TextField nameField;
    
    @FXML
    public TextField price;
    
    @FXML
    public ComboBox<TypeSale> typeSale;
    
    @FXML
    public ListView<Sale> listViewSales;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
	Pattern validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]{0,2})?");
	
	UnaryOperator<TextFormatter.Change> filter = c -> {
	    String text = c.getControlNewText();
	    if (validEditingState.matcher(text).matches()) {
		return c ;
	    } else {
		return null ;
	    }
	};

	StringConverter<Double> converter = new StringConverter<Double>() {

	    @Override
	    public Double fromString(String s) {
		if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
		    return 0.0 ;
		} else {
		    return Double.valueOf(s);
		}
	    }
	    
	    @Override
	    public String toString(Double d) {
		return d.toString();
	    }
	};

	TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
	this.price.setTextFormatter(textFormatter);
	
	ObservableList<TypeSale> observableTypes = FXCollections.observableList(Arrays.asList(TypeSale.values()));
	typeSale.setItems(observableTypes);
	typeSale.getSelectionModel().selectFirst();
    }
    
    public void setDashboard(Dashboard dashboard) {
	this.dashboard = dashboard;
	SaleCellFactory saleCellFactory = new SaleCellFactory(this.dashboard, this.dashboard.getSales(), false);
	this.listViewSales.setCellFactory(saleCellFactory);
	this.listViewSales.setItems((ObservableList<Sale>) dashboard.getSales());
	this.listViewSales.refresh();
    }
    
    @FXML
    public void addVente(ActionEvent e) {
	if (this.nameField.getText().isEmpty()) {
	    showError("Le nom n'a pas été saisie");
	} else if (Double.parseDouble(this.price.getText()) == 0.0) {
	    showError("Le prix n'a pas été saisie");
	} else {
	    String name = this.nameField.getText();
	    double price = Double.parseDouble(this.price.getText());
	    TypeSale type = this.typeSale.getValue();
	    if (!name.isEmpty()) {
		Sale sale = new Sale(name, price, type);
		this.dashboard.getSales().add(sale);
		this.dashboard.saveSales();
		this.nameField.setText("");
		this.price.setText("0.0");
		Collections.sort(this.dashboard.getSales(), this.dashboard.getSaleComparator());
		this.dashboard.saveSales();
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
    public void modifSale(ActionEvent e) {
	if (this.nameField.getText().isEmpty()) {
	    showError("Le nom n'a pas été saisie");
	} else if (Double.parseDouble(this.price.getText()) == 0.0) {
	    showError("Le prix n'a pas été saisie");
	} else {
	    Sale sale = this.listViewSales.getSelectionModel().getSelectedItem();
	    if (sale != null) {
		sale.setName(this.nameField.getText());
		sale.setPrice(Double.parseDouble(this.price.getText()));
		sale.setType(this.typeSale.getSelectionModel().getSelectedItem());
		this.dashboard.saveSales();
		this.nameField.setText("");
		this.price.setText("0.0");
		this.listViewSales.refresh();
		this.dashboard.saveSales();
	    }
	}
    }
    
    @FXML
    public void deleteSale(ActionEvent e) {
	int index = this.listViewSales.getSelectionModel().getSelectedIndex();
	if (index >= 0) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + this.dashboard.getSales().get(index).getName() + " ?", ButtonType.YES, ButtonType.NO);
	    alert.setTitle("Supprimer le client ?");
	    alert.showAndWait();

	    if (alert.getResult() == ButtonType.YES) {
		this.dashboard.getSales().remove(index);
		this.dashboard.saveSales();
	    }
	}
    }
    
    @FXML
    public void clickSale(MouseEvent e) {
	if (Math.floorMod(e.getClickCount(), 2) == 0) {
	    Sale sale = this.listViewSales.getSelectionModel().getSelectedItem();
	    if (sale != null) {
		this.nameField.setText(sale.getName());
		this.price.setText("" + sale.getPrice());
		this.typeSale.getSelectionModel().select(sale.getType());
	    }

	}
    }
}
