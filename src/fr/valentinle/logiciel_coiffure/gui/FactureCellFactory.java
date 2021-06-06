package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Facture;
import java.util.List;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class FactureCellFactory implements Callback<ListView<Facture>, ListCell<Facture>> {
    
    // le dashboard de l'application
    private Dashboard dashboard;
    // la liste que represente la listView
    private List<Facture> list;
    
    public FactureCellFactory(Dashboard dashboard, List<Facture> list) {
	this.dashboard = dashboard;
	this.list = list;
    }
    
    @Override
    public ListCell<Facture> call(ListView<Facture> param) {
	return new FactureListCell(this.dashboard, this.list);
    }
}