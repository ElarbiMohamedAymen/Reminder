package com.geenie.reminder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geenie.reminder.entities.Remind;
import com.geenie.reminder.enumerations.State;
import com.geenie.reminder.repository.ReminderRepository;
import com.geenie.reminder.service.interfaces.IRemindMySQLService;

@Service
public class RemindMySQLServiceImpl implements IRemindMySQLService {

	@Autowired
	private ReminderRepository reminderRepository;

	@Override
	public void addEvent(Remind remind) {
		reminderRepository.save(remind);
	}

	@Override
	public List<Remind> getAllEvents() {
		return (List<Remind>) reminderRepository.findAll();
	}

	@Override
	public void updateEvent(Remind remind) {
		if (remind.getState().equals(State.TODO)) {
			remind.setState(State.DOING);
		}else if (remind.getState().equals(State.DOING)) {
			remind.setState(State.DONE);
		}
		reminderRepository.save(remind);

	}
}
