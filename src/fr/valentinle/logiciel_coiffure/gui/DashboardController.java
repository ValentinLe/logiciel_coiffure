package fr.valentinle.logiciel_coiffure.gui;

import fr.valentinle.logiciel_coiffure.model.Sale;
import fr.valentinle.logiciel_coiffure.model.Client;
import fr.valentinle.logiciel_coiffure.model.Config;
import fr.valentinle.logiciel_coiffure.model.Dashboard;
import fr.valentinle.logiciel_coiffure.model.Facture;
import fr.valentinle.logiciel_coiffure.model.TypeSale;
import fr.valentinle.logiciel_coiffure.model.observer.DashboardListener;
import fr.valentinle.logiciel_coiffure.output.JSONReader;
import fr.valentinle.logiciel_coiffure.output.JSONWriter;
import fr.valentinle.logiciel_coiffure.output.SaveData;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DashboardController implements Initializable, DashboardListener {

	protected Dashboard dashboard;

	@FXML
	protected ListView<Client> clientsListView;

	@FXML
	protected Label clientsSize;

	@FXML
	protected Label caDay;

	@FXML
	protected Label caMensuel;

	@FXML
	protected Label caAnnuel;

	@FXML
	protected ListView<Facture> factureListView;

	@FXML
	protected TextField searchTextField;

	private FilteredList<Client> filteredListClient;

	private FilteredList<Facture> filteredListFactures;

	@FXML
	protected PieChart piechartMonth;

	@FXML
	protected PieChart piechartYear;

	@FXML
	protected ComboBox<Integer> comboMonth;

	@FXML
	protected ComboBox<Integer> comboYear;

	@FXML
	protected Label gainsSelect;

	@FXML
	protected Label legendPrestaMonth;

	@FXML
	protected Label legendProdMonth;

	@FXML
	protected Label legendPrestaYear;

	@FXML
	protected Label legendProdYear;

	protected Config config;

	protected static String clientFilename = "resources/data/clients.json";
	protected static String salesFilename = "resources/data/sales.json";
	protected static String facturesFilename = "resources/data/factures.json";
	protected static String savePointsFilename = "resources/config/points_sauvegarde.txt";
	protected static String configFilename = "resources/config/config.json";
	protected static String saveDataFilename = "resources/data/";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		File dataFolder = new File(saveDataFilename);
		File configFolder = new File(configFilename);
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		if (!configFolder.exists()) {
			configFolder.mkdir();
		}
		ObservableList<Client> observableClients = FXCollections.observableList(new ArrayList<>());
		ObservableList<Sale> observableSales = FXCollections.observableList(new ArrayList<>());
		ObservableList<Facture> observableFactures = FXCollections.observableList(new ArrayList<>());
		this.dashboard = new Dashboard(observableClients, observableSales, observableFactures, clientFilename,
				salesFilename, facturesFilename,
				FXCollections.observableList(SaveData.loadSavePoints(savePointsFilename)));
		this.dashboard.addListener(this);
		this.dashboard.loadClients();
		this.dashboard.loadSales();
		this.dashboard.loadFactures();
		this.clientsSize.setText("" + this.dashboard.getNumberOfClients());

		this.clientsListView.setItems((ObservableList<Client>) this.dashboard.getClients());

		ClientCellFactory clientCellFactory = new ClientCellFactory(this.dashboard, this.dashboard.getClients());
		this.clientsListView.setCellFactory(clientCellFactory);
		this.clientsListView.setItems((ObservableList<Client>) this.dashboard.getClients());

		FactureCellFactory factureCellFactory = new FactureCellFactory(dashboard, this.filteredListFactures);
		this.factureListView.setCellFactory(factureCellFactory);
		this.writeCa();

		this.comboMonth.valueProperty().addListener((observable, oldValue, newValue) -> {
			Map<Integer, Map<Integer, LocalDate>> mapDates = dashboard.getMapMonthYearFactures();
			filteredListFactures.setPredicate(facture -> {
				Integer month = newValue;
				Integer year = comboYear.getSelectionModel().getSelectedItem();
				if (month == null || year == null) {
					return true;
				}
				return facture.getDate().getMonthValue() == month && facture.getDate().getYear() == year;
			});
			this.factureListView.refresh();
			this.updateLabelGainsOfSelection();
			this.drawChartMonth();
		});

		this.comboYear.valueProperty().addListener((observable, oldValue, newValue) -> {
			Map<Integer, Map<Integer, LocalDate>> mapDates = dashboard.getMapMonthYearFactures();
			if (mapDates.containsKey(newValue)) {
				List<Integer> values = new ArrayList<>(mapDates.get(newValue).keySet());
				values.sort(new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1.compareTo(o2);
					}
				}.reversed());
				this.comboMonth.setItems(FXCollections.observableList(values));
				this.comboMonth.getSelectionModel().selectFirst();
			}
			filteredListFactures.setPredicate(facture -> {
				Integer month = comboMonth.getSelectionModel().getSelectedItem();
				Integer year = newValue;
				if (month == null || year == null) {
					return true;
				}
				return facture.getDate().getMonthValue() == month && facture.getDate().getYear() == year;
			});
			this.factureListView.refresh();
			this.updateLabelGainsOfSelection();
			this.drawChartMonth();
		});
		
		this.comboboxDates();

		this.drawCharts();

		this.loadConfig();
		this.saveDataIfMustSave();

		this.filteredListClient = new FilteredList<>(observableClients, s -> true);
		this.clientsListView.setItems(filteredListClient);

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredListClient.setPredicate(client -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseSearch = searchTextField.getText().toLowerCase();
				return client.getFamilyName().toLowerCase().contains(lowerCaseSearch)
						|| client.getName().toLowerCase().contains(lowerCaseSearch);
			});
		});
	}

	public static String getJarPath() {
		try {
			return new File(DashboardController.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getParent() + "/";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public void saveDataIfMustSave() {
		long nbDaysLastSave = Duration.between(this.config.LAST_UPDATE.atStartOfDay(), LocalDate.now().atStartOfDay())
				.toDays();
		List<String> savePoints = this.dashboard.getSavePoints();
		if (nbDaysLastSave >= this.config.FREQ_SAVE && !savePoints.isEmpty()) {
			SaveData.saveData(saveDataFilename, savePoints);
			config.LAST_UPDATE = LocalDate.now();
			saveConfig();
		}
	}

	@FXML
	public void manualSaveData(ActionEvent e) {
		List<String> savePoints = this.dashboard.getSavePoints();
		if (!savePoints.isEmpty()) {
			SaveData.saveData(saveDataFilename, savePoints);
			config.LAST_UPDATE = LocalDate.now();
			saveConfig();
		} else {
			showWarning("Pas de points de sauvegardes",
					"Il n'y a pas de points de sauvegardes enregistrés, la sauvegarde n'a pas été faite.");
		}
	}

	public void showWarning(String title, String msg) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(msg);

		alert.showAndWait();
	}

	public void loadConfig() {
		JSONReader<Config> parserConfig = new JSONReader<>();
		this.config = parserConfig.read(configFilename, Config.class);
		if (this.config == null) {
			this.config = new Config();
			this.config.LAST_UPDATE = LocalDate.now();
			saveConfig();
		}
	}

	public void saveConfig() {
		JSONWriter<Config> writer = new JSONWriter<>();
		writer.write(config, configFilename);
	}

	public void comboboxDates() {
		this.filteredListFactures = new FilteredList<>((ObservableList<Facture>) dashboard.getFactures(), facture -> {
			Integer month = this.comboMonth.getSelectionModel().getSelectedItem();
			Integer year = this.comboYear.getSelectionModel().getSelectedItem();
			if (month == null || year == null) {
				return true;
			}
			return facture.getDate().getMonthValue() == month && facture.getDate().getYear() == year;
		});
		this.factureListView.setItems(this.filteredListFactures);
		this.comboYear.setItems(FXCollections.observableList(dashboard.getFacturesYears()));
		this.comboYear.getSelectionModel().selectFirst();
		Map<Integer, Map<Integer, LocalDate>> mapDatesStart = dashboard.getMapMonthYearFactures();
		if (mapDatesStart.containsKey(this.comboYear.getSelectionModel().getSelectedItem())) {
			List<Integer> values = new ArrayList<>(
					mapDatesStart.get(this.comboYear.getSelectionModel().getSelectedItem()).keySet());
			values.sort(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			}.reversed());
			this.comboMonth.setItems(FXCollections.observableList(new ArrayList<>(values)));
			this.comboMonth.getSelectionModel().selectFirst();
		}
		this.comboMonth.setConverter(new IntegerMonthConverter());
		this.updateLabelGainsOfSelection();
	}

	public void updateLabelGainsOfSelection() {
		Integer month = this.comboMonth.getSelectionModel().getSelectedItem();
		Integer year = this.comboYear.getSelectionModel().getSelectedItem();
		double gains = 0.0;
		if (month != null && year != null) {
			LocalDate date = LocalDate.of(year, month, 1);
			gains = this.dashboard.sumMensuelFacture(date);
		}
		NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
		this.gainsSelect.setText(nf.format(gains) + " €");
	}

	public void drawCharts() {
		this.drawChartMonth();
		this.drawChartYear();
	}

	public void drawChartMonth() {
		ObservableList<Data> dataList = FXCollections.observableList(new ArrayList<>());
		Integer month = this.comboMonth.getSelectionModel().getSelectedItem();
		Integer year = this.comboYear.getSelectionModel().getSelectedItem();
		if (month == null) {
			month = LocalDate.now().getMonthValue();
		}
		if (year == null) {
			year = LocalDate.now().getYear();
		}
		NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
		for (TypeSale type : TypeSale.values()) {
			double value = dashboard.getGainsOf(dashboard.getFacturesOfMonth(month, year), type);
			if (type == TypeSale.PRESTATION) {
				this.legendPrestaMonth.setText("Préstations\n" + nf.format(value) + " €");
			} else if (type == TypeSale.PRODUCT) {
				this.legendProdMonth.setText("Produits\n" + nf.format(value) + " €");
			}
			dataList.add(new Data(type.toString() + "\n" + nf.format(value) + " €", value));
		}
		this.piechartMonth.setData(dataList);
		this.piechartMonth.setLegendVisible(false);
	}

	public void drawChartYear() {
		ObservableList<Data> dataList = FXCollections.observableList(new ArrayList<>());
		NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
		for (TypeSale type : TypeSale.values()) {
			double value = dashboard.getGainsOf(dashboard.getFacturesOfYear(LocalDate.now()), type);
			if (type == TypeSale.PRESTATION) {
				this.legendPrestaYear.setText("Préstations\n" + nf.format(value) + " €");
			} else if (type == TypeSale.PRODUCT) {
				this.legendProdYear.setText("Produits\n" + nf.format(value) + " €");
			}
			dataList.add(new Data(type.toString() + "\n" + nf.format(value) + " €", value));
		}
		this.piechartYear.setData(dataList);
		this.piechartYear.setLegendVisible(false);
	}

	public void writeCa() {
		NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
		this.caDay.setText(nf.format(this.dashboard.sumJournaliereFacture()) + " €");
		this.caMensuel.setText(nf.format(this.dashboard.sumMensuelFacture()) + " €");
		this.caAnnuel.setText(nf.format(this.dashboard.sumAnnuelFacture()) + " €");
	}

	@FXML
	public void openSave(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Save.fxml"));
			Parent root = loader.load();
			SaveController saveControl = loader.getController();
			saveControl.setDashboard(dashboard);
			saveControl.setConfig(config);
			Stage stage = new Stage();
			stage.setTitle("Sauvegardes");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/save.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					SaveData.writeSavePoints(savePointsFilename, dashboard.getSavePoints());
					JSONWriter<Config> writer = new JSONWriter<>();
					writer.write(config, DashboardController.configFilename);
				}
			});
			stage.initOwner((Stage) this.clientsListView.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void openClient(Client client) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Client.fxml"));
			Parent root = loader.load();
			ClientController clientControl = loader.getController();
			clientControl.setDashboard(dashboard);
			clientControl.setClient(client);
			Stage stage = new Stage();
			stage.setTitle("Fiche client");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/ficheclient.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.clientsListView.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void facturation(ActionEvent e) {
		this.openFacturationWindow();
	}

	public void openFacturationWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Facturation.fxml"));
			Parent root = loader.load();
			FacturationController facturationControl = loader.getController();
			facturationControl.setDashboard(dashboard);
			Stage stage = new Stage();
			stage.setTitle("Facturation");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/facture.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.clientsListView.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void openVentes(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Vente.fxml"));
			Parent root = loader.load();
			VenteController venteControl = loader.getController();
			venteControl.setDashboard(dashboard);
			Stage stage = new Stage();
			stage.setTitle("Ventes");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/ventes.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.clientsListView.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void openStocks(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Stocks.fxml"));
			Parent root = loader.load();
			StocksController stocksControl = loader.getController();
			stocksControl.setDashboard(dashboard);
			Stage stage = new Stage();
			stage.setTitle("Stocks");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/stocks.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.clientsListView.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void newClient() {
		Client newClient = this.dashboard.createNewClient();
		this.openClient(newClient);
	}

	@FXML
	public void clientClick(MouseEvent e) {
		if (Math.floorMod(e.getClickCount(), 2) == 0) {
			int index = this.clientsListView.getSelectionModel().getSelectedIndex();
			if (index >= 0) {
				Client client = this.filteredListClient.get(index);
				this.openClient(client);
			}

		}
	}

	@FXML
	public void openFacture(MouseEvent e) {
		if (Math.floorMod(e.getClickCount(), 2) == 0) {
			int index = this.factureListView.getSelectionModel().getSelectedIndex();
			if (index >= 0) {
				Facture facture = this.filteredListFactures.get(index);
				this.openFactureWindow(facture);
			}

		}
	}

	public void openFactureWindow(Facture facture) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Facture.fxml"));
			Parent root = loader.load();
			FactureController controller = loader.getController();
			controller.setDashboard(this.dashboard);
			controller.setFacture(facture);
			Stage stage = new Stage();
			stage.setTitle("Facture");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/all.css").toString());
			scene.getStylesheets().add(getClass().getResource("/resources/css/factureInfo.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) this.clientsListView.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void clientHasChanged() {
		this.clientsListView.refresh();
		this.comboboxDates();
	}

	@Override
	public void clientsListHasChanged() {
		this.filteredListClient = new FilteredList<>((ObservableList<Client>) this.dashboard.getClients(), client -> {
			if (this.searchTextField.getText() == null || this.searchTextField.getText().isEmpty()) {
				return true;
			}
			String lowerCaseSearch = searchTextField.getText().toLowerCase();
			return client.getFamilyName().toLowerCase().contains(lowerCaseSearch)
					|| client.getName().toLowerCase().contains(lowerCaseSearch);
		});
		this.clientsSize.setText("" + this.dashboard.getNumberOfClients());
		this.clientsListView.setItems(filteredListClient);
	}

	@Override
	public void factureCreated() {
		this.filteredListFactures = new FilteredList<>((ObservableList<Facture>) dashboard.getFactures(), facture -> {
			Integer month = this.comboMonth.getSelectionModel().getSelectedItem();
			Integer year = this.comboYear.getSelectionModel().getSelectedItem();
			if (month == null || year == null) {
				return true;
			}
			return facture.getDate().getMonthValue() == month && facture.getDate().getYear() == year;
		});

		this.factureListView.setItems(this.filteredListFactures);
		this.writeCa();
		this.drawCharts();
	}

}
