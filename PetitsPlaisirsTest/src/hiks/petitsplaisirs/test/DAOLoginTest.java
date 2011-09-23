package hiks.petitsplaisirs.test;

import java.util.Date;

import  hiks.petitsplaisirs.dao.*;
import hiks.petitsplaisirs.model.*;
import android.test.AndroidTestCase;

public class DAOLoginTest extends AndroidTestCase {

	private SessionHandler tLogin;
	private HouseHandler tHouse;
	private UserHandler tUser;
	private TaskHandler tTask;
	private String houseName = "utHouse";
	private String houseMdp = "utHouseMdp";
	private String user1Name = "utUser1";
	private String user1Mdp = "ututUser1Mdp";
	private String user2Name = "utUser2";
	private String user2Mdp = "ututUser2Mdp";
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        tLogin = new SessionHandler(getContext());
        tHouse = new HouseHandlerSQL(getContext(), tLogin.getBase());
        tUser = new UserHandler(getContext(), tLogin.getBase());
        tTask = new TaskHandler(getContext(), tLogin.getBase());
    }

	public void testNominalScenario() {
		int res;
		User[] houseUsers;
		Task[] houseTasks;
		Task[] userTasks;
		Task[] allTasks;
		int cpt;
		int userId;
		int houseId;
		int nbTasksHouse;
		int nbTasksUser;
		
		// Remove test House => The House is removed
		res = tHouse.removeHouse(houseName, houseMdp);
		assertTrue("Erreur la supression d'une maison : "+res, res >= 0 || res == -1);
		
		// Create a new test House with a User => The House is created
		res = tHouse.addHouse(houseName, houseMdp, user1Name, user1Mdp);
		assertTrue("Erreur la création d'une nouvelle maison : "+res, res >= 0);
		houseId = res;
		
		// Check connection => The connection is OK
		res = tLogin.checkUser(houseId, user1Name, user1Mdp);
		assertTrue("Erreur à la tentative de login", res != -99);
		assertTrue("Mauvaise vérification de login", res != -1);
		
		userId = res;
		
		// Add another User to the House => The User is created
		res = tHouse.addUser(user2Name, user2Mdp, houseId);
		assertTrue("Erreur à l'insertion de user2Name", res == 0);
		
		// Show Ladder => User1 > User2 (alphabetical)
		houseUsers = tUser.getUsers(houseId);
		assertTrue("Problème de récupération des habitants : "+houseUsers.length+" au lieu de 2.", houseUsers.length == 2);
		assertTrue("Problème dans l'ordre du Ladder : Le premier est "+houseUsers[0].getNom(), houseUsers[0].getNom().equals(user1Name));
		assertTrue("Problème dans l'ordre du Ladder : Le deuxième est "+houseUsers[1].getNom(), houseUsers[1].getNom().equals(user2Name));
		
		// Show Tasks of the House => No Tasks
		houseTasks = tTask.getHouseTodoTasks(houseId);
		assertTrue("Problème de récupération des tâches de la maison : "+houseTasks.length+" au lieu de 0.", houseTasks.length == 0);
				
		// Add one Task to the House => The Task is added
		allTasks = tTask.getTasks();
		cpt = (int)Math.floor(Math.random()*allTasks.length);
		res = tTask.addTaskToHouse(allTasks[cpt], houseId);
		assertTrue("Erreur à l'insertion de la tâche "+allTasks[cpt].getNom(), res == 0);
		
		// Show Tasks of the House => The added Task is shown
		houseTasks = tTask.getHouseTodoTasks(houseId);
		assertTrue("Problème de récupération des tâches de la maison : "+houseTasks.length+" au lieu de 1.", houseTasks.length == 1);
		
		// Choose priority of the Task => The Task is prioritized
		res = tTask.setTaskPriority(houseTasks[0], 1);
		assertTrue("Problème de priorisation de la tâches : "+res+" ligne(s) affectée(s) au lieu de 1.", res == 1);
		houseTasks = tTask.getHouseTodoTasks(houseId);
		assertTrue("Problème de priorisation de la tâches : "+houseTasks[0].getPriority()+" au lieu de 1.", houseTasks[0].getPriority() == 1);
		
		// First User grab the Task => The Task is added
		houseTasks = tTask.getHouseTodoTasks(houseId);
		res = tTask.addTaskToUser(houseTasks[0], userId);
		assertTrue("Problème d'ajout d'une tache à un user", res == 0);
		
		// Show first User Todo Tasks => The added Task is shown
		userTasks = tTask.getUserTodoTasks(userId);
		assertTrue("Problème de récupération des tâches du user : "+userTasks.length+ " au lieu de 1.", userTasks.length == 1);
		
		// Show Tasks of the House => No Tasks
		houseTasks = tTask.getHouseTodoTasks(houseId);
		assertTrue("Problème de récupération des tache à faire : "+houseTasks.length+" au lieu de 0.", houseTasks.length == 0);
		
		// Show Ladder => User1 > User2 (alphabetique)
		houseUsers = tUser.getUsers(houseId);
		assertTrue("Problème de récupération des habitants : "+houseUsers.length+" au lieu de 2.", houseUsers.length == 2);
		assertTrue("Problème dans l'ordre du Ladder : Le premier est "+houseUsers[0].getNom(), houseUsers[0].getNom().equals(user1Name));
		assertTrue("Problème dans l'ordre du Ladder : Le deuxième est "+houseUsers[1].getNom(), houseUsers[1].getNom().equals(user2Name));
		
		// User 1 do the assigned Task => Task is done
		userTasks = tTask.getUserTodoTasks(userId);
		tTask.setTaskDone(userTasks[0], new Date());
		
		// Show first User Todo Tasks => The added Task is no more shown
		userTasks = tTask.getUserTodoTasks(userId);
		assertTrue("Problème de récupération des tâches du user : "+userTasks.length+ " au lieu de 0.", userTasks.length == 0);
		
		// Show Ladder => User1 > User2 (points)
		houseUsers = tUser.getUsers(houseId);
		assertTrue("Problème de récupération des habitants : "+houseUsers.length+" au lieu de 2.", houseUsers.length == 2);
		assertTrue("Problème dans l'ordre du Ladder : Le premier est "+houseUsers[0].getNom(), houseUsers[0].getNom().equals(user1Name));
		assertTrue("Problème dans l'ordre du Ladder : Le deuxième est "+houseUsers[1].getNom(), houseUsers[1].getNom().equals(user2Name));
		
		// Add some Tasks to the House => The Tasks are added
		allTasks = tTask.getTasks();
		nbTasksHouse = 20;
		{
			int nbAllTasks = allTasks.length;
			Task[] someTasks = new Task[nbTasksHouse];
			for (int i = 0; i<nbTasksHouse; i++){
				cpt = (int)Math.floor(Math.random()*nbAllTasks);
				someTasks[i] = allTasks[cpt];
			}
			res = tTask.addTasksToHouse(someTasks, houseId);
		}
		assertTrue("Erreur à l'insertion de plusieurs tâches ", res == 0);
		
		// Show Tasks of the House => The added Task is shown
		houseTasks = tTask.getHouseTodoTasks(houseId);
		assertTrue("Problème de récupération des tâches de la maison : "+houseTasks.length+" au lieu de "+nbTasksHouse+".", houseTasks.length == nbTasksHouse);
		
		// Choose priority of the Tasks => The Tasks are prioritized
		{
			cpt = (int)Math.floor(Math.random()*10);
			int tmpId = houseTasks[cpt].getId();
			res = tTask.setTaskPriority(houseTasks[cpt], 1);
			assertTrue("Problème de priorisation de la tâches : "+res+" ligne(s) affectée(s) au lieu de 1.", res == 1);
		
			// Show Tasks of the House => The added Tasks are shown orderder by priority
			houseTasks = tTask.getHouseTodoTasks(houseId);
			assertTrue("Problème de priorisation de la tâches : "+houseTasks[0].getId()+" au lieu de "+tmpId+".", houseTasks[0].getId() == tmpId);
		}
		
		// First User disconnection => The User is disconnected
		// TODO
		
		// Second User connection => The User is connected
		res = tLogin.checkUser(houseId, user2Name, user2Mdp);
		assertTrue("Erreur à la tentative de login", res != -99);
		assertTrue("Mauvaise vérification de login", res != -1);
		
		userId = res;
		
		// Second User grab some Tasks => The Task are added
		houseTasks = tTask.getHouseTodoTasks(houseId);
		nbTasksUser = 10; 
		nbTasksHouse = nbTasksHouse - nbTasksUser;
		{
			
			int nbHouseTasks = houseTasks.length;
			Task[] someTasks = new Task[nbTasksUser];
			for (int i = 0; i<nbTasksUser; i++){
				cpt = (int)Math.floor(Math.random()*nbHouseTasks);
				someTasks[i] = houseTasks[i];
			}
			res = tTask.addTasksToUser(someTasks, userId);
		}
		assertTrue("Problème d'ajout de plusieurs tâches à un user", res == 0);
		
		// Show second User Todo Tasks => The added Task is shown
		userTasks = tTask.getUserTodoTasks(userId);
		assertTrue("Problème de récupération des tâches du user : "+userTasks.length+ " au lieu de "+nbTasksUser+".", userTasks.length == nbTasksUser);
		
		// Show Tasks of the House => No Tasks
		houseTasks = tTask.getHouseTodoTasks(houseId);
		assertTrue("Problème de récupération des tache à faire : "+houseTasks.length+" au lieu de "+nbTasksHouse+".", houseTasks.length == nbTasksHouse);
		
		// Show Ladder => User1 > User2 (points)
		houseUsers = tUser.getUsers(houseId);
		assertTrue("Problème de récupération des habitants : "+houseUsers.length+" au lieu de 2.", houseUsers.length == 2);
		assertTrue("Problème dans l'ordre du Ladder : Le premier est "+houseUsers[0].getNom(), houseUsers[0].getNom().equals(user1Name));
		assertTrue("Problème dans l'ordre du Ladder : Le deuxième est "+houseUsers[1].getNom(), houseUsers[1].getNom().equals(user2Name));
		
		// User 2 do the assigned Task => Task is done
		userTasks = tTask.getUserTodoTasks(userId);
		for (int i=0; i<nbTasksUser; i++){
			tTask.setTaskDone(userTasks[i], new Date());
		}
		
		// Show first User Todo Tasks => The added Task is no more shown
		userTasks = tTask.getUserTodoTasks(userId);
		assertTrue("Problème de récupération des tâches du user : "+userTasks.length+ " au lieu de 0.", userTasks.length == 0);
		
		// Show Ladder => User2 > User1 (points)
		houseUsers = tUser.getUsers(houseId);
		assertTrue("Problème de récupération des habitants : "+houseUsers.length+" au lieu de 2.", houseUsers.length == 2);
		assertTrue("Problème dans l'ordre du Ladder : Le premier est "+houseUsers[0].getNom()+" avec "+houseUsers[0].getPoints()+" points, le second ayant " +houseUsers[1].getPoints()+" points.", houseUsers[0].getPoints()>houseUsers[1].getPoints());
		assertTrue("Problème dans l'ordre du Ladder : Le premier est "+houseUsers[0].getNom()+" avec "+houseUsers[0].getPoints()+" points", houseUsers[0].getNom().equals(user2Name));
		assertTrue("Problème dans l'ordre du Ladder : Le deuxième est "+houseUsers[1].getNom()+" avec "+houseUsers[1].getPoints()+" points", houseUsers[1].getNom().equals(user1Name));

		// Second User disconnection => The User is disconnected
	}
	
	// TODO 
	/*
	public void testDeleteUser(){}
	public void testDeleteHouse(){}
	public void testDeleteTask(){}
	
	public void testAssociatedTheSameTaskTwiceToAHouse(){}// Revoir la gestion des Id
	public void testAssociatedTheSameTaskTwiceToAUser(){}// Revoir la gestion des Id
	
	
	public void testInsertSameHouseTwice() {}
	public void testInsertHouseWithStrangeCaracters() {}
	public void testInsertSameUserTwice() {}
	public void testInsertUserWithStrangeCaracters() {}
	public void testConnectionWithUnknownLogin() {}
	public void testConnectionWithBadPassword() {}
	public void testConnectionWithStrangeCaracters() {}
	*/
}
