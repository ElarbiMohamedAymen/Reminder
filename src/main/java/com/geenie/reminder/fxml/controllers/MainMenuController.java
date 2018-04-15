package com.geenie.reminder.fxml.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.geenie.reminder.config.StageManager;
import com.geenie.reminder.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

@Controller
public class MainMenuController implements Initializable{
	
	@Lazy
    @Autowired
    private StageManager stageManager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
    @FXML
    void addEvent(ActionEvent event) {

    }

    @FXML
    void disconnect(ActionEvent event) {
		stageManager.switchScene(FxmlView.LOGIN);
    }

}
