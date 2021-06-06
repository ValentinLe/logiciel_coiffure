package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Sale;
import java.util.List;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Factory sur une music d'une listView
 */
public class SaleCellFactory implements Callback<ListView<Sale>, ListCell<Sale>> {
    
    private Dashboard dashboard;
    
    // la liste que represente la listView
    private List<Sale> list;
    
    private boolean showQuantity;
    
    public SaleCellFactory(Dashboard dashboard, List<Sale> list, boolean showQuantity) {
	this.dashboard = dashboard;
	this.list = list;
	this.showQuantity = showQuantity;
	
    }
    
    @Override
    public ListCell<Sale> call(ListView<Sale> param) {
	return new SaleListCell(this.dashboard, this.list, this.showQuantity);
    }
}
