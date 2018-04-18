package com.geenie.reminder.enumerations;

public enum Priority {
	URGENT {

		@Override
		public String getStatus() {
			return "Urgent";
		}
	},
	PAS_VRAIMENT {

		@Override
		public String getStatus() {
			return "Pas Vraiment";
		}
	},
	NORMAL {

		@Override
		public String getStatus() {
			return "Normal";
		}
	};

	public abstract String getStatus();
	
}
