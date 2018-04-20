package com.geenie.reminder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geenie.reminder.entities.Remind;

@Repository
public interface ReminderRepository  extends CrudRepository<Remind, Long> {

	@Query("SELECT r FROM  Remind r WHERE r.visible = TRUE")
	public List<Remind> getAllVisibleEvents();
}
