package com.geenie.reminder.service.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.geenie.reminder.entities.Remind;;

public interface IRemindMySQLService {

	@Transactional
	public void addEvent(Remind remind);
	
	public List<Remind> getAllEvents();
	
	public void updateEvent(Remind remind);
	
	public void hideRemind(Remind remind);
	
	public List<Remind> getAllVisibleEvents();

}
