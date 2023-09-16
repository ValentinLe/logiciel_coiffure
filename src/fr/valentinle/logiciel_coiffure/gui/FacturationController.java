package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Facture;
import fr.valentinle.logiciel_coiffure.model.Sale;
import fr.valentinle.logiciel_coiffure.model.TypePaiement;
import fr.valentinle.logiciel_coiffure.model.TypeSale;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class FacturationController implements Initializable {
    
    @FXML
    private TextField price;
    
    @FXML
    private TextField familyField;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private DatePicker dateField;
    
    @FXML
    private ListView<Sale> listViewSales;
    
    @FXML
    private Label fidelity;
    
    @FXML
    private ListView<Sale> listViewSalesFacture;
    
    @FXML
    private ComboBox<TypePaiement> typePaiement;
    
    private Facture facture;
    
    private Dashboard dashboard;
    
    private Client client;
    
    @FXML
    private HBox zoneFidelity;
    
    @FXML
    private ImageView iconFree;
    
    @FXML
    private ComboBox<TypeSale> comboTypeSale;
    
    
    private FilteredList<Sale> filteredListSales;
    
    @FXML
    private CheckBox checkFidelity;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	
	this.familyField.setTextFormatter(new TextFormatter<>((change) -> {
	    change.setText(change.getText().toUpperCase());
	    return change;
	}));
	
	this.nameField.textProperty().addListener((ov, oldValue, newValue) -> {
	    this.nameField.setText(StringUtils.capitalize(newValue));
       });
	
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
	
	this.dateField.setValue(LocalDate.now());
	this.facture = new Facture();
	this.facture.setSales(FXCollections.observableList(new ArrayList<>()));
	this.listViewSalesFacture.setItems((ObservableList<Sale>) this.facture.getSales());
	
	this.checkFidelity.setSelected(true);
	ObservableList<TypePaiement> observableTypes = FXCollections.observableList(Arrays.asList(TypePaiement.values()));
	this.typePaiement.setItems(observableTypes);
	this.typePaiement.getSelectionModel().selectFirst();
	this.zoneFidelity.setVisible(false);
	this.zoneFidelity.setManaged(false);
	this.iconFree.setVisible(false);
	
	this.comboTypeSale.setItems(FXCollections.observableArrayList(TypeSale.values()));
	this.comboTypeSale.getSelectionModel().selectFirst();
	
	this.comboTypeSale.valueProperty().addListener((observable, oldValue, newValue) -> {
	    filteredListSales.setPredicate(sale -> {
		TypeSale type = this.comboTypeSale.getSelectionModel().getSelectedItem();
		if (type == null) {
		    return true;
		}
		return sale.getType() == type;
	    });
	});
    }
    
    public void setDashboard(Dashboard dashboard) {
	this.dashboard = dashboard;
	SaleCellFactory saleCellFactory = new SaleCellFactory(dashboard, dashboard.getSales(), false);
	this.filteredListSales = new FilteredList<>((ObservableList<Sale>) dashboard.getSales(), sale -> {
	    TypeSale type = this.comboTypeSale.getSelectionModel().getSelectedItem();
	    if (type == null) {
		return true;
	    }
	    return sale.getType() == type;
	});
	this.listViewSales.setCellFactory(saleCellFactory);
	this.listViewSales.setItems(filteredListSales);
    }
    
    public void setClient(Client client) {
	this.client = client;
	this.familyField.setText(client.getFamilyName());
	this.nameField.setText(client.getName());
	this.zoneFidelity.setVisible(true);
	this.zoneFidelity.setManaged(true);
	this.fidelity.setText(client.getFidelity() + " / 7");
	this.iconFree.setVisible(client.freePrestation());
    }
    
    /**
     * Ferme la fenetre dans lequel se trouve le noeud donne
     * @param node le noeud dont fermer la fenetre dans laquelle il se trouve
     */
    private void close(Node node) {
	Stage stage = (Stage) node.getScene().getWindow();
	stage.close();
    }
    
    public String getFullName() {
	return this.familyField.getText() + " " + this.nameField.getText();
    }
    
    @FXML
    public void facturer(ActionEvent e) {
	if (Double.parseDouble(this.price.getText()) == 0.0) {
	    this.showError("Le prix n'a pas été saisie");
	} else if (nameField.getText().isEmpty() || familyField.getText().isEmpty()) {
	    this.showError("Le prénom et/ou le nom de famille n'a pas été renseigné");
	} else {
	    this.facture.setDate(this.dateField.getValue());
	    if (this.client != null) {
		this.facture.setClient(this.client);
		this.client.passageClient(this.dateField.getValue(), true);
	    } else {
		this.facture.setFullName(this.getFullName());
	    }
	    this.dashboard.addFacture(this.facture);
	    if (this.listViewSalesFacture.getItems().isEmpty()) {
		this.facture.setAmount(Double.parseDouble(this.price.getText()));
	    }
	    this.facture.setTypePaiement(this.typePaiement.getSelectionModel().getSelectedItem());
	    this.facture.computeFacture(dashboard, client);
	    Collections.sort(this.dashboard.getFactures(), this.dashboard.getFactureComparator());
	    this.dashboard.saveFactures();
	    if (this.client != null) {
		this.dashboard.saveClients();
	    }
	    this.dashboard.factureCreated();
	    close((Node) e.getSource());
	}
    }
    
    public void showError(String msg) {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setTitle("Erreur");
	alert.setContentText(msg);

	alert.showAndWait();
    }
    
    @FXML
    public void addSale(ActionEvent e) {
	int index = this.listViewSales.getSelectionModel().getSelectedIndex();
	if (index >= 0) {
	    this.facture.addSale(this.listViewSales.getSelectionModel().getSelectedItem());
	    this.price.setText("" + this.facture.getAmount());
	}
    }
    
    @FXML
    public void removeSale(ActionEvent e) {
	int index = this.listViewSales.getSelectionModel().getSelectedIndex();
	if (index >= 0) {
	    this.facture.removeSale(this.listViewSales.getSelectionModel().getSelectedItem());
	    this.price.setText("" + this.facture.getAmount());
	}
    }
    
    @FXML
    public void clickSale(MouseEvent e) {
	if (Math.floorMod(e.getClickCount(), 2) == 0) {
	    int index = this.listViewSales.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		this.facture.addSale(this.listViewSales.getSelectionModel().getSelectedItem());
		this.price.setText("" + (Math.round(100.0 * this.facture.getAmount()) / 100.0));
	    }

	}
    }
    
    @FXML
    public void annuler(ActionEvent e) {
	close((Node) e.getSource());
    }
    
}
