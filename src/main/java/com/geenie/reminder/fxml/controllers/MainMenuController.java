package com.geenie.reminder.fxml.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.geenie.reminder.config.StageManager;
import com.geenie.reminder.entities.Remind;
import com.geenie.reminder.enumerations.Priority;
import com.geenie.reminder.enumerations.State;
import com.geenie.reminder.service.interfaces.IRemindMySQLService;
import com.geenie.reminder.view.FxmlView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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

	@FXML
	private JFXButton backBtn;

	@FXML
	private JFXButton persistEvent;

	private List<String> stateAsList = new ArrayList<>();

	private Remind remindToUpdate = null;

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
		Image image = new Image("/buttons/circled-left-2-25.png");
		backBtn.setGraphic(new ImageView(image));

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
		persistEvent.setText("Save");
		switchPanes(mainPane.getId());
		resetEventValue();
		createAllEventScrollingList();
	}

	@FXML
	void saveEvent(ActionEvent event) {
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Remind remind;
		if (remindToUpdate == null) {
			remind = new Remind();
		} else {
			remind = remindToUpdate;
		}
		if (validateInputs()) {
			remind.setName(eventNameTF.getText().trim());
			remind.setDescription(eventDescriptionTF.getText().trim());
			try {
				remind.setDate(eventDateTF.getValue().format(formatters));
			} catch (NullPointerException e) {
				remind.setDate(null);
			}
			try {
				remind.setTime(eventTimeTF.getTime().toString());
			} catch (NullPointerException e) {
				remind.setTime(null);
			}
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
		} else {
			if (eventNameTF.getText().trim().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Reminder");
				alert.setHeaderText(null);
				alert.setContentText("You must pick a name for the event");
				alert.showAndWait();
				return;
			} else {
				remind.setName(eventNameTF.getText().trim());
				if (eventDescriptionTF.getText().trim().isEmpty()) {
					remind.setDescription(null);
				}
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
			}

		}
		if ("Save".equals(persistEvent.getText())) {
			remind.setState(State.TODO);
			remindToUpdate = null;
		}
		remindService.addEvent(remind);
		persistEvent.setText("Save");
		backtoMain(event);

	}

	private boolean validateInputs() {

		String eventName = eventNameTF.getText().trim();
		String description = eventDescriptionTF.getText().trim();
		return (eventName != null &&  description != null);
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
		List<Remind> lr = remindService.getAllVisibleEvents();
		for (Remind remind : lr) {
			ContextMenu cm = createCutomizedContextMenu(remind);
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
			Label label;
			if (remind.getDescription() != null) {
				label = new Label(remind.getName() + "\n" + remind.getDescription());
			} else {
				label = new Label(remind.getName());
			}

			label.setTextFill(Color.web("#ffffff"));
			AnchorPane.setLeftAnchor(label, 5.0);
			AnchorPane.setTopAnchor(label, 5.0);
			Label status = new Label(remind.getState().getStatus());
			status.setTextFill(Color.web("#ffffff"));
			AnchorPane.setRightAnchor(status, 5.0);
			AnchorPane.setTopAnchor(status, 5.0);
			AnchorPane.setBottomAnchor(status, 5.0);
			anchorPane.getChildren().addAll(label, status);
			anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (e.getButton() == MouseButton.SECONDARY) {
						cm.show(anchorPane, e.getScreenX(), e.getScreenY());
					} else {
						remindService.updateEvent(remind);
						status.setText(remind.getState().getStatus());
					}
				}
			});

			content.getChildren().add(anchorPane);
		}
		scroller.setPrefHeight(mainPane.getPrefHeight());
		scroller.setPrefWidth(mainPane.getPrefWidth());

		BorderPane borderPane = new BorderPane(scroller, null, null, null, null);
		mainPane.getChildren().add(borderPane);
	}

	private ContextMenu createCutomizedContextMenu(Remind remind) {
		final ContextMenu cm = new ContextMenu();
		cm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String choice = ((MenuItem) event.getTarget()).getText();
				switch (choice) {
				case "Delete":
					remindService.hideRemind(remind);
					createAllEventScrollingList();
					break;
				case "Update":
					cm.hide();
					updateReminder(remind);
					break;
				default:
					break;
				}
			}

		});
		MenuItem menuItem1 = new MenuItem("Delete");
		MenuItem menuItem2 = new MenuItem("Update");

		cm.getItems().addAll(menuItem1, menuItem2);

		return cm;
	}

	private void updateReminder(Remind remind) {
		remindToUpdate = remind;
		persistEvent.setText("Update");
		switchPanes(addEvent.getId());
		priorityComboBox.getItems().clear();
		Priority[] states = Priority.class.getEnumConstants();
		for (Priority state : states) {
			stateAsList.add(state.getStatus());
		}
		Set<String> foo = new HashSet<>(stateAsList);
		priorityComboBox.getItems().addAll(foo);
		for (String s : foo) {
		    if(s.equals(remind.getPriority().getStatus())){
		    	priorityComboBox.setValue(s);
		    }
		}
		Image image = new Image("/buttons/circled-left-2-25.png");
		backBtn.setGraphic(new ImageView(image));

		eventNameTF.setText(remind.getName());
		eventDescriptionTF.setText(remind.getDescription());

		if (remind.getDate() != null) {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate eventDate = LocalDate.parse(remind.getDate(), df);
			eventDateTF.setValue(eventDate);
		}

		if (remind.getTime() != null) {
			LocalTime eventTime = LocalTime.parse(remind.getTime());
			eventTimeTF.setTime(eventTime);
		}
	}

}
