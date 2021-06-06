package fr.valentinle.logiciel_coiffure.model.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe permettant d'avoir un objet ecoutable
 */
public abstract class AbstractListenableDashboard implements ListenableDashboard {

    // liste des ecouteurs du model
    private transient Set<DashboardListener> listeners;

    /**
     * Un objet ecoutable n'a aucun listener au depart
     */
    public AbstractListenableDashboard() {
	this.listeners = new HashSet<>();
    }

    /**
     * Ajoute un listener au model
     *
     * @param listener le listener a ajouter
     */
    @Override
    public void addListener(DashboardListener listener) {
	if (listeners == null) {
	    listeners = new HashSet<>();
	}
	listeners.add(listener);
    }

    /**
     * Supprime un listener du model si il est present dans la liste des
     * listener
     *
     * @param listener le listener a supprimer
     * @return true si le model contient le listener, false sinon
     */
    @Override
    public boolean removeListener(DashboardListener listener) {
	if (listeners == null) {
	    listeners = new HashSet<>();
	}
	return listeners.remove(listener);
    }

    /**
     * Indique a tous les listeners que la liste des clients a changee
     */
    @Override
    public void clientsListHasChanged() {
	for (DashboardListener l : listeners) {
	    l.clientsListHasChanged();
	}
    }
    
    @Override
    public void clientHasChanged() {
	for (DashboardListener l : listeners) {
	    l.clientHasChanged();
	}
    }
    
    @Override
    public void factureCreated() {
	for (DashboardListener l : listeners) {
	    l.factureCreated();
	}
    }

}
