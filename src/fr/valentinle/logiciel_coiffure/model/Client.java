package fr.valentinle.logiciel_coiffure.model;

import fr.valentinle.logiciel_coiffure.model.observer.AbstractListenableClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Client extends AbstractListenableClient {

    protected long id;
    
    protected String name;
    
    protected String familyName;
    
    protected String address;
    
    protected String city;
    
    protected String postalCode;
    
    protected String phoneNumber;
    
    protected LocalDate lastVisitDate;
    
    protected int fidelity;
    
    protected List<Recipe> recipes;
    
    protected List<String> productsSold;

	protected String memo;
    
    public final static int MAX_FIDELITY = 7;

    public Client(
	    long id, 
	    String name, 
	    String familyName, 
	    String address, 
	    String city, 
	    String postalCode, 
	    String phoneNumber, 
	    LocalDate lastVisitDate, 
	    int fidelity, 
	    List<Recipe> recipes, 
	    List<String> productsSold,
		String memo) {
	
	this.id = id;
	this.name = StringUtils.capitalize(name);
	this.familyName = familyName.toUpperCase();
	this.address = address;
	this.city = StringUtils.capitalize(city);
	this.postalCode = postalCode;
	this.phoneNumber = phoneNumber;
	this.lastVisitDate = lastVisitDate;
	this.fidelity = fidelity;
	this.recipes = recipes;
	this.productsSold = productsSold;
	this.memo = memo;
    }
    
    public Client(long id) {
	this(id, "", "", "", "", "", "", null, 0, new ArrayList<>(), new ArrayList<>(), "");
    }

    public long getId() {
	return id;
    }

    public int getFidelity() {
	return fidelity;
    }

    public List<Recipe> getRecipes() {
	return recipes;
    }
    
    public String getName() {
	return name;
    }

    public String getFamilyName() {
	return familyName;
    }
    
    public String getFullName() {
	return this.getFamilyName() + " " + this.getName();
    }

    public String getAddress() {
	return address;
    }

    public String getCity() {
	return city;
    }

    public String getPostalCode() {
	return postalCode;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public LocalDate getLastVisitDate() {
	return lastVisitDate;
    }

    public List<String> getProductsSold() {
	return productsSold;
    }

	public String getMemo(){
		return memo;
	}

    public void setId(int id) {
	this.id = id;
    }

    public void setFidelity(int fidelity) {
	this.fidelity = fidelity;
    }

    public void setRecipes(List<Recipe> recipes) {
	this.recipes = recipes;
    }

    public void setName(String name) {
	this.name = StringUtils.capitalize(name);
    }

    public void setFamilyName(String familyName) {
	this.familyName = familyName.toUpperCase();
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public void setCity(String city) {
	this.city = StringUtils.capitalize(city);
    }

    public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public void setLastVisitDate(LocalDate lastVisitDate) {
	this.lastVisitDate = lastVisitDate;
    }

    public void setProductsSold(List<String> productsSold) {
	this.productsSold = productsSold;
    }

	public void setMemo(String memo) {
		this.memo = memo;
	}
    
    public boolean freePrestation() {
	return this.fidelity == MAX_FIDELITY;
    }
    
    public void passageClient(LocalDate date, boolean incFidelity) {
	this.setLastVisitDate(date);
	if (incFidelity) {
	    this.fidelity += 1;
	    if (this.fidelity > MAX_FIDELITY) {
		this.fidelity = 0;
	    }
	}
    }
    
    public Comparator<Recipe> getRecipeComparator() {
	Comparator<Recipe> c =  new Comparator<Recipe>() {
	    @Override
	    public int compare(Recipe o1, Recipe o2) {
		return o1.getDate().compareTo(o2.getDate());
	    }
	};
	return c.reversed();
    }
    
    public void addRecipe(Recipe recipe) {
	this.recipes.add(recipe);
	this.clientDataHasChanged();
    }
    
    public void removeRecipeAt(int index) {
	this.recipes.remove(index);
	this.clientDataHasChanged();
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
	final Client other = (Client) obj;
	if (this.id != other.id) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "Client{" + "id=" + id + ", name=" + name + ", familyName=" + familyName + ", address=" + address + ", city=" + city + ", postalCode=" + postalCode + ", phoneNumber=" + phoneNumber + ", lastVisitDate=" + lastVisitDate + ", fidelity=" + fidelity + ", recipes=" + recipes + ", productsSold=" + productsSold + '}';
    }
    
    
    
}
