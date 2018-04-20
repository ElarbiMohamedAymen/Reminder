package com.geenie.reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geenie.reminder.entities.Remind;
import com.geenie.reminder.enumerations.Priority;
import com.geenie.reminder.enumerations.State;
import com.geenie.reminder.service.interfaces.IRemindMySQLService;



@Service
@Transactional
public class StartUpInit implements ApplicationRunner {

	@Autowired
	@Qualifier("remindMySQLServiceImpl")
	IRemindMySQLService remindService;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		Remind remind = new Remind();
		Remind remind2 = new Remind();
		Remind remind3 = new Remind();
		
		/*Remind */
		remind.setName("Preparer le dinner");
		remind.setDescription("La description pour la préparation du dinner");
		remind.setPriority(Priority.NORMAL);
		remind.setState(State.TODO);
		/*Remind2 */
		remind2.setName("Faire sortir la poubelle");
		remind2.setDescription("Description pour la poubelle");
		remind2.setPriority(Priority.URGENT);
		remind2.setState(State.DOING);
		/*Remind3 */
		remind3.setName("Faire les courses");
		remind3.setDescription("Acheter les legumes");
		remind3.setPriority(Priority.PAS_VRAIMENT);
		remind3.setState(State.DONE);
		remindService.addEvent(remind);
		remindService.addEvent(remind2);
		remindService.addEvent(remind3);
	}
}
