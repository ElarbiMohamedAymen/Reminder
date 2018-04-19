package com.geenie.reminder.fxml.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.geenie.reminder.config.StageManager;
import com.geenie.reminder.entities.Remind;
import com.geenie.reminder.enumerations.Priority;
import com.geenie.reminder.service.interfaces.IRemindMySQLService;
import com.geenie.reminder.view.FxmlView;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Controller
public class MainMenuController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Lazy
	@Autowired
	@Qualifier("remindMySQLServiceImpl")
	IRemindMySQLService remindService;

	@FXML
	private AnchorPane parentPane;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private AnchorPane addEvent;

	@FXML
	private MenuBar menuBar;
	@FXML
	private JFXTextField eventNameTF;

	@FXML
	private JFXTextArea eventDescriptionTF;

	@FXML
	private JFXDatePicker eventDateTF;

	@FXML
	private JFXComboBox<String> priorityComboBox;

	@FXML
	private JFXDatePicker eventTimeTF;

	private List<String> stateAsList = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		switchPanes(mainPane.getId());
		createAllEventScrollingList();
	}

	@FXML
	void addEvent(ActionEvent event) {
		switchPanes(addEvent.getId());
		priorityComboBox.getItems().clear();
		Priority[] states = Priority.class.getEnumConstants();
		for (Priority state : states) {
			stateAsList.add(state.getStatus());
		}
		Set<String> foo = new HashSet<>(stateAsList);
		priorityComboBox.getItems().addAll(foo);
	}

	@FXML
	void disconnect(ActionEvent event) {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	@FXML
	void clearEvent(ActionEvent event) {
		resetEventValue();
	}

	private void resetEventValue() {
		eventNameTF.clear();
		eventDescriptionTF.clear();
		priorityComboBox.getSelectionModel().clearSelection();
		eventDateTF.setValue(null);
		eventTimeTF.setValue(null);
	}

	@FXML
	void backtoMain(ActionEvent event) {
		switchPanes(mainPane.getId());
		resetEventValue();
		createAllEventScrollingList();
	}

	@FXML
	void saveEvent(ActionEvent event) {
		if (validateInputs()) {
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			Remind remind = new Remind();
			remind.setName(eventNameTF.getText());
			remind.setDescription(eventDescriptionTF.getText());
			remind.setDate(eventDateTF.getValue().format(formatters));
			remind.setTime(eventTimeTF.getTime().toString());
			String priorityAsString = priorityComboBox.getSelectionModel().getSelectedItem();
			if (priorityAsString != null) {
				switch (priorityAsString) {
				case "Urgent":
					remind.setPriority(Priority.URGENT);
					break;
				case "Pas Vraiment":
					remind.setPriority(Priority.PAS_VRAIMENT);
					break;
				case "Normal":
					remind.setPriority(Priority.NORMAL);
					break;
				default:
					remind.setPriority(Priority.PAS_VRAIMENT);
					break;
				}
			} else {
				remind.setPriority(Priority.PAS_VRAIMENT);
			}
			remindService.addEvent(remind);
		} else {
			// TODO
		}

	}

	private boolean validateInputs() {

		String eventName = eventNameTF.getText();
		String priorityAsString = priorityComboBox.getSelectionModel().getSelectedItem();
		String description = eventDescriptionTF.getText();
		// String eventDate = eventDateTF.getValue().toString();
		// String eventTime = eventTimeTF.getTime().toString();
		return (eventName != null && priorityAsString != null && description != null
				&& eventDateTF.getValue().toString() != null && eventTimeTF.getTime().toString() != null);
	}

	private void switchPanes(String name) {
		for (Iterator<Node> it = parentPane.getChildren().iterator(); it.hasNext();) {
			while (it.hasNext()) {
				Node node = it.next();
				if (node.getId().equals(name)) {
					node.setVisible(true);
				} else {
					node.setVisible(false);
				}
			}
		}
		menuBar.setVisible(true);
	}

	private void createAllEventScrollingList() {
		VBox content = new VBox(5);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setFitToWidth(true);
		List<Remind> lr = remindService.getAllEvents();
		for (Remind remind : lr) {
			AnchorPane anchorPane = new AnchorPane();
			String style;
			switch (remind.getPriority()) {
			case NORMAL:
				style = String.format("-fx-background: rgb(67, 140, 129);" + "-fx-background-color: -fx-background;");
				break;
			case PAS_VRAIMENT:
				style = String.format("-fx-background: rgb(255, 0, 182);" + "-fx-background-color: -fx-background;");
				break;
			case URGENT:
				style = String.format("-fx-background: rgb(255, 0, 0);" + "-fx-background-color: -fx-background;");
				break;

			default:
				style = String.format("-fx-background: rgb(0, 0, 0);" + "-fx-background-color: -fx-background;");
				break;
			}

			anchorPane.setStyle(style);
			Label label = new Label(remind.getName() + "\n" + remind.getDescription());
			label.setTextFill(Color.web("#ffffff"));
			AnchorPane.setLeftAnchor(label, 5.0);
			AnchorPane.setTopAnchor(label, 5.0);
			Button button = new Button("Remove");
			button.setOnAction(evt -> content.getChildren().remove(anchorPane));
			AnchorPane.setRightAnchor(button, 5.0);
			AnchorPane.setTopAnchor(button, 5.0);
			AnchorPane.setBottomAnchor(button, 5.0);
			anchorPane.getChildren().addAll(label, button);
			anchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					System.out.println(remind.getIdReminder());
				}
			});
			content.getChildren().add(anchorPane);
		}
		scroller.setPrefHeight(mainPane.getPrefHeight());
		scroller.setPrefWidth(mainPane.getPrefWidth());

		BorderPane borderPane = new BorderPane(scroller, null, null, null, null);
		mainPane.getChildren().add(borderPane);
	}

}
