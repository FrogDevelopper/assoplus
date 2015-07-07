/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class LicenceDto implements ReferenceDto {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();
	private SimpleSetProperty<OptionDto> options = new SimpleSetProperty<>(FXCollections.observableSet());

	public LicenceDto(String code, String label) {
		this.code.setValue(code);
		this.label.setValue(label);
	}

	public LicenceDto() {

	}

	public long getId() {
		return id.get();
	}

	public SimpleLongProperty idProperty() {
		return id;
	}

	public void setId(long id) {
		this.id.set(id);
	}

	@Override
	public String getCode() {
		return code.get();
	}

	public SimpleStringProperty codeProperty() {
		return code;
	}

	public void setCode(String code) {
		this.code.set(code);
	}

	@Override
	public String getLabel() {
		return label.get();
	}

	public SimpleStringProperty labelProperty() {
		return label;
	}

	public void setLabel(String label) {
		this.label.set(label);
	}

	public ObservableSet<OptionDto> getOptions() {
		return options.get();
	}

	public SimpleSetProperty<OptionDto> optionsProperty() {
		return options;
	}

	public void setOptions(ObservableSet<OptionDto> options) {
		this.options.set(options);
	}
}
