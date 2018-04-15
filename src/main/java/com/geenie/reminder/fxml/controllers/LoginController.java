/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geenie.reminder.fxml.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.geenie.reminder.config.StageManager;
import com.geenie.reminder.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.CheckBox;

/**
 * FXML Controller class
 *
 * @author ElarbiMohamedAymen
 */
@Controller
public class LoginController implements Initializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Lazy
	@Autowired
	private StageManager stageManager;
	

    @FXML
    private JFXTextField usernameTF;

    @FXML
    private JFXTextField passwordTF;

    @FXML
    private CheckBox rememberMe;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	@FXML
	void login(ActionEvent event) {
		stageManager.switchScene(FxmlView.MAIN_MENU);
	}

}
