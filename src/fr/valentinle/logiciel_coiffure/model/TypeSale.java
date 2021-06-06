package fr.valentinle.logiciel_coiffure.model;

public enum TypeSale {
    PRESTATION("Préstation"), PRODUCT("Produit");
    
    private String name;
    
    private TypeSale(String name) {
	this.name = name;
    }
    
    @Override
    public String toString() {
	return name;
    }
}
