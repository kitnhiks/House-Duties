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

	private SessionHandler sh;

	public HouseHandlerSQL(Context context){
		//On créer la BDD et sa table
		maBase = new DBAccess(context, null);
		sh = new SessionHandler(context);
	}

	public HouseHandlerSQL(Context context, DBAccess base){
		//On créer la BDD et sa table
		maBase = base;
		sh = new SessionHandler(context);
	}

	public DBAccess getBase(){
		return maBase;
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBase.getWritableDatabase();
	}

	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}

	/**
	 * Supprime une maison
	 * @param houseName le nom de la maison
	 * @return nombre de ligne supprimées si la suppression a eu lieu (-1 si la maison n'existait pas, -99 en cas de problème)
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
			result = sh.checkHouse(houseName);
			if (result == -1){ // Cas de maison non trouvée
				return -1;
			}

			int houseId = result;

			// Suppression de la maison et des éléments associés (taches, users...)
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
	 * @return l'id de la maison (-1 si elle existe déjà, -99 en cas de problème)
	 */
	public int addHouse(String houseName, String housePass, String userName, String userPass){
		int returnValue = -99;

		returnValue = addHouse (houseName, housePass);
		if (returnValue > 0){ // Si la maison est bien insérée, on insère le user
			int houseId = returnValue;

			returnValue = addUser(userName, userPass, houseId);
			if (returnValue > 0){ // Si tout s'est bien passé, on retourne l'id de la maison
				returnValue = houseId;
			}
		}

		return returnValue;
	}

	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @return l'id de la maison (-1 si elle existe déjà, -99 en cas de problème)
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

			// Check unicité du nom de la maison
			result = sh.checkHouse(houseName);
			if (result != -1){ // Cas de maison trouvée
				return -1;
			}

			// Insertion de la maison
			House newHouse = new House(houseName, housePass);
			result = insertHouse(newHouse);
			if (result == -1){ // Cas d'erreur
				throw new Exception (METHOD_NAME+" : Erreur à l'insertion de la maison");
			}

			// Récupération de l'Id de la maison
			result = sh.checkHouse(houseName);
			if (result < 0){ // Cas de maison non trouvée (alors que je viens de l'insérer...)
				throw new Exception (METHOD_NAME+" : Erreur pour récupérer ma maison insérée :(");
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
	 * @return l'id du user si tout s'est bien passé (-99 en cas de problème)
	 */
	public int addUser(String userName, String userPass, int houseId){
		String METHOD_NAME = "addUser"; 
		try{
			// Check arguments
			if ("".equals(userName) || "".equals(userPass)) {
				throw new Exception (METHOD_NAME+" : Mauvais parammètres");
			}

			open();
			int result;

			// Check existence de la maison
			House house = getHouseById(houseId);
			if (house == null){ // Cas de maison non trouvée
				throw new Exception (METHOD_NAME+" : Id de la maison inexistant");
			}

			// Check unicité du user
			result = sh.checkUser(houseId, userName, userPass);
			if (result != -1){ // Cas de user trouvée
				return -1;
			}

			// Insertion des habitants de la maison
			User tmpUser;
			tmpUser = new User(Tools.escapeDataChars(userName));
			tmpUser.setMdp(Tools.escapeDataChars(userPass));
			result = insertUser(tmpUser);
			if (result == -1){ // Cas d'erreur
				throw new Exception (METHOD_NAME+" : Erreur à l'insertion du user "+userName);
			}


			// Check existence du user
			int userId = sh.checkUser(userName, userPass);
			if (userId < 0){ // Cas de user non trouvée (alors que je viens de l'insérer...)
				throw new Exception (METHOD_NAME+" : Erreur pour récupérer mon user insérée ("+userName+")");
			}

			result = linkUserHouse(userId, houseId);
			if (result == -1){ // Cas d'erreur
				throw new Exception (METHOD_NAME+" : Erreur à l'insertion du lien du user "+userName);
			}				

			close();

			return userId;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}

	/**
	 * Ajoute des nouveaux habitants
	 * @param userName la liste des noms d'habitant
	 * @param userPass la liste des mots de passes
	 * @param houseId l'id de la maison
	 * @return 0 si tout s'est bien passé (-99 en cas de problème)
	 */
	public int addUsers(String[] usersName, String[] usersPass, int houseId){
		String METHOD_NAME = "addUser"; 
		try{
			// Check arguments
			if (usersName == null || usersPass == null || usersName.length != usersPass.length) {
				throw new Exception (METHOD_NAME+" : Mauvais parammètres");
			}
			int nbUser = usersName.length;
			// Check mandatory
			if (nbUser == 0) {
				throw new Exception (METHOD_NAME+" : Aucun habitant à insérer");
			}

			open();
			int result;

			// Check existence de la maison
			House house = getHouseById(houseId);
			if (house == null){ // Cas de maison non trouvée
				throw new Exception (METHOD_NAME+" : Id de la maison inexistant");
			}

			// Insertion des habitants de la maison
			User tmpUser;
			for (int i=0; i<nbUser; i++){
				// Check unicité du user
				result = sh.checkUser(houseId, usersName[i], usersPass[i]);
				if (result != -1){ // Cas de user trouvée
					return -1;
				}

				tmpUser = new User(Tools.escapeDataChars(usersName[i]));
				tmpUser.setMdp(Tools.escapeDataChars(usersPass[i]));
				result = insertUser(tmpUser);
				if (result == -1){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur à l'insertion du user "+usersName[i]);
				}


				// Check existence du user
				int userId = sh.checkUser(house.getId(), usersName[i], usersPass[i]);
				if (userId < 0){ // Cas de user non trouvée (alors que je viens de l'insérer...)
					throw new Exception (METHOD_NAME+" : Erreur pour récupérer mon user insérée ("+usersName[i]+")");
				}

				result = linkUserHouse(userId, houseId);
				if (result == -1){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur à l'insertion du lien du user "+usersName[i]);
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
	 * @param user
	 * @param house
	 * @return comme bdd.insert (i.e. : the row ID of the newly inserted row, or -1 if an error occurred)
	 */
	private int insertUser(User user){
		ContentValues values = new ContentValues();
		values.put(DBAccess.user_TABLE_COL_NOM, user.getNom());
		values.put(DBAccess.user_TABLE_COL_MDP, user.getMdp());
		return (int) bdd.insert(DBAccess.user_TABLE, null, values);
	}

	/**
	 * Insert un lien habitant-maison dans la base
	 * @param user
	 * @param house
	 * @return comme bdd.insert (i.e. : the row ID of the newly inserted row, or -1 if an error occurred)
	 */
	private int linkUserHouse(int userId, int houseId){
		ContentValues values = new ContentValues();
		values.put(DBAccess.user_house_TABLE_COL_IDUSER, userId);
		values.put(DBAccess.user_house_TABLE_COL_IDHOUSE, houseId);
		return (int) bdd.insert(DBAccess.user_house_TABLE, null, values);
	}

	/**
	 * Retourne la maison à partir de son Id
	 * @param Id
	 * @return la Maison si elle est trouvée, null sinon
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
			m.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_ID)));
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
		// Delete les tâches associées à la maison
		result += bdd.delete(DBAccess.tache_house_TABLE, 
				DBAccess.tache_house_TABLE_COL_IDMAISON +" = "+ houseId,
				null);
		// Delete les tâches associées aux users
		result += bdd.delete(DBAccess.tache_user_TABLE, 
				DBAccess.tache_user_TABLE_COL_IDUSER +" in " +
				"("+
				"select "+DBAccess.user_TABLE_COL_ID+
				" from "+DBAccess.user_TABLE+", "+DBAccess.user_house_TABLE+
				" where "+DBAccess.user_house_TABLE_COL_IDHOUSE+" = "+houseId+
				" and "+DBAccess.user_house_TABLE_COL_IDUSER+" = "+DBAccess.user_TABLE_COL_ID+
				")",
				null);
		// Delete les users associés
		result += bdd.delete(DBAccess.user_TABLE, 
				DBAccess.user_TABLE_COL_ID+" in " +
				"("+
				"select "+DBAccess.user_TABLE_COL_ID+
				" from "+DBAccess.user_TABLE+", "+DBAccess.user_house_TABLE+
				" where "+DBAccess.user_house_TABLE_COL_IDHOUSE+" = "+houseId+
				" and "+DBAccess.user_house_TABLE_COL_IDUSER+" = "+DBAccess.user_TABLE_COL_ID+
				")",
				null);
		return result;
	}
}
