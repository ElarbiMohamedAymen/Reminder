package com.geenie.reminder.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geenie.reminder.entities.Remind;

@Repository
public interface ReminderRepository  extends CrudRepository<Remind, Long> {

}
