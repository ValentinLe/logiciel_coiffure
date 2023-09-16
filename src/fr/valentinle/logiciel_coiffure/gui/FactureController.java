
package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Facture;
import fr.valentinle.logiciel_coiffure.model.LocalDateString;
import fr.valentinle.logiciel_coiffure.model.Sale;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class FactureController implements Initializable {

    @FXML
    private Label fullname;
    
    @FXML
    private Label price;
    
    @FXML
    private Label modePaiement;
    
    @FXML
    private Label date;
    
    @FXML
    private ListView<Sale> listViewSales;
    
    private Facture facture;
    
    private Dashboard dashboard;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	
    }
    
    public void setFacture(Facture facture) {
	this.facture = facture;
	this.fullname.setText(facture.getFullName());
	NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
	this.price.setText(nf.format(facture.getAmount()) + " €");
	this.modePaiement.setText(facture.getTypePaiement().name());
	this.date.setText(LocalDateString.toDateString(facture.getDate()));
	
	SaleCellFactory saleCellFactory = new SaleCellFactory(null, this.facture.getSales(), false);
	this.listViewSales.setCellFactory(saleCellFactory);
	this.listViewSales.setItems(FXCollections.observableList(this.facture.getSales()));
    }
    
    /**
     * Ferme la fenetre dans lequel se trouve le noeud donne
     * @param node le noeud dont fermer la fenetre dans laquelle il se trouve
     */
    private void close(Node node) {
	Stage stage = (Stage) node.getScene().getWindow();
	stage.close();
    }
    
    public boolean confirmationDialog(String statement) {
	Alert alert = new Alert(Alert.AlertType.CONFIRMATION, statement);
	alert.getButtonTypes().clear();
	alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	Optional<ButtonType> choose = alert.showAndWait();
	return choose.get() == ButtonType.YES;
    }
    
    public void setDashboard(Dashboard dashboard) {
	this.dashboard = dashboard;
    }
    
    @FXML
    public void deleteFacture(ActionEvent e) {
	if (this.confirmationDialog("Supprimer la facture de " + this.facture.getFullName() + " ?")) {
	    this.dashboard.removeFacture(this.facture);
	    this.dashboard.saveFactures();
	    close((Node) e.getSource());
	}
    }
    
}
