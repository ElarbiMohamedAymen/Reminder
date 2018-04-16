package com.geenie.reminder.view;

import java.util.ResourceBundle;

public enum FxmlView {

	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/login.fxml";
		}
	},
	MAIN_MENU {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("main_menu.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/mainMenu.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}

}
