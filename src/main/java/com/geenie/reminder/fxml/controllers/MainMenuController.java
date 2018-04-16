package com.geenie.reminder.fxml.controllers;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.geenie.reminder.config.StageManager;
import com.geenie.reminder.view.FxmlView;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

@Controller
public class MainMenuController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

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
    private JFXComboBox<?> priorityComboBox;

    @FXML
    private JFXDatePicker eventTimeTF;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		switchPanes(parentPane.getId());

	}

	@FXML
	void addEvent(ActionEvent event) {
		switchPanes(addEvent.getId());
	}

	@FXML
	void disconnect(ActionEvent event) {
		stageManager.switchScene(FxmlView.LOGIN);
	}
	
	@FXML
	void clearEvent(ActionEvent event) {
		//TODO
	}
	
	@FXML
	void saveEvent(ActionEvent event) {
		//TODO
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

}
