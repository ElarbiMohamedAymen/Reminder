package com.geenie.reminder.enumerations;

public enum State {
	TODO {

		@Override
		public String getStatus() {
			return "Todo";
		}
	},
	DOING {

		@Override
		public String getStatus() {
			return "Doing";
		}
	},
	DONE {

		@Override
		public String getStatus() {
			return "Done";
		}
	};

	public abstract String getStatus();
}
