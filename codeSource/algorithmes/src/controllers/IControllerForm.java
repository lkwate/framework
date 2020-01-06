package controllers;

import java.util.LinkedHashMap;

public interface IControllerForm {
    /**
     * Annuler
     * Valider
     * Insertion
     * Supprimer
     */
    /**
     * Perform some action when button 'Annuler' is clicked
     */
    public void annuler();

    /**
     * Perform some action when button 'Valider' is clicked
     */
    public void valider();

    /**
     * Perform some action when button 'Insertion' is clicked
     */
    public void insertion();

    /**
     * Perform some action when button 'Supprimer' is clicked
     */
    public void suppression();

    /**
     * @param operationName
     * @param parameter
     */
    public void applyOperation(String operationName, LinkedHashMap<String, String> parameter);
}
