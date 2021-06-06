package fr.valentinle.logiciel_coiffure.model.observer;

public interface DashboardListener {

    public void clientsListHasChanged();
    
    public void clientHasChanged();
    
    public void factureCreated();

}
