package fr.valentinle.logiciel_coiffure.model;

import fr.valentinle.logiciel_coiffure.model.observer.AbstractListenableDashboard;
import fr.valentinle.logiciel_coiffure.output.JSONReader;
import fr.valentinle.logiciel_coiffure.output.JSONWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dashboard extends AbstractListenableDashboard {

	protected List<Client> clients;
	protected List<Sale> sales;
	protected List<Facture> factures;
	protected String clientFilename;
	protected String salesFilename;
	protected String facturesFilename;
	protected long maxId;
	protected List<String> savePoints;

	public Dashboard() {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, null, new ArrayList<>());
	}

	public Dashboard(String clientFilename, String salesFilename, String facturesFilename, List<String> listPoints) {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), clientFilename, salesFilename, facturesFilename,
				listPoints);
	}

	public Dashboard(List<Client> clients, List<Sale> sales, List<Facture> factures, String clientFilename,
			String salesFilename, String facturesFilename, List<String> listPoints) {

		this.clients = clients;
		this.sales = sales;
		this.factures = factures;
		this.clientFilename = clientFilename;
		this.salesFilename = salesFilename;
		this.facturesFilename = facturesFilename;
		if (clientFilename != null) {
			this.loadClients();
		}
		if (salesFilename != null) {
			this.loadSales();
		}
		if (facturesFilename != null) {
			this.loadFactures();
		}
		this.savePoints = listPoints;
	}

	public List<Client> getClients() {
		return clients;
	}

	public String getFilename() {
		return clientFilename;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public void setFilename(String filename) {
		this.clientFilename = filename;
	}

	public List<String> getSavePoints() {
		return savePoints;
	}

	public void addSavePoint(String dir) {
		if (!this.savePoints.contains(dir)) {
			this.savePoints.add(dir);
		}
	}

	public void removeSavePoint(String dir) {
		this.savePoints.remove(dir);
	}

	public void setSavePoints(List<String> savePoints) {
		this.savePoints = savePoints;
	}

	public void loadClients() {
		try {
			Type type = (ParameterizedType) Dashboard.class.getDeclaredField("clients").getGenericType();
			JSONReader<List<Client>> reader = new JSONReader<>();
			this.clients.clear();
			List<Client> clientsLoaded = reader.read(this.clientFilename, type);
			if (clientsLoaded != null) {
				for (Client client : clientsLoaded) {
					this.addClient(client, false);
				}
			}
			Collections.sort(this.clients, getClientComparator());
			this.clientsListUpdated(false);
		} catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public void loadSales() {
		try {
			Type type = (ParameterizedType) Dashboard.class.getDeclaredField("sales").getGenericType();
			JSONReader<List<Sale>> reader = new JSONReader<>();
			this.sales.clear();
			List<Sale> salesLoaded = reader.read(this.salesFilename, type);
			if (salesLoaded != null) {
				this.sales.addAll(salesLoaded);
			}
			Collections.sort(this.sales, getSaleComparator());
		} catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public void loadFactures() {
		try {
			Type type = (ParameterizedType) Dashboard.class.getDeclaredField("factures").getGenericType();
			JSONReader<List<Facture>> reader = new JSONReader<>();
			this.factures.clear();
			List<Facture> facturesLoaded = reader.read(this.facturesFilename, type);
			if (facturesLoaded != null) {
				this.factures.addAll(facturesLoaded);
			}
			Collections.sort(this.factures, getFactureComparator());
		} catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public Comparator<Facture> getFactureComparator() {
		Comparator<Facture> c = new Comparator<Facture>() {
			@Override
			public int compare(Facture o1, Facture o2) {
				int comp = o1.getDate().compareTo(o2.getDate());
				if (comp == 0) {
					return new Integer(factures.indexOf(o2)).compareTo(new Integer(factures.indexOf(o1)));
				}
				return comp;
			}
		};
		return c.reversed();
	}

	public Comparator<Client> getClientComparator() {
		Comparator<Client> c = new Comparator<Client>() {
			@Override
			public int compare(Client o1, Client o2) {
				return o1.getFullName().compareTo(o2.getFullName());
			}
		};
		return c;
	}

	public Comparator<Sale> getSaleComparator() {
		Comparator<Sale> c = new Comparator<Sale>() {
			@Override
			public int compare(Sale o1, Sale o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};
		return c;
	}

	public boolean sameYear(LocalDate d1, LocalDate d2) {
		return d1.getYear() == d2.getYear();
	}

	public boolean sameMonth(LocalDate d1, LocalDate d2) {
		return d1.getMonth() == d2.getMonth() && this.sameYear(d1, d2);
	}

	public boolean sameDay(LocalDate d1, LocalDate d2) {
		return d1.getDayOfYear() == d2.getDayOfYear() && this.sameYear(d1, d2);
	}

	public double sumJournaliereFacture() {
		return this.sumJournaliereFacture(LocalDate.now());
	}

	public double sumJournaliereFacture(LocalDate date) {
		double sum = 0.0;
		for (Facture facture : this.factures) {
			if (this.sameDay(facture.getDate(), date)) {
				sum += facture.getAmount();
			}
		}
		return sum;
	}

	public double sumMensuelFacture() {
		return this.sumMensuelFacture(LocalDate.now());
	}

	public double sumMensuelFacture(LocalDate date) {
		double sum = 0.0;
		for (Facture facture : this.factures) {
			if (this.sameMonth(facture.getDate(), date)) {
				sum += facture.getAmount();
			}
		}
		return sum;
	}

	public double sumAnnuelFacture() {
		return this.sumAnnuelFacture(LocalDate.now());
	}

	public double sumAnnuelFacture(LocalDate date) {
		double sum = 0.0;
		for (Facture facture : this.factures) {
			if (this.sameYear(facture.getDate(), date)) {
				sum += facture.getAmount();
			}
		}
		return sum;
	}

	public double getGainsOf(List<Facture> factures, TypeSale typeSale) {
		double sum = 0.0;
		for (Facture facture : factures) {
			for (Sale sale : facture.getSales()) {
				if (sale.getType() == typeSale) {
					sum += sale.getPrice();
				}
			}
		}
		return sum;
	}

	public double getGainsOf(TypeSale typeSale) {
		return this.getGainsOf(this.factures, typeSale);
	}

	public List<Sale> getProducts() {
		List<Sale> products = new ArrayList<>();
		for (Sale sale : this.sales) {
			if (sale.getType() == TypeSale.PRODUCT) {
				products.add(sale);
			}
		}
		return products;
	}

	public List<Facture> getFacturesOfYear(LocalDate date) {
		List<Facture> facturesYear = new ArrayList<>();
		for (Facture facture : this.factures) {
			if (this.sameYear(facture.getDate(), date)) {
				facturesYear.add(facture);
			}
		}
		return facturesYear;
	}

	public List<Facture> getFacturesOfMonth(LocalDate date) {
		return this.getFacturesOfMonth(date.getMonthValue(), date.getYear());
	}

	public List<Facture> getFacturesOfMonth(int month, int year) {
		List<Facture> facturesYear = new ArrayList<>();
		for (Facture facture : this.factures) {
			if (facture.getDate().getMonthValue() == month && facture.getDate().getYear() == year) {
				facturesYear.add(facture);
			}
		}
		return facturesYear;
	}

	public Map<Integer, Map<Integer, LocalDate>> getMapMonthYearFactures() {
		Map<Integer, Map<Integer, LocalDate>> mapDates = new HashMap<>();
		for (Facture facture : this.factures) {
			LocalDate date = facture.getDate();
			if (!mapDates.containsKey(date.getYear())) {
				mapDates.put(date.getYear(), new HashMap<>());
			}
			mapDates.get(date.getYear()).put(date.getMonthValue(), date);
		}
		return mapDates;
	}

	public List<Integer> getFacturesYears() {
		Set<Integer> facturesYearsSet = new HashSet<>();
		for (Facture facture : this.factures) {
			facturesYearsSet.add(facture.getDate().getYear());
		}
		List<Integer> facturesYears = new ArrayList<>(facturesYearsSet);
		facturesYears.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		}.reversed());
		return facturesYears;
	}

	public void addFacture(Facture facture) {
		this.factures.add(0, facture);
		this.clientHasChanged();
		this.factureCreated();
	}

	public void removeFacture(Facture facture) {
		int index = this.factures.indexOf(facture);
		if (0 <= index && index < this.factures.size()) {
			this.factures.remove(facture);
			this.clientHasChanged();
			this.factureCreated();

		}
	}

	public List<Facture> getFactures() {
		return factures;
	}

	public String getClientFilename() {
		return clientFilename;
	}

	public String getSalesFilename() {
		return salesFilename;
	}

	public String getFacturesFilename() {
		return facturesFilename;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public void setFactures(List<Facture> factures) {
		this.factures = factures;
	}

	public void setClientFilename(String clientFilename) {
		this.clientFilename = clientFilename;
	}

	public void setSalesFilename(String salesFilename) {
		this.salesFilename = salesFilename;
	}

	public void setFacturesFilename(String facturesFilename) {
		this.facturesFilename = facturesFilename;
	}

	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public long getMaxId() {
		if (!this.clients.isEmpty()) {
			Client clientMaxId = Collections.max(this.clients, Comparator.comparingLong(Client::getId));
			return clientMaxId.getId();
		} else {
			return 0;
		}
	}

	public void saveClients() {
		JSONWriter<List<Client>> writer = new JSONWriter<>();
		writer.write(this.clients, this.clientFilename);
	}

	public void saveSales() {
		JSONWriter<List<Sale>> writer = new JSONWriter<>();
		writer.write(this.sales, this.salesFilename);
	}

	public void saveFactures() {
		JSONWriter<List<Facture>> writer = new JSONWriter<>();
		writer.write(this.factures, this.facturesFilename);
	}

	public void save() {
		this.saveClients();
		this.saveSales();
		this.saveFactures();
	}

	public int getNumberOfClients() {
		return this.clients.size();
	}

	public boolean addClient(Client client) {
		return this.addClient(client, false);
	}

	public boolean addClient(Client client, boolean saveToFile) {
		boolean ret = this.clients.add(client);
		this.clientsListUpdated(saveToFile);
		return ret;
	}

	public boolean removeClient(Client client) {
		boolean r = this.removeClient(client, false);
		return r;
	}

	public boolean removeClient(Client client, boolean saveToFile) {
		boolean ret = this.clients.remove(client);
		this.clientsListUpdated(saveToFile);
		return ret;
	}

	public void clientsListUpdated(boolean saveToFile) {
		this.maxId = this.getMaxId();
		if (saveToFile) {
			this.saveClients();
		}
		this.clientHasChanged();
		this.clientsListHasChanged();
	}

	public Client getClientAt(int index) {
		return this.clients.get(index);
	}

	public Client getClientWithId(long id) {
		for (Client client : this.clients) {
			if (client.getId() == id) {
				return client;
			}
		}
		return null;
	}

	public boolean clientExists(Client client) {
		return this.clients.contains(client);
	}

	public boolean clientExists(long id) {
		for (Client client : this.clients) {
			if (client.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public Client createNewClient() {
		return new Client(this.maxId + 1);
	}

	public void addQuantityProduct(Sale product, int quantity) {
		product.setQuantity(product.getQuantity() + quantity);
	}

	public void removeQuantityProduct(Sale product, int quantity) {
		product.setQuantity(product.getQuantity() - quantity);
		if (product.getQuantity() < 0) {
			product.setQuantity(0);
		}
	}

	@Override
	public String toString() {
		String res = "Dashboard{\n" + "clients=\n";
		for (Client c : this.clients) {
			res += "\t" + c + "\n";
		}
		res += this.sales + "\n";
		res += this.factures;
		res += "}";
		return res;
	}
}
