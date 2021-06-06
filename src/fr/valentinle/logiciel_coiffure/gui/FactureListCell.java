package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Facture;
import fr.valentinle.logiciel_coiffure.model.LocalDateString;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class FactureListCell extends ListCell<Facture> {

    @FXML
    private Label date;
    
    @FXML
    private Label fullname;
    
    @FXML
    private Label amount;
    
    @FXML
    private GridPane gridpane;

    // le dashboard de l'application
    private Dashboard dashboard;

    // la liste que contient la listView
    private List<Facture> list;

    public FactureListCell(Dashboard dashboard, List<Facture> list) {
	loadFXML();
	this.dashboard = dashboard;
	this.list = list;
    }

    /**
     * Charge la structure d'une cellule representant un client
     */
    private void loadFXML() {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/FactureCell.fxml"));
	    loader.setController(this);
	    loader.load();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    protected void updateItem(Facture facture, boolean empty) {
	super.updateItem(facture, empty);
	if (!empty) {
	    // si la cellule contient un client, on affiche ses informations
	    this.date.setText(LocalDateString.toDateString(facture.getDate()));
	    this.fullname.setText(facture.getFullName());
	    NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
	    this.amount.setText(facture.getTypePaiement().toString() + " : " + nf.format(facture.getAmount()) + " €");
	    prefWidthProperty().bind(getListView().widthProperty().subtract(Integer.MAX_VALUE));
	    setGraphic(this.gridpane);
	} else {
	    // sinon on affiche rien
	    this.date.setText("");
	    this.fullname.setText("");
	    this.amount.setText("");
	}
    }
}