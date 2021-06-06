package fr.valentinle.logiciel_coiffure.model;

public enum TypePaiement {
    CB("CB"), ESP("ESP"), CHQ("CHQ");
    
    private String name;
    
    private TypePaiement(String name) {
	this.name = name;
    }
    
    @Override
    public String toString() {
	return name;
    }
}
