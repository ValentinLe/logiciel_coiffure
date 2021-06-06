package fr.valentinle.logiciel_coiffure.gui;

import static fr.valentinle.logiciel_coiffure.gui.DashboardController.savePointsFilename;
import fr.valentinle.logiciel_coiffure.model.Config;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.output.JSONWriter;
import fr.valentinle.logiciel_coiffure.output.SaveData;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Val
 */
public class SaveController implements Initializable {
    
    private Dashboard dashboard;
    
    private Config config;
    
    @FXML
    private Spinner<Integer> spinnerFreqSave;
    
    @FXML
    private ListView<String> listFolders;
    
    protected ObservableList<String> savePoints;
    
    private int initialValue = -1;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	this.spinnerFreqSave.valueProperty().addListener((observable, oldValue, newValue) -> {
	    this.config.FREQ_SAVE = newValue;
	});
    }
    
    public void setDashboard(Dashboard dashboard) {
	this.dashboard = dashboard;
	this.savePoints = FXCollections.observableList(new ArrayList<>(dashboard.getSavePoints()));
	this.listFolders.setItems(this.savePoints);
	this.dashboard.setSavePoints(this.savePoints);
    }
    
    public void setConfig(Config config) {
	this.config = config;
	this.initialValue = config.FREQ_SAVE;
	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, config.FREQ_SAVE);
	this.spinnerFreqSave.setValueFactory(valueFactory);
    }
    
    @FXML
    public void addFolder(ActionEvent e) {
	DirectoryChooser chooser = new DirectoryChooser();
	chooser.setTitle("Points de sauvegarde");
	File selectedDirectory = chooser.showDialog(listFolders.getScene().getWindow());
	if (selectedDirectory != null && !this.savePoints.contains(selectedDirectory.getPath())) {
	    this.savePoints.add(selectedDirectory.getPath());
	}
    }
     
    @FXML
    public void removeFolder(ActionEvent e) {
	int index = this.listFolders.getSelectionModel().getSelectedIndex();
	if (index >= 0) {
	    this.savePoints.remove(index);
	}
    }
    
    @FXML
    public void valid(ActionEvent e) {
	SaveData.writeSavePoints(savePointsFilename, dashboard.getSavePoints());
	JSONWriter<Config> writer = new JSONWriter<>();
	writer.write(config, DashboardController.configFilename);
	close((Node) e.getSource());
    }
    
    @FXML
    public void cancel(ActionEvent e) {
	if (initialValue > 0) {
	    this.config.FREQ_SAVE = initialValue;
	}
	close((Node) e.getSource());
    }
    
    /**
     * Ferme la fenetre dans lequel se trouve le noeud donne
     * @param node le noeud dont fermer la fenetre dans laquelle il se trouve
     */
    private void close(Node node) {
	Stage stage = (Stage) node.getScene().getWindow();
	stage.close();
    }
}
