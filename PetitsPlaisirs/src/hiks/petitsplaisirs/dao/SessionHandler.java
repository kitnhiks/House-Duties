package hiks.petitsplaisirs.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SessionHandler {
 
	// TODO : coller les infos de base dans DBAccess ou autre static	
	
	private SQLiteDatabase bdd;
 
	private DBAccess maBase;
 
	public SessionHandler(Context context){
		//On créer la BDD et sa table
		maBase = new DBAccess(context, null);
	}
	
	public SessionHandler(Context context, DBAccess base){
		//On créer la BDD et sa table
		maBase = base;
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
	 * Vérifie l'existence d'une maison à partir de son nom
	 * @param houseName le nom de la maison
	 * @return l'id de la maison si tout s'est bien passé (ErrorHandler.UNKNOWN en cas de problème et ErrorHandler.NOT_EXISTS si la maison n'existe pas)
	 */
	public int checkHouse(String houseName, String housePass){
		String METHOD_NAME = "checkHouse";
		try{
			// Check mandatory
			if ("".equals(houseName) || "".equals(housePass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			open();
			
			// Check l'id de la maison
			int houseId = getHouseId(houseName, housePass);
			
			close();
			
			return houseId;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return ErrorHandler.UNKNOWN;
		}
	}
	
	/**
	 * Vérifier l'existence d'un habitant d'une maison à partir de son nom
	 * @param houseId l'id de la maison
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe de l'habitant
	 * @return l'id de l'habitant de la maison si tout s'est bien passé (ErrorHandler.UNKNOWN en cas de problème et ErrorHandler.NOT_EXISTS si l'user n'existe pas)
	 */
	public int checkUser(int houseId, String userName, String userPass){
		String METHOD_NAME = "checkUser";
		try{
			// Check mandatory
			if ("".equals(houseId) || "".equals(userName) || "".equals(userPass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			open();
			
			// Check l'id du user
			int userId = getUserId(houseId, userName, userPass);
			
			close();
			
			return userId;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return ErrorHandler.UNKNOWN;
		}
	}
	
	/**
	 * Vérifier l'existence d'un habitant à partir de son mail
	 * @param userEmail le mail de l'habitant
	 * @param userPass le mot de passe de l'habitant
	 * @return l'id de l'habitant de la maison si tout s'est bien passé (ErrorHandler.UNKNOWN en cas de problème et ErrorHandler.NOT_EXISTS si l'user n'existe pas)
	 */
	public int checkUser(String userEmail, String userPass){
		String METHOD_NAME = "checkUser";
		try{
			// Check mandatory
			if ("".equals(userEmail) || "".equals(userPass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			open();
			
			// Check l'id du user
			int userId = getUserId(userEmail, userPass);
			
			close();
			
			return userId;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return ErrorHandler.UNKNOWN;
		}
	}
	
	/**
	 * Retourne l'id d'un user associé à une maison à partir du nom et du mot de passe 
	 * @param houseId
	 * @param userName
	 * @param userPass
	 * @return l'id du user s'il existe, ErrorHandler.NOT_EXISTS sinon
	 */
	private int getUserId(int houseId, String userName, String userPass){
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
				DBAccess.user_TABLE_COL_MDP + " = \"" + userPass +"\"", 
				null, null, null, null);

		int userId = ErrorHandler.UNKNOWN;
		
		if (c.getCount() == 0){
			userId = ErrorHandler.NOT_EXISTS;
		}else{
			c.moveToFirst();
			userId = c.getInt(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_ID));
		}
		c.close();
 
		return userId;
	}

	/**
	 * Retourne l'id d'un user associé à une maison à partir du nom et du mot de passe 
	 * @param userEmail
	 * @param userPass
	 * @return l'id du user s'il existe, ErrorHandler.NOT_EXISTS sinon
	 */
	private int getUserId(String userEmail, String userPass){
		Cursor c = bdd.query(DBAccess.user_TABLE, 
				new String[] {
					DBAccess.user_TABLE_COL_ID
					},
				DBAccess.user_TABLE_COL_EMAIL + " = \"" + userEmail +"\" AND "+
				DBAccess.user_TABLE_COL_MDP + " = \"" + userPass +"\"", 
				null, null, null, null);

		int userId = ErrorHandler.UNKNOWN;
		
		if (c.getCount() == 0){
			userId = ErrorHandler.NOT_EXISTS;
		}else{
			c.moveToFirst();
			userId = c.getInt(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_ID));
		}
		c.close();
 
		return userId;
	}
	
	/**
	 * Retourne l'id d'une maison à partir du nom
	 * @param houseName
	 * @param housePass
	 * @return l'id de la maison si elle existe, ErrorHandler.NOT_EXISTS sinon et ErrorHandler.UNKNOWN si il y'a plusieurs résultats
	 */
	private int getHouseId(String houseName, String housePass){
		Cursor c = bdd.query(DBAccess.house_TABLE, 
				new String[] {DBAccess.house_TABLE_COL_ID, DBAccess.house_TABLE_COL_NOM}, 
				"LOWER("+DBAccess.house_TABLE_COL_NOM + ") = \"" + houseName.toLowerCase() +"\" AND "+
				DBAccess.house_TABLE_COL_MDP + " = \"" + housePass +"\"", 
				null, null, null, null);

		int houseId = ErrorHandler.UNKNOWN;
		
		if (c.getCount() == 0){
			houseId = ErrorHandler.NOT_EXISTS;
		}else if (c.getCount() > 1){
			houseId = ErrorHandler.UNKNOWN;
		}else{
			c.moveToFirst();
			houseId = c.getInt(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_ID));
		}
		
		c.close();
 
		return houseId;
	}
}
