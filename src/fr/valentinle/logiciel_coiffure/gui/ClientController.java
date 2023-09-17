package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.LocalDateString;
import fr.valentinle.logiciel_coiffure.model.Recipe;
import fr.valentinle.logiciel_coiffure.model.observer.ClientListener;
import fr.valentinle.logiciel_coiffure.model.observer.DashboardListener;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

/**
 * Controller de la fenetre de parametres
 */
public class ClientController implements Initializable, DashboardListener, ClientListener {

	// dashboard de l'application
	private Dashboard dashboard;

	private Client client;

	@FXML
	private TextField familyName;

	@FXML
	private TextField name;

	@FXML
	private TextField address;

	@FXML
	private TextField city;

	@FXML
	private TextField postalCode;

	@FXML
	private TextField phoneNumber;

	@FXML
	private Label lastVisit;

	@FXML
	private Spinner<Integer> fidelity;

	@FXML
	private Label fidelityMax;

	@FXML
	private ListView recipesListView;

	@FXML
	private ListView productsSoldListView;

	@FXML
	private DatePicker dateRecipe;

	@FXML
	private TextArea textRecipe;

	@FXML
	private TextField timeRecipe;

	private Recipe currentRecipe;

	@FXML
	private Label nameFamily;

	@FXML
	private TextArea memo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.familyName.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));

		this.name.textProperty().addListener((ov, oldValue, newValue) -> {
			this.name.setText(StringUtils.capitalize(newValue));
		});

		this.city.textProperty().addListener((ov, oldValue, newValue) -> {
			this.city.setText(StringUtils.capitalize(newValue));
		});

		this.name.textProperty().addListener((observable) -> {
			this.nameFamily.setText(this.familyName.getText() + " " + this.name.getText());
		});

		this.familyName.textProperty().addListener((observable) -> {
			this.nameFamily.setText(this.familyName.getText() + " " + this.name.getText());
		});

		dateRecipe.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (currentRecipe != null) {
				currentRecipe.setDate(newValue);
			}
		});

		textRecipe.textProperty().addListener((observable, oldValue, newValue) -> {
			if (currentRecipe != null) {
				currentRecipe.setText(newValue);
			}
		});

		timeRecipe.textProperty().addListener((observable, oldValue, newValue) -> {
			if (currentRecipe != null) {
				currentRecipe.setDuration(newValue);
			}
		});

		this.fidelity.valueProperty().addListener((observable, oldValue, newValue) -> {
			this.client.setFidelity(newValue);
		});

	}

	/**
	 * Setter sur le dashboard
	 *
	 * @param dashboard le dashboard a setter
	 */
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
		dashboard.addListener(this);
	}

	public void setClient(Client client) {
		this.client = client;
		this.familyName.setText(client.getFamilyName());
		this.name.setText(client.getName());
		this.address.setText(client.getAddress());
		this.city.setText(client.getCity());
		this.postalCode.setText(client.getPostalCode());
		this.phoneNumber.setText(client.getPhoneNumber());
		ObservableList<Recipe> observableRecipes = FXCollections.observableList(this.client.getRecipes());
		observableRecipes.sort(client.getRecipeComparator());
		this.recipesListView.setItems(observableRecipes);
		this.productsSoldListView.setItems(FXCollections.observableList(this.client.getProductsSold()));
		if (this.client.getLastVisitDate() != null) {
			this.lastVisit.setText(LocalDateString.toDateString(this.client.getLastVisitDate()));
		}
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Client.MAX_FIDELITY, this.client.getFidelity());
		this.fidelity.setValueFactory(valueFactory);
		this.fidelityMax.setText(" / " + Client.MAX_FIDELITY);
		this.memo.setText(client.getMemo());
		this.client.addListener(this);
	}

	/**
	 * Validation des parametres
	 *
	 * @param e event de click sur le bouton valider
	 */
	@FXML
	private void valid(ActionEvent e) {
		boolean clientCreated = this.createClient();
		if (clientCreated) {
			// on ferme la fenetre
			close((Node) e.getSource());
		} else {
			this.showError("Le nom et/ou le prénom n'a pas été renseigné.");
		}
	}

	public boolean createClient() {
		if (!this.familyName.getText().trim().isEmpty() && !this.name.getText().trim().isEmpty()) {
			this.client.setFamilyName(this.familyName.getText().trim());
			this.client.setName(this.name.getText().trim());
			this.client.setAddress(this.address.getText().trim());
			this.client.setCity(this.city.getText().trim());
			this.client.setPostalCode(this.postalCode.getText().trim());
			this.client.setPhoneNumber(this.phoneNumber.getText().trim());
			this.client.setMemo(this.memo.getText());

			if (!this.dashboard.clientExists(this.client)) {
				this.dashboard.addClient(this.client, true);
				Collections.sort(this.dashboard.getClients(), this.dashboard.getClientComparator());
			}
			this.dashboard.clientsListUpdated(true);
			return true;
		} else {
			return false;
		}
	}

	public void showError(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erreur");
		alert.setContentText(msg);

		alert.showAndWait();
	}

	@FXML
	public void addRecipe(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Recipe.fxml"));
			Parent root = loader.load();
			RecipeController controller = loader.getController();
			controller.setClient(this.client);
			Stage stage = new Stage();
			stage.setTitle("Recette");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/recipe.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.familyName.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void removeRecipe(ActionEvent e) {
		int index = this.recipesListView.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			this.client.removeRecipeAt(index);
			dateRecipe.setValue(null);
			textRecipe.setText("");
			timeRecipe.setText("");
			currentRecipe = null;
		}
	}

	@FXML
	public void recipeClick(MouseEvent e) {
		int index = this.recipesListView.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			Recipe recipe = this.client.getRecipes().get(index);
			this.currentRecipe = recipe;
			dateRecipe.setValue(recipe.getDate());
			textRecipe.setText(recipe.getText());
			timeRecipe.setText(recipe.getDuration());
		}
	}

	@FXML
	public void facturation(ActionEvent e) {
		if (dashboard.clientExists(this.client)) {
			this.openFacturationWindow();
		} else {
			boolean wantCreateClient = this
					.confirmationDialog("Le client n'est pas enregistré, faut-t-il l'enregistrer ?");
			if (wantCreateClient) {
				boolean clientCreated = this.createClient();
				if (clientCreated) {
					this.openFacturationWindow();
				} else {
					this.showError("Le nom et/ou le prénom n'a pas été renseigné.");
				}
			}
		}
	}

	public boolean confirmationDialog(String statement) {
		Alert alert = new Alert(AlertType.CONFIRMATION, statement);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> choose = alert.showAndWait();
		return choose.get() == ButtonType.YES;
	}

	public void openFacturationWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Facturation.fxml"));
			Parent root = loader.load();
			FacturationController facturationControl = loader.getController();
			facturationControl.setDashboard(dashboard);
			facturationControl.setClient(this.client);
			Stage stage = new Stage();
			stage.setTitle("Facturation");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/facture.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.familyName.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Annulation des parametres
	 *
	 * @param e event de click sur le bouton cancel
	 */
	@FXML
	private void cancel(ActionEvent e) {
		boolean canClose = true;
		if (!this.dashboard.clientExists(this.client)) {
			if (!this.familyName.getText().isEmpty() || !this.name.getText().isEmpty()
					|| !this.address.getText().isEmpty() || !this.city.getText().isEmpty()
					|| !this.phoneNumber.getText().isEmpty() || !this.postalCode.getText().isEmpty()) {

				boolean wantCreateClient = this
						.confirmationDialog("Le client n'est pas enregistré, faut-t-il l'enregistrer ?");
				if (wantCreateClient) {
					boolean clientCreated = this.createClient();
					if (!clientCreated) {
						canClose = false;
						this.showError("Le nom et/ou le prénom n'a pas été renseigné.");
					}
				}
			}
		}
		if (canClose) {
			// on ferme la fenetre sans rien prendre en compte
			close((Node) e.getSource());
		}
	}

	@FXML
	private void deleteClient(ActionEvent e) {
		if (this.dashboard.getClients().contains(this.client)) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Supprimer " + this.client.getFullName() + " ?",
					ButtonType.YES, ButtonType.NO);
			alert.setTitle("Supprimer le client ?");
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				Platform.runLater(() -> {
					if (this.dashboard.clientExists(this.client)) {
						this.dashboard.removeClient(this.client, true);
					}
				});
				close((Node) e.getSource());
			}
		} else {
			this.showError("Suppression impossible, le client n'a pas été créé.");
		}
	}

	/**
	 * Ferme la fenetre dans lequel se trouve le noeud donne
	 *
	 * @param node le noeud dont fermer la fenetre dans laquelle il se trouve
	 */
	private void close(Node node) {
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
	}

	@Override
	public void clientsListHasChanged() {

	}

	@Override
	public void clientHasChanged() {

	}

	@Override
	public void clientDataHasChanged() {
		ObservableList<Recipe> observableRecipes = FXCollections.observableList(this.client.getRecipes());
		observableRecipes.sort(client.getRecipeComparator());
		this.recipesListView.setItems(observableRecipes);
	}

	@Override
	public void factureCreated() {
		if (this.client.getLastVisitDate() != null) {
			this.lastVisit.setText(LocalDateString.toDateString(this.client.getLastVisitDate()));
		}
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Client.MAX_FIDELITY, this.client.getFidelity());
		this.fidelity.setValueFactory(valueFactory);
		this.productsSoldListView.setItems(FXCollections.observableList(this.client.getProductsSold()));
	}

}
