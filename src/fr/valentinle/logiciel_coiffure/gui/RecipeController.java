
package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Recipe;
import fr.valentinle.logiciel_coiffure.model.observer.DashboardListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecipeController implements Initializable {

    private Client client;
    
    @FXML
    private DatePicker dateRecipe;
    
    @FXML
    private TextArea textRecipe;
    
    @FXML
    private TextField timeRecipe;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	dateRecipe.setValue(LocalDate.now());
    }
    
    public void setClient(Client client) {
	this.client = client;
    }
    
    @FXML
    public void addRecipe(ActionEvent e) {
	LocalDate date = dateRecipe.getValue();
	String text = textRecipe.getText();
	String textTime = timeRecipe.getText();
	Recipe recipe = new Recipe(date, text, textTime);
	this.client.addRecipe(recipe);
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
