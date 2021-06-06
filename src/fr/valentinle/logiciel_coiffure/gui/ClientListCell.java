package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class ClientListCell extends ListCell<Client> {

    @FXML
    private Label familyName;
    
    @FXML
    private Label name;
    
    @FXML
    private GridPane gridpane;

    // le dashboard de l'application
    private Dashboard dashboard;

    // la liste que contient la listView
    private List<Client> list;

    public ClientListCell(Dashboard dashboard, List<Client> list) {
	loadFXML();
	this.dashboard = dashboard;
	this.list = list;
    }

    public List<Client> getClientList() {
	return this.list;
    }

    /**
     * Charge la structure d'une cellule representant un client
     */
    private void loadFXML() {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/ClientCell.fxml"));
	    loader.setController(this);
	    loader.load();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    protected void updateItem(Client client, boolean empty) {
	super.updateItem(client, empty);
	if (!empty) {
	    // si la cellule contient un client, on affiche ses informations
	    this.familyName.setText(client.getFamilyName());
	    this.name.setText(client.getName());
	    prefWidthProperty().bind(getListView().widthProperty().subtract(Integer.MAX_VALUE));
	    setGraphic(this.gridpane);
	} else {
	    // sinon on affiche rien
	    familyName.setText("");
	    name.setText("");
	}
    }
}
