package fr.valentinle.logiciel_coiffure.model.observer;

public interface ListenableClient {
    
    /**
     * Ajoute un listener au model
     * @param listener le listener a ajouter
     */
    public void addListener(ClientListener listener);
    
    /**
     * Supprime un listener du model si il est present dans la liste des listener
     * @param listener le listener a supprimer
     * @return true si le model contient le listener, false sinon
     */
    public boolean removeListener(ClientListener listener);
    
    public void clientDataHasChanged();
}
