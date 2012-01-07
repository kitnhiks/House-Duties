package hiks.petitsplaisirs.dao;

import java.util.Date;

import hiks.petitsplaisirs.model.Task;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TaskHandler {

	// TODO : coller les infos de base dans DBAccess ou autre static

	private DBAccess maBase;

	public TaskHandler(Context context){
		//On cr�er la BDD et sa table
		maBase = new DBAccess(context, null);
	}

	public TaskHandler(Context context, DBAccess base){
		//On cr�er la BDD et sa table
		maBase = base;
	}

	public DBAccess getBase(){
		return maBase;
	}

	/** 
	 * @param houseId
	 * @return les t�ches pour une maison donn�e, tableau vide si aucune t�che n'est trouv�e
	 */
	public Task[] getHouseTodoTasks(int houseId){
		maBase.open();
		Cursor c = maBase.getBDD().query(
				DBAccess.tache_house_TABLE+" tth"+
				" INNER JOIN "+
				DBAccess.tache_TABLE+" tt"+
				" ON (tth."+DBAccess.tache_house_TABLE_COL_IDTACHE+
				" = tt."+DBAccess.tache_TABLE_COL_ID+")", 
				new String[] {
						"tt."+DBAccess.tache_TABLE_COL_ID,
						"tt."+DBAccess.tache_TABLE_COL_NOM,
						"tt."+DBAccess.tache_TABLE_COL_POINT,
						"tt."+DBAccess.tache_TABLE_COL_IDCATEGORIE,
						"tth."+DBAccess.tache_house_TABLE_COL_IDRELATION,
						"tth."+DBAccess.tache_house_TABLE_COL_PRIORITE,
						"tth."+DBAccess.tache_house_TABLE_COL_DEADLINE
				}, 
				DBAccess.tache_house_TABLE_COL_IDMAISON + " = " + houseId, 
				null, null, null, DBAccess.tache_house_TABLE_COL_PRIORITE+" DESC, "+DBAccess.tache_house_TABLE_COL_DEADLINE+" DESC, "+DBAccess.tache_TABLE_COL_POINT+" DESC, "+DBAccess.tache_TABLE_COL_NOM);

		int nbTasks = c.getCount();

		Task[] lt = new Task[nbTasks];
		Task t;
		int cpt = 0;
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			t = new Task(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_NOM)));
			t.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_ID)));
			t.setIdRelation(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_house_TABLE_COL_IDRELATION)));
			t.setPoint(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_POINT)));
			t.setPriority(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_house_TABLE_COL_PRIORITE)));
			Date d = new Date(c.getLong(c.getColumnIndexOrThrow(DBAccess.tache_house_TABLE_COL_DEADLINE)));
			t.setDeadline(d);
			lt[cpt] = t;
			cpt++;
			c.moveToNext();
		}
		c.close();
		maBase.close();

		return lt;
	}

	/** 
	 * @param userId
	 * @return les t�ches pour un utilisateur donn�, tableau vide si aucune t�che n'est trouv�e
	 */
	public Task[] getTasksByUser(int userId){
		maBase.open();
		Cursor c = maBase.getBDD().query(
				DBAccess.tache_user_TABLE+" ttu"+
				" INNER JOIN "+
				DBAccess.tache_TABLE+" tt"+
				" ON (ttu."+DBAccess.tache_user_TABLE_COL_IDTACHE+
				" = tt."+DBAccess.tache_TABLE_COL_ID+")", 
				new String[] {
						"tt."+DBAccess.tache_TABLE_COL_ID,
						"tt."+DBAccess.tache_TABLE_COL_NOM,
						"tt."+DBAccess.tache_TABLE_COL_POINT,
						"tt."+DBAccess.tache_TABLE_COL_IDCATEGORIE,
						"ttu."+DBAccess.tache_user_TABLE_COL_IDRELATION
				}, 
				DBAccess.tache_user_TABLE_COL_IDUSER + " = " + userId, 
				null, null, null, null);

		int nbTasks = c.getCount();

		Task[] lt = new Task[nbTasks];
		Task t;
		int cpt = 0;
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			t = new Task(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_NOM)));
			t.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_ID)));
			t.setIdRelation(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_user_TABLE_COL_IDRELATION)));
			t.setPoint(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_POINT)));
			lt[cpt] = t;
			cpt++;
			c.moveToNext();
		}
		c.close();
		maBase.close();

		return lt;
	}

	/** 
	 * @param userId
	 * @return les t�ches � faire pour un utilisateur donn�, tableau vide si aucune t�che n'est trouv�e
	 */
	public Task[] getUserTodoTasks(int userId) {
		maBase.open();
		Cursor c = maBase.getBDD().query(
				DBAccess.tache_user_TABLE+" ttu"+
				" INNER JOIN "+
				DBAccess.tache_TABLE+" tt"+
				" ON (ttu."+DBAccess.tache_user_TABLE_COL_IDTACHE+
				" = tt."+DBAccess.tache_TABLE_COL_ID+")", 
				new String[] {
						"tt."+DBAccess.tache_TABLE_COL_ID,
						"tt."+DBAccess.tache_TABLE_COL_NOM,
						"tt."+DBAccess.tache_TABLE_COL_POINT,
						"tt."+DBAccess.tache_TABLE_COL_IDCATEGORIE,
						"ttu."+DBAccess.tache_user_TABLE_COL_IDRELATION
				}, 
				DBAccess.tache_user_TABLE_COL_IDUSER + " = " + userId +
				" AND "+
				DBAccess.tache_user_TABLE_COL_FAITLE + " is null "
				, 
				null, null, null, null);

		int nbTasks = c.getCount();

		Task[] lt = new Task[nbTasks];
		Task t;
		int cpt = 0;
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			t = new Task(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_NOM)));
			t.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_ID)));
			t.setIdRelation(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_user_TABLE_COL_IDRELATION)));
			t.setPoint(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_POINT)));
			lt[cpt] = t;
			cpt++;
			c.moveToNext();
		}
		c.close();
		maBase.close();

		return lt;
	}

	/**
	 * 
	 * @return toutes les t�ches
	 */
	public Task[] getTasks(){
		maBase.open();
		Cursor c = maBase.getBDD().query(
				DBAccess.tache_TABLE+" tt", 
				new String[] {
						"tt."+DBAccess.tache_TABLE_COL_ID,
						"tt."+DBAccess.tache_TABLE_COL_NOM,
						"tt."+DBAccess.tache_TABLE_COL_POINT,
						"tt."+DBAccess.tache_TABLE_COL_IDCATEGORIE,
				},
				null, null, null, null, null);

		int nbTasks = c.getCount();
		if (nbTasks == 0)
			return null;

		Task[] lt = new Task[nbTasks];
		Task t;
		int cpt = 0;
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			t = new Task(c.getString(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_NOM)));
			t.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_ID)));
			t.setPoint(c.getInt(c.getColumnIndexOrThrow(DBAccess.tache_TABLE_COL_POINT)));
			lt[cpt] = t;
			cpt++;
			c.moveToNext();
		}
		c.close();
		maBase.close();

		return lt;
	}

	/**
	 * Ajoute une tache � une maison
	 * @param task
	 * @param houseId
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int addTaskToHouse(Task task, int houseId){
		return addTasksToHouse(new Task[] {task}, houseId);
	}

	/**
	 * Ajoute une liste de tache � une maison
	 * @param tasks
	 * @param houseId
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int addTasksToHouse(Task[] tasks, int houseId){
		String METHOD_NAME = "addTasksToHouse"; 
		try{
			int nbTasks = tasks.length;
			// Check mandatory
			if (nbTasks == 0) {
				throw new Exception (METHOD_NAME+" : Aucune t�che � ins�rer");
			}

			maBase.open();
			int result;

			// TODO : Check existence de la maison
			// TODO : Check existence de la t�che

			// Insertion des t�che de la maison
			for (int i=0; i<nbTasks; i++){
				result = insertTaskToHouse(tasks[i], houseId);
				if (result == ErrorHandler.SQL_ERROR){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur � l'insertion de la t�che "+tasks[i]);
				}
			}

			maBase.close();

			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return ErrorHandler.UNKNOWN;
		}
	}

	/**
	 * Associe une t�che � une maison
	 * @param task
	 * @param houseId
	 * @return The row ID of the newly inserted row, or ErrorHandler.SQL_ERROR if an error occurred
	 */
	private int insertTaskToHouse(Task task, int houseId){
		ContentValues values = new ContentValues();
		// TODO : generate GUID pour les t�ches
		int rnd = (int)Math.floor(Math.random()*1000000);
		values.put(DBAccess.tache_house_TABLE_COL_IDTACHE, task.getId());
		values.put(DBAccess.tache_house_TABLE_COL_IDRELATION, houseId+"-"+task.getId()+"-"+new Date().getTime()+"-"+rnd);
		values.put(DBAccess.tache_house_TABLE_COL_IDMAISON, houseId);
		return (int) maBase.getBDD().insert(DBAccess.tache_house_TABLE, null, values);
	}

	/**
	 * Retire une t�che d'une maison
	 * @param task
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int removeTaskFromHouse(Task task){
		String METHOD_NAME = "removeTaskFromHouse"; 
		try{
			// Check mandatory
			// Rien...

			maBase.open();
			int result;

			// TODO : Check existence de la maison
			// TODO : Check existence de la t�che

			// Deletion de la t�che de la maison
			result = deleteTaskFromHouse(task);
			if (result == 0){ // Rien n'a �t� supprim�
				System.out.println(METHOD_NAME+" Rien n'a �t� supprim�... (T�che : "+task+")");
			}

			maBase.close();

			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return ErrorHandler.UNKNOWN;
		}
	}

	/**
	 * Delete l'association d'une t�che � une maison
	 * @param task
	 * @return comme bdd.delete (i.e. : the number of rows affected, 0 otherwise)
	 */
	private int deleteTaskFromHouse(Task task){
		return maBase.getBDD().delete(DBAccess.tache_house_TABLE,
				DBAccess.tache_house_TABLE_COL_IDRELATION +"= \""+ task.getIdRelation()+"\"",
				null);
	}

	/**
	 * Met � jour la priorit� d'une t�che associ� � une maison
	 * @param task
	 * @param la nouvelle priorit�
	 * @return comme bdd.update (i.e. : the number of rows affected, 0 otherwise)
	 */
	public int setTaskPriority(Task task, int priority){
		maBase.open();
		ContentValues values = new ContentValues();
		values.put(DBAccess.tache_house_TABLE_COL_PRIORITE, priority);
		int result = updateTaskOfHouse(task, values);
		maBase.close();
		
		if (result == 1){
			task.setPriority(priority);
		}
		return result;
	}

	/**
	 * Update l'association d'une t�che � une maison
	 * @param task
	 * @param les valeurs � mettre � jour
	 * @return comme bdd.update (i.e. : the number of rows affected, 0 otherwise)
	 */
	public int updateTaskOfHouse(Task task, ContentValues values) {

		return maBase.getBDD().update(DBAccess.tache_house_TABLE, values, 
				DBAccess.tache_house_TABLE_COL_IDRELATION +"= \""+ task.getIdRelation()+"\"",
				null);
	}

	/**
	 * Ajoute une tache � un user
	 * @param task
	 * @param userId
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int addTaskToUser(Task task, int userId){
		return addTasksToUser(new Task[] {task}, userId);
	}

	/**
	 * Ajoute une liste de tache � un user
	 * @param tasks
	 * @param userId
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int addTasksToUser(Task[] tasks, int userId){
		String METHOD_NAME = "addTasksToUser"; 
		try{
			int nbTasks = tasks.length;
			// Check mandatory
			if (nbTasks == 0) {
				throw new Exception (METHOD_NAME+" : Aucune t�che � ins�rer");
			}

			maBase.open();
			int result;

			// TODO : Check existence du user
			// TODO : Check existence de la t�che

			// Insertion des t�che du user
			for (int i=0; i<nbTasks; i++){
				result = insertTaskToUser(tasks[i], userId);
				if (result == ErrorHandler.SQL_ERROR){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur � l'insertion de la t�che au user "+tasks[i]);
				}
			}

			// Suppression des t�che de la maison
			for (int i=0; i<nbTasks; i++){
				result = deleteTaskFromHouse(tasks[i]);
				if (result == 0){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur � la suppression de la t�che de la maison "+tasks[i]);
				}
			}

			maBase.close();

			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			maBase.close();
			return ErrorHandler.UNKNOWN;
		}
	}

	/**
	 * Associe une t�che � un user
	 * @param task
	 * @param id du user
	 * @return The row ID of the newly inserted row, or ErrorHandler.SQL_ERROR if an error occurred
	 */
	private int insertTaskToUser(Task task, int userId){
		ContentValues values = new ContentValues();
		// TODO : generate GUID pour les t�ches
		int rnd = (int)Math.floor(Math.random()*1000000);
		values.put(DBAccess.tache_user_TABLE_COL_IDTACHE, task.getId());
		values.put(DBAccess.tache_user_TABLE_COL_IDRELATION, userId+"-"+task.getId()+"-"+new Date().getTime()+"-"+rnd);
		values.put(DBAccess.tache_user_TABLE_COL_IDUSER, userId);
		return (int) maBase.getBDD().insert(DBAccess.tache_user_TABLE, null, values);
	}

	/**
	 * Retire une t�che d'un user et la rajoute � une maison
	 * @param task
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int ReturnTaskToHouse(Task task, int houseId){
		String METHOD_NAME = "ReturnTaskToHouse"; 
		try{
			// Check mandatory
			// Rien...

			maBase.open();
			int result;

			// TODO : Check existence du user, maison
			// TODO : Check existence de la t�che

			// Deletion de la t�che du user
			result = deleteTaskFromUser(task);
			if (result == 0){ // Rien n'a �t� supprim�
				System.out.println(METHOD_NAME+" Rien n'a �t� supprim�... (T�che : "+task+")");
			}else{
				// Rajout de la t�che � la maison
				result = addTaskToHouse(task, houseId);
				if (result != 0){
					throw new Exception (METHOD_NAME+" : Erreur � la suppression de la t�che de la maison "+task);
				}
			}

			maBase.close();

			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			maBase.close();
			return ErrorHandler.UNKNOWN;
		}
	}
	
	
	/**
	 * Retire une t�che d'un user
	 * @param task
	 * @return 0 si c'est OK, ErrorHandler.UNKNOWN sinon
	 */
	public int removeTaskFromUser(Task task){
		String METHOD_NAME = "removeTaskFromUser"; 
		try{
			// Check mandatory
			// Rien...

			maBase.open();
			int result;

			// TODO : Check existence du user
			// TODO : Check existence de la t�che

			// Deletion de la t�che du user
			result = deleteTaskFromUser(task);
			if (result == 0){ // Rien n'a �t� supprim�
				System.out.println(METHOD_NAME+" Rien n'a �t� supprim�... (T�che : "+task+")");
			}

			maBase.close();

			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			maBase.close();
			return ErrorHandler.UNKNOWN;
		}
	}

	/**
	 * Delete l'association d'une t�che � une user
	 * @param task
	 * @return comme bdd.delete (i.e. : the number of rows affected, 0 otherwise)
	 */
	private int deleteTaskFromUser(Task task){
		return (int) maBase.getBDD().delete(DBAccess.tache_user_TABLE, 
				DBAccess.tache_user_TABLE_COL_IDRELATION +"= \""+ task.getIdRelation()+"\"",
				null);
	}

	/**
	 * Met � jour la date de Done d'une tache pour un user
	 * @param task
	 * @param doneDate
	 * @return comme bdd.update (i.e. : the number of rows affected, 0 otherwise)
	 */
	public int setTaskDone(Task task, Date doneDate) {
		maBase.open();
		ContentValues values = new ContentValues(1);
		values.put(DBAccess.tache_user_TABLE_COL_FAITLE, doneDate.getTime());
		int result = updateTaskOfUser(task, values);
		maBase.close();
		
		if (result == 1){
			task.setDoneDate(doneDate);
		}
		
		return result;
	}

	/**
	 * Update l'association d'une t�che � un user
	 * @param task
	 * @param values les valeurs � mettre � jour
	 * @return comme bdd.update (i.e. : the number of rows affected, 0 otherwise)
	 */
	public int updateTaskOfUser(Task task, ContentValues values) {
		return maBase.getBDD().update(DBAccess.tache_user_TABLE, values, 
				DBAccess.tache_user_TABLE_COL_IDRELATION+" = \""+task.getIdRelation()+"\"", 
				null);
	}
}
