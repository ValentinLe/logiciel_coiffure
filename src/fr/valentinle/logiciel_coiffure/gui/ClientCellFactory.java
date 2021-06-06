package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import java.util.List;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Factory sur une music d'une listView
 */
public class ClientCellFactory implements Callback<ListView<Client>, ListCell<Client>> {
    
    // le dashboard de l'application
    private Dashboard dashboard;
    // la liste que represente la listView
    private List<Client> list;
    
    public ClientCellFactory(Dashboard dashboard, List<Client> list) {
        this.dashboard = dashboard;
        this.list = list;
    }
    
    @Override
    public ListCell<Client> call(ListView<Client> param) {
        return new ClientListCell(this.dashboard, this.list);
    }
}
