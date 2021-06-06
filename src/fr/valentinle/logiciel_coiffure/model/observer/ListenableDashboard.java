
package fr.valentinle.logiciel_coiffure.model.observer;

/**
 * Interface representant un listener de model
 */
public interface ListenableDashboard {
    
    /**
     * Ajoute un listener au model
     * @param listener le listener a ajouter
     */
    public void addListener(DashboardListener listener);
    
    /**
     * Supprime un listener du model si il est present dans la liste des listener
     * @param listener le listener a supprimer
     * @return true si le model contient le listener, false sinon
     */
    public boolean removeListener(DashboardListener listener);
    
    
    public void clientsListHasChanged();
    
    public void clientHasChanged();
    
    public void factureCreated();
        
}
