package fr.valentinle.logiciel_coiffure.model.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe permettant d'avoir un objet ecoutable
 */
public abstract class AbstractListenableClient implements ListenableClient {

    // liste des ecouteurs du model
    private transient Set<ClientListener> listeners;

    /**
     * Un objet ecoutable n'a aucun listener au depart
     */
    public AbstractListenableClient() {
	this.listeners = new HashSet<>();
    }

    /**
     * Ajoute un listener au model
     *
     * @param listener le listener a ajouter
     */
    @Override
    public void addListener(ClientListener listener) {
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
    public boolean removeListener(ClientListener listener) {
	if (listeners == null) {
	    listeners = new HashSet<>();
	}
	return listeners.remove(listener);
    }
    
    @Override
    public void clientDataHasChanged() {
	for (ClientListener l : listeners) {
	    l.clientDataHasChanged();
	}
    }

}
