package hiks.petitsplaisirs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import hiks.petitsplaisirs.model.*;
import hiks.petitsplaisirs.utils.Tools;

public class HouseHandlerSQL extends HouseHandler {
 
	// TODO : coller les infos de base dans DBAccess ou autre static
	
 
	private SQLiteDatabase bdd;
 
	private DBAccess maBase;
 
	public HouseHandlerSQL(Context context){
		//On cr�er la BDD et sa table
		maBase = new DBAccess(context, null);
	}
	
	public HouseHandlerSQL(Context context, DBAccess base){
		//On cr�er la BDD et sa table
		maBase = base;
	}
	
	public DBAccess getBase(){
		return maBase;
	}
 
	public void open(){
		//on ouvre la BDD en �criture
		bdd = maBase.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}
	
	/**
	 * Supprime une maison
	 * @param houseName le nom de la maison
	 * @return nombre de ligne supprim�es si la suppression a eu lieu (-1 si la maison n'existait pas, -99 en cas de probl�me)
	 */
	public int removeHouse(String houseName, String housePass){
		String METHOD_NAME = "removeHouse"; 
		int returnValue = -99;
		try{
			// Check mandatory
			if ("".equals(houseName) || "".equals(housePass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			houseName = Tools.escapeDataChars(houseName);
			housePass = Tools.escapeDataChars(housePass);
			
			open();
			int result;
			
			// Check existence de la maison
			result = checkHouseExists(houseName);
			if (result == -1){ // Cas de maison non trouv�e
				return -1;
			}
			
			int houseId = result;
			
			// Suppression de la maison et des �l�ments associ�s (taches, users...)
			result = deleteHouse(houseId);
						
			returnValue = result;
			
			close();
			
			return returnValue;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
	
	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe de l'habitant
	 * @return l'id de la maison (-1 si elle existe d�j�, -99 en cas de probl�me)
	 */
	public int addHouse(String houseName, String housePass, String userName, String userPass){
		int returnValue = -99;
		
		String[] usersName = {userName};
		String[] usersPass = {userPass};
		returnValue = addHouse (houseName, housePass);
		if (returnValue > 0){ // Si la maison est bien ins�r�e, on ins�re le user
			int houseId = returnValue;
			
			returnValue = addUsers(usersName, usersPass, houseId);
			if (returnValue == 0){ // Si tout s'est bien pass�, on retourne l'id de la maison
				returnValue = houseId;
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @return l'id de la maison (-1 si elle existe d�j�, -99 en cas de probl�me)
	 */
	private int addHouse(String houseName, String housePass){
		String METHOD_NAME = "addHouse"; 
		int returnValue = -99;
		try{
			// Check mandatory
			if ("".equals(houseName) || "".equals(housePass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			houseName = Tools.escapeDataChars(houseName);
			housePass = Tools.escapeDataChars(housePass);
			
			open();
			int result;
			
			// Check unicit� du nom de la maison
			result = checkHouseExists(houseName);
			if (result != -1){ // Cas de maison trouv�e
				return -1;
			}
			
			// Insertion de la maison
			House newHouse = new House(houseName, housePass);
			result = insertHouse(newHouse);
			if (result == -1){ // Cas d'erreur
				throw new Exception (METHOD_NAME+" : Erreur � l'insertion de la maison");
			}
			
			// R�cup�ration de l'Id de la maison
			result = checkHouseExists(houseName);
			if (result < 0){ // Cas de maison non trouv�e (alors que je viens de l'ins�rer...)
				throw new Exception (METHOD_NAME+" : Erreur pour r�cup�rer ma maison ins�r�e :(");
			}
			returnValue = result;
			
			close();
			
			return returnValue;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
 
	/**
	 * Ajoute un nouvel habitant
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe
	 * @param houseId l'id de la maison
	 * @return 0 si tout s'est bien pass� (-99 en cas de probl�me)
	 */
	public int addUser(String userName, String userPass, int houseId){
		String[] usersName = {userName};
		String[] usersPass = {userPass};
		return addUsers(usersName, usersPass, houseId);
	}
	
	/**
	 * Ajoute des nouveaux habitants
	 * @param userName la liste des noms d'habitant
	 * @param userPass la liste des mots de passes
	 * @param houseId l'id de la maison
	 * @return 0 si tout s'est bien pass� (-99 en cas de probl�me)
	 */
	public int addUsers(String[] usersName, String[] usersPass, int houseId){
		String METHOD_NAME = "addUser"; 
		try{
			// Check arguments
			if (usersName == null || usersPass == null || usersName.length != usersPass.length) {
				throw new Exception (METHOD_NAME+" : Mauvais paramm�tres");
			}
			int nbUser = usersName.length;
			// Check mandatory
			if (nbUser == 0) {
				throw new Exception (METHOD_NAME+" : Aucun habitant � ins�rer");
			}
			
			open();
			int result;
			
			// Check existence de la maison
			House house = getHouseById(houseId);
			if (house == null){ // Cas de maison non trouv�e
				throw new Exception (METHOD_NAME+" : Id de la maison inexistant");
			}
			
			// Insertion des habitants de la maison
			User tmpUser;
			for (int i=0; i<nbUser; i++){
				tmpUser = new User(Tools.escapeDataChars(usersName[i]));
				tmpUser.setMdp(usersPass[i]);
				result = insertUser(tmpUser, house);
				if (result == -1){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur � l'insertion du user "+usersName[i]);
				}
			}
			
			close();
			
			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
 
	/**
	 * Insert un habitant dans la base
	 * @param user, house
	 * @return comme bdd.insert (i.e. : the row ID of the newly inserted row, or -1 if an error occurred)
	 */
	private int insertUser(User user, House house){
		ContentValues values = new ContentValues();
		values.put(DBAccess.user_TABLE_COL_NOM, user.getNom());
		values.put(DBAccess.user_TABLE_COL_MDP, user.getMdp());
		values.put(DBAccess.user_TABLE_COL_IDHOUSE, house.getId());
		return (int) bdd.insert(DBAccess.user_TABLE, null, values);
	}
	
	/**
	 * Retourne la maison � partir de son Id
	 * @param Id
	 * @return la Maison si elle est trouv�e, null sinon
	 */
	private House getHouseById(int Id){
		Cursor c = bdd.query(DBAccess.house_TABLE, 
				new String[] {
					DBAccess.house_TABLE_COL_ID, 
					DBAccess.house_TABLE_COL_NOM, 
					DBAccess.house_TABLE_COL_MDP
					}, 
				DBAccess.house_TABLE_COL_ID + " = \"" + Id +"\"", 
				null, null, null, null);

		House m = null;
		if (c.getCount() != 0){
			c.moveToFirst();
			m = new House();
			m.setId(c.getLong(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_ID)));
			m.setNom(c.getString(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_NOM)));
			m.setMdp(c.getString(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_MDP)));
			c.close();
		}
		return m;
	}
	
	/**
	 * Insert une maison dans la base
	 * @param maison
	 * @return comme bdd.insert (i.e. : the row ID of the newly inserted row, or -1 if an error occurred)
	 */
	private int insertHouse(House maison){
		ContentValues values = new ContentValues();
		values.put(DBAccess.house_TABLE_COL_NOM, maison.getNom());
		values.put(DBAccess.house_TABLE_COL_MDP, maison.getMdp());
		return (int) bdd.insert(DBAccess.house_TABLE, null, values);
	}
	
	/**
	 * Delete une maison dans la base
	 * @param l'id de la maison
	 * @return comme bdd.delete (i.e. : the number of rows affected, 0 otherwise)
	 */
	private int deleteHouse(int houseId){
		int result = 0;
		// Delete la maison
		result += bdd.delete(DBAccess.house_TABLE, 
				DBAccess.house_TABLE_COL_ID +" = "+ houseId,
				null);
		// Delete les t�ches associ�es � la maison
		result += bdd.delete(DBAccess.tache_house_TABLE, 
				DBAccess.tache_house_TABLE_COL_IDMAISON +" = "+ houseId,
				null);
		// Delete les t�ches associ�es aux users
		result += bdd.delete(DBAccess.tache_user_TABLE, 
				DBAccess.tache_user_TABLE_COL_IDUSER +" in " +
				"("+
					"select "+DBAccess.user_TABLE_COL_ID+
					" from "+DBAccess.user_TABLE+
					" where "+DBAccess.user_TABLE_COL_IDHOUSE+" = "+houseId+
				")",
				null);
		// Delete les users associ�s
		result += bdd.delete(DBAccess.user_TABLE, 
				DBAccess.user_TABLE_COL_IDHOUSE +" = "+ houseId,
				null);
		return result;
	}
	
	/**
	 * V�rifie l'existence d'une maison � partir du nom
	 * @param houseName
	 * @return l'id de la maison si elle existe, -1 sinon et -99 si il y'a plusieurs r�sultats
	 */
	private int checkHouseExists(String houseName){
		Cursor c = bdd.query(DBAccess.house_TABLE, 
				new String[] {DBAccess.house_TABLE_COL_ID, DBAccess.house_TABLE_COL_NOM, DBAccess.house_TABLE_COL_MDP}, 
				DBAccess.house_TABLE_COL_NOM + " = \"" + houseName +"\"", 
				null, null, null, null);

		if (c.getCount() == 0)
			return -1;
		
		if (c.getCount() > 1)
			return -99;
 
		c.moveToFirst();
		int houseId = c.getInt(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_ID));
		c.close();
 
		return houseId;
	}
}