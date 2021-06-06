package fr.valentinle.logiciel_coiffure.model;

import java.util.Objects;

public class Sale {
    
    protected String name;
    
    protected double price;
    
    protected TypeSale type;
    
    protected int quantity;
    
    public Sale(String name, double price, TypeSale type, int quantity) {
	this.name = name;
	this.price = price;
	this.type = type;
	this.quantity = quantity;
    }
    
    public Sale(String name, double price, TypeSale type) {
	this(name, price, type, 0);
    }
    
    public double getPrice() {
	return this.price;
    }

    public String getName() {
	return name;
    }

    public TypeSale getType() {
	return type;
    }
    
    public void buy(Sale sale, Dashboard dashboard, Client client) {
	if (sale.getType() == TypeSale.PRODUCT) {
	    dashboard.removeQuantityProduct(sale, 1);
	    if (client != null && !client.getProductsSold().contains(sale.getName())) {
		client.getProductsSold().add(sale.getName());
	    }
	}
    }

    public int getQuantity() {
	return quantity;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public void setType(TypeSale type) {
	this.type = type;
    }

    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + Objects.hashCode(this.name);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Sale other = (Sale) obj;
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return name;
    }
    
}
