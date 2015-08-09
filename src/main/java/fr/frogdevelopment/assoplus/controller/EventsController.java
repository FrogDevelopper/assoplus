/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.Main;
import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.CategoryDto;
import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.service.CategoriesService;
import fr.frogdevelopment.assoplus.service.EventsService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller("eventController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventsController implements Initializable {

	private static final int MAX_CHAR = 500;

	private DateTimeFormatter dateTimeFormatter;

	private ResourceBundle resources;

	@Autowired
	private EventsService eventsService;

	@Autowired
	private CategoriesService categoriesService;

	@FXML
	private TextField txtTitle;
	@FXML
	private DatePicker dpDate;
	@FXML
	private ComboBox<CategoryDto> cbCategory;
	@FXML
	private Label countChar;
	@FXML
	private TextArea taText;

	@FXML
	private Button btnCategories;

	@FXML
	private Button btnSave;
	@FXML
	private Button btnUpdate;

	@FXML
	private TableView<EventDto> tableView;

	private ObservableList<EventDto> data;

	private EventDto currentData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
		dateTimeFormatter = DateTimeFormatter.ofPattern(resources.getString("global.date.format"));

		data = eventsService.getAllData();
		tableView.setItems(data);


		MaskHelper.addMaskDate(dpDate);
		dpDate.setPromptText(resources.getString("global.date.format.prompt"));
		dpDate.setConverter(new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateTimeFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateTimeFormatter);
				} else {
					return null;
				}
			}
		});

		cbCategory.setItems(categoriesService.getAllData());
		cbCategory.setVisibleRowCount(4);
		cbCategory.setConverter(new StringConverter<CategoryDto>() {

			private final Map<String, CategoryDto> _cache = new HashMap<>();

			@Override
			public String toString(CategoryDto item) {
				_cache.put(item.getCode(), item);
				return item.getCode();
			}

			@Override
			public CategoryDto fromString(String code) {
				return _cache.get(code);
			}
		});

		tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, eventDto) -> {
			if (eventDto != null) {
				currentData = eventDto;
				txtTitle.setText(eventDto.getTitle());
				dpDate.setValue(LocalDate.parse(eventDto.getDate(), dateTimeFormatter));
				Optional<CategoryDto> first = cbCategory.getItems().stream().filter(categoryDto -> categoryDto.getCode().equals(eventDto.getCategoryCode())).limit(1).findFirst();
				cbCategory.setValue(first.get());
				taText.setText(eventDto.getText());

				btnSave.setDisable(true);
				btnUpdate.setDisable(false);
			} else {
				btnSave.setDisable(false);
				btnUpdate.setDisable(true);

				txtTitle.setText(null);
				dpDate.setValue(null);
				cbCategory.setValue(null);
				taText.setText(null);
			}
		});

		taText.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				countChar.setText(newValue.length() + "/" + MAX_CHAR);
			} else {
				countChar.setText("0/" + MAX_CHAR);
			}
		});

		taText.setOnKeyTyped(event -> {
			String text = taText.getText();
			int length = text.length();
			if (length >= MAX_CHAR - 1) {
				taText.setText(text.substring(0, MAX_CHAR - 1));
				taText.positionCaret(length);
			}
		});
	}

	public void saveData() {
		if (isAllFieldsOK()) {
			EventDto dto = fillDto(new EventDto());
			eventsService.saveData(dto);

			data.add(dto);
		}
	}

	public void updateData() {
		if (isAllFieldsOK()) {
			eventsService.updateData(fillDto(currentData));
		}
	}

	private boolean isAllFieldsOK() {
		boolean isOk = Validator.validate(txtTitle);
		isOk &= Validator.validate(dpDate);
		isOk &= Validator.validate(cbCategory);
		isOk &= Validator.validate(taText);

		return isOk;
	}

	private EventDto fillDto(EventDto dto) {
		dto.setTitle(txtTitle.getText());
		dto.setDate(dpDate.getValue().format(dateTimeFormatter));
		dto.setCategoryCode(cbCategory.getValue().getCode());
		dto.setText(taText.getText());

		return dto;
	}

	public void manageCategories(MouseEvent event) {
		Button source = (Button) (event.getSource());
		Window parent = source.getScene().getWindow();

		Parent root = Main.load("/fxml/events/categories.fxml");
		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(parent);
		dialog.setTitle(resources.getString("event.categories.manage"));
		dialog.setScene(new Scene(root, 450, 450));

		dialog.setOnCloseRequest(event1 -> cbCategory.setItems(categoriesService.getAllData()));

		dialog.show();
	}
}
