package de.seifi.rechnung_manager_app.fx_services;

public interface IBindingService <T> {

    boolean isEditingMode();

    T addNewRow();
}
