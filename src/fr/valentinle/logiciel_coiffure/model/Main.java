package fr.valentinle.logiciel_coiffure.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	Client c1 = new Client(1);
	c1.setFamilyName("jojo");
	
	Recipe r1 = new Recipe(LocalDate.now(), "30g de machin", "35 min");
	Recipe r2 = new Recipe(LocalDate.now(), "5g de truc", "1h45");
	
	Client c2 = new Client(2, "patrick", "morice", "10 rue dampierre", "paris", "27320", "06.00.00.00.00", LocalDate.now(), 2, Arrays.asList(r1), new ArrayList<>(), "");
	
	Client c3 = new Client(3, "jack", "yola", "48 rue joter", "marseille", "23454", "06.23.65.24.96", LocalDate.now(), 5, Arrays.asList(r2), new ArrayList<>(), "");
	
	List<Client> clients = Arrays.asList(c1, c2, c3);
	
	String clientFilename = "src/resources/data/clients/clients.json";
	String salesFilename = "src/resources/data/clients/clients.json";
	String facturesFilename = "src/resources/data/clients/clients.json";
	Dashboard dashboard = new Dashboard(clientFilename, salesFilename, facturesFilename, new ArrayList<>());
	dashboard.addClient(c1);
	dashboard.addClient(c2);
	dashboard.addClient(c3);
	dashboard.saveClients();
    }
}
