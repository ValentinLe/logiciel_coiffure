package fr.valentinle.logiciel_coiffure.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Facture {
    
    protected String fullName;
    
    protected LocalDate date;
    
    protected double amount = 0.0;
    
    protected List<Sale> sales;
    
    protected TypePaiement typePaiement;
    
    public Facture() {
	this("", null, new ArrayList<>(), TypePaiement.CB);
    }
    
	public Facture(Client client, LocalDate date, List<Sale> sales, TypePaiement typePaiement) {
	this(client.getFullName(), date, sales, typePaiement);
    }
    
    public Facture(String fullName, LocalDate date, List<Sale> sales, TypePaiement typePaiement) {
	this.fullName = fullName;
	this.date = date;
	this.amount = this.computeAmount(sales);
	this.sales = sales;
	this.typePaiement = typePaiement;
    }
    
    public Facture(String fullName, LocalDate date, TypePaiement typePaiement) {
	this(fullName, date, new ArrayList<>(), typePaiement);
    }
    
    public Facture(Client client, LocalDate date, TypePaiement typePaiement) {
	this(client, date, new ArrayList<>(), typePaiement);
    }
    
    public void computeFacture(Dashboard dashboard, Client client) {
	for (Sale sale : this.sales) {
	    sale.buy(sale, dashboard, client);
	}
    }
    
    public double sumSales() {
	return this.sumSales(this.sales);
    }
    
    public double sumSales(List<Sale> sales) {
	double sum = 0.0;
	for (Sale sale : sales) {
	    sum += sale.getPrice();
	}
	return sum;
    }
    
    public int nbProducts() {
	int cpt = 0;
	for (Sale sale : this.sales) {
	    if (sale.getType() == TypeSale.PRODUCT) {
		cpt += 1;
	    }
	}
	return cpt;
    }

    public String getFullName() {
	return fullName;
    }

    public LocalDate getDate() {
	return date;
    }

    public double getAmount() {
	return amount;
    }

    public List<Sale> getSales() {
	return sales;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public void setSales(List<Sale> sales) {
	this.sales = sales;
    }
    
    public void addSale(Sale sale) {
	this.sales.add(sale);
	this.amount = this.computeAmount(this.sales);
    }
    
    public void removeSale(Sale sale) {
	this.sales.remove(sale);
	this.amount = this.computeAmount(this.sales);
    }

    public TypePaiement getTypePaiement() {
	return typePaiement;
    }
    
    public void setAmount(double amount) {
	this.amount = amount;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
	this.typePaiement = typePaiement;
    }
    
    
    public void setClient(Client client) {
	this.fullName = client.getFullName();
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }
    
    public double computeAmount(List<Sale> sales) {
	double amount = 0.0;
	amount += this.sumSales(sales);
	return amount;
    }

    @Override
    public String toString() {
	return "Facture{" + "fullName=" + fullName + ", date=" + date + ", sales=" + sales + '}';
    }

}
