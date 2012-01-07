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
		//On cr�er la BDD et sa table
		maBase = new DBAccess(context, null);
		sh = new SessionHandler(context);
	}

	public HouseHandlerSQL(Context context, DBAccess base){
		//On cr�er la BDD et sa table
		maBase = base;
		sh = new SessionHandler(context);
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
	 * @return nombre de ligne supprim�es si la suppression a eu lieu (ErrorHandler.NOT_EXISTS si la maison n'existait pas, ErrorHandler.UNKNOWN en cas de probl�me)
	 */
	public int removeHouse(String houseName, String housePass){
		String METHOD_NAME = "removeHouse"; 
		int returnValue = ErrorHandler.UNKNOWN;
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
			result = sh.checkHouse(houseName, housePass);
			if (result == ErrorHandler.NOT_EXISTS){ // Cas de maison non trouv�e
				returnValue = ErrorHandler.NOT_EXISTS;
			}else{
				int houseId = result;

				// Suppression de la maison et des �l�ments associ�s (taches, users...)
				result = deleteHouse(houseId);

				returnValue = result;
			}

			close();

			return returnValue;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			close();
			return ErrorHandler.UNKNOWN;
		}
	}

	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe de l'habitant
	 * @return l'id de la maison (ErrorHandler.ALREADY_EXISTS si elle existe d�j�, ErrorHandler.UNKNOWN en cas de probl�me)
	 */
	public int addHouse(String houseName, String housePass, String newUserEmail, String userName, String userPass){
		int returnValue = ErrorHandler.UNKNOWN;

		returnValue = addHouse (houseName, housePass);
		if (returnValue > 0){ // Si la maison est bien ins�r�e, on ins�re le user
			int houseId = returnValue;

			returnValue = addUser(userName, userPass, newUserEmail, houseId);
			if (returnValue > 0){ // Si tout s'est bien pass�, on retourne l'id de la maison
				returnValue = houseId;
			}else{ // Sinon on supprime la maison qui vient d'�tre ins�r�e
				removeHouse(houseName, housePass);
			}
		}

		return returnValue;
	}

	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @return l'id de la maison (ErrorHandler.ALREADY_EXISTS si elle existe d�j�, ErrorHandler.UNKNOWN en cas de probl�me)
	 */
	private int addHouse(String houseName, String housePass){
		String METHOD_NAME = "addHouse"; 
		int returnValue = ErrorHandler.UNKNOWN;
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
			result = sh.checkHouse(houseName, housePass);
			if (result != ErrorHandler.NOT_EXISTS){ // Cas de maison trouv�e
				returnValue = ErrorHandler.HOUSE_ALREADY_EXISTS;
			}else{

				// Insertion de la maison
				House newHouse = new House(houseName, housePass);
				result = insertHouse(newHouse);
				if (result == ErrorHandler.SQL_ERROR){ // Cas d'erreur
					throw new Exception (METHOD_NAME+" : Erreur � l'insertion de la maison");
				}
	
				// R�cup�ration de l'Id de la maison
				result = sh.checkHouse(houseName, housePass);
				if (result < 0){ // Cas de maison non trouv�e (alors que je viens de l'ins�rer...)
					throw new Exception (METHOD_NAME+" : Erreur pour r�cup�rer ma maison ins�r�e :(");
				}
				returnValue = result;

			}
			
			close();

			return returnValue;
		}catch(Exception e){
			close();
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return ErrorHandler.UNKNOWN;
		}
	}

	/**
	 * Ajoute un nouvel habitant
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe
	 * @param userEmail l'e-mail
	 * @param houseId l'id de la maison
	 * @return l'id du user si tout s'est bien pass� (
	 * 	ErrorHandler.EMAIL_ALREADY_EXISTS, si l'e-mail existe d�j�,
	 *  ErrorHandler.USER_ALREADY_EXISTS, si l'user existe d�j�, 
	 * 	ErrorHandler.UNKNOWN en cas de probl�me
	 * )
	 */
	public int addUser(String userName, String userPass, String userEmail, int houseId){
		// TODO : Copier cette methode pour les autres
		String METHOD_NAME = "addUser"; 
		int returnValue = ErrorHandler.UNKNOWN;
		try{
			// Check arguments
			if ("".equals(userName) || "".equals(userPass) || "".equals(userEmail)) {
				throw new Exception ("Mauvais param�tres");
			}
			
			open();
			int result;

			// Check existence de la maison
			House house = getHouseById(houseId);
			if (house == null){ // Cas de maison non trouv�e
				throw new Exception ("Id de la maison inexistant");
			}

			int userId;
			// V�rifier si l'habitant existe d�j�
			userId = getUserId(userPass, userEmail);
			if (userId == ErrorHandler.NOT_EXISTS){
				if (isEmailKnown(userEmail)){
					returnValue = ErrorHandler.EMAIL_ALREADY_EXISTS;
					throw new Exception (userEmail+" existe d�j�");
				}else{
					// Insertion de l'habitants
					User tmpUser;
					tmpUser = new User(Tools.escapeDataChars(userEmail));
					tmpUser.setMdp(Tools.escapeDataChars(userPass));
					tmpUser.setNom(Tools.escapeDataChars(userName));
					userId = insertUser(tmpUser);
					if (userId == ErrorHandler.SQL_ERROR){ // Cas d'erreur
						throw new Exception ("Erreur � l'insertion du user "+userName);
					}
				}
			}else{
				// V�rifier s'il est li� � la maison
				if (doUserExistsInHouse(userName, userPass, userEmail, houseId)){
					returnValue = ErrorHandler.USER_ALREADY_EXISTS;
					throw new Exception (userName+" existe d�j�");
				}
			}
			
			// V�rifier si le nom est d�j� utilis� pour la maison
			int nbHomonymous = countHomonymous(userName, houseId);
			if (nbHomonymous!=0){
				userName = userName + " ("+(nbHomonymous+1)+")";
			}
			
			result = linkUserHouse(userId, houseId);
			if (result == ErrorHandler.SQL_ERROR){ // Cas d'erreur
				throw new Exception ("Erreur � l'insertion du lien du user "+userName);
			}

			returnValue = userId;
			close();
			return returnValue;
			
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			close();
			return returnValue;
		}
	}

	private int getUserId(String userPass, String userEmail) {
		int returnValue = ErrorHandler.UNKNOWN;
		
		Cursor c = bdd.query(DBAccess.user_TABLE, 
				new String[] {
					DBAccess.user_TABLE_COL_ID
					},
				DBAccess.user_TABLE_COL_EMAIL + " = \"" + userEmail +"\""+" AND "+ 
				DBAccess.user_TABLE_COL_MDP + " = \"" + userPass +"\"", 
				null, null, null, null);

		if (c.getCount() == 0){
			returnValue = ErrorHandler.NOT_EXISTS;
		}else if (c.getCount() == 1){
			c.moveToFirst();
			returnValue = c.getInt(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_ID));
		}
		
		c.close();
		return returnValue;
	}

	private int countHomonymous(String userName, int houseId) {
		int returnValue = 0;
		Cursor c = bdd.query(DBAccess.user_TABLE+
				" INNER JOIN "+
				DBAccess.user_house_TABLE+
				" ON ("+DBAccess.user_house_TABLE_COL_IDUSER+
				" = "+DBAccess.user_TABLE_COL_ID+")", 
				new String[] {
					DBAccess.user_TABLE_COL_ID
					},
				DBAccess.user_house_TABLE_COL_IDHOUSE + " = " + houseId +" AND "+"("+
				"LOWER("+DBAccess.user_TABLE_COL_NOM + ") = \"" + userName.toLowerCase() +"\"" + " OR " +
				"LOWER("+DBAccess.user_TABLE_COL_NOM + ") LIKE \"" + userName.toLowerCase() +" (%)\""+")",
				null, null, null, null);

		returnValue = c.getCount();
		c.close();
		return returnValue;
	}

	private boolean doUserExistsInHouse(String userName, String userPass, String userEmail, int houseId) {
		boolean returnValue = false;
		
		Cursor c = bdd.query(DBAccess.user_TABLE+
				" INNER JOIN "+
				DBAccess.user_house_TABLE+
				" ON ("+DBAccess.user_house_TABLE_COL_IDUSER+
				" = "+DBAccess.user_TABLE_COL_ID+")", 
				new String[] {
					DBAccess.user_TABLE_COL_ID
					},
				DBAccess.user_house_TABLE_COL_IDHOUSE + " = " + houseId +" AND "+
				"LOWER("+DBAccess.user_TABLE_COL_NOM + ") = \"" + userName.toLowerCase() +"\" AND "+
				DBAccess.user_TABLE_COL_MDP + " = \"" + userPass +"\" AND "+
				DBAccess.user_TABLE_COL_EMAIL + " = \"" + userEmail +"\"", 
				null, null, null, null);

		returnValue = c.getCount() != 0;

		c.close();
 
		return returnValue;
	}

	
	private boolean isEmailKnown(String userEmail) {
		boolean returnValue = false;
		Cursor c = bdd.query(DBAccess.user_TABLE, 
				new String[] {
					DBAccess.user_TABLE_COL_ID
					},
				DBAccess.user_TABLE_COL_EMAIL + " = \"" + userEmail +"\"", 
				null, null, null, null);

		returnValue = c.getCount() != 0;
		c.close();
		return returnValue;
	}

	/**
	 * Insert un habitant dans la base
	 * @param user
	 * @param house
	 * @return The row ID of the newly inserted row, or ErrorHandler.SQL_ERROR if an error occurred
	 */
	private int insertUser(User user){
		ContentValues values = new ContentValues();
		values.put(DBAccess.user_TABLE_COL_NOM, user.getNom());
		values.put(DBAccess.user_TABLE_COL_EMAIL, user.getEmail());
		values.put(DBAccess.user_TABLE_COL_MDP, user.getMdp());
		return (int) bdd.insert(DBAccess.user_TABLE, null, values);
	}

	/**
	 * Insert un lien habitant-maison dans la base
	 * @param user
	 * @param house
	 * @return The row ID of the newly inserted row, or ErrorHandler.SQL_ERROR if an error occurred
	 */
	private int linkUserHouse(int userId, int houseId){
		ContentValues values = new ContentValues();
		values.put(DBAccess.user_house_TABLE_COL_IDUSER, userId);
		values.put(DBAccess.user_house_TABLE_COL_IDHOUSE, houseId);
		return (int) bdd.insert(DBAccess.user_house_TABLE, null, values);
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
	 * @return the row ID of the newly inserted row, or ErrorHandler.SQL_ERROR if an error occurred
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
				" from "+DBAccess.user_TABLE+", "+DBAccess.user_house_TABLE+
				" where "+DBAccess.user_house_TABLE_COL_IDHOUSE+" = "+houseId+
				" and "+DBAccess.user_house_TABLE_COL_IDUSER+" = "+DBAccess.user_TABLE_COL_ID+
				")",
				null);
		// Delete les users associ�s
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
