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
	 * @return l'id de la maison si tout s'est bien passé (-99 en cas de problème et -1 si la maison n'existe pas)
	 */
	public int checkHouse(String houseName){
		String METHOD_NAME = "checkHouse";
		try{
			// Check mandatory
			if ("".equals(houseName)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			open();
			
			// Check l'id de la maison
			int houseId = getHouseId(houseName);
			
			close();
			
			return houseId;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
	
	/**
	 * Vérifier l'existence d'un habitant d'une maison à partir de son nom
	 * @param houseName l'id de la maison
	 * @param userName le nom de l'habitant
	 * @param userMdp le mot de passe de l'habitant
	 * @return l'id de l'habitant de la maison si tout s'est bien passé (-99 en cas de problème et -1 si l'user n'existe pas)
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
			return -99;
		}
	}
	
	/**
	 * Vérifie la connexion d'un user à une maison à partir du nom et du mot de passe 
	 * @param houseName
	 * @param userName
	 * @param userMdp
	 * @return l'id du user s'il existe, -1 sinon
	 */
	private int getUserId(int houseId, String userName, String userPass){
		Cursor c = bdd.query(DBAccess.user_TABLE, 
				new String[] {
					DBAccess.user_TABLE_COL_ID
					},
				DBAccess.user_TABLE_COL_IDHOUSE + " = " + houseId +" AND "+
				DBAccess.user_TABLE_COL_NOM + " = \"" + userName +"\" AND "+
				DBAccess.user_TABLE_COL_MDP + " = \"" + userPass +"\"", 
				null, null, null, null);

		int userId = -99;
		
		if (c.getCount() == 0){
			userId = -1;
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
	 * @return l'id de la maison si elle existe, -1 sinon et -99 si il y'a plusieurs résultats
	 */
	private int getHouseId(String houseName){
		Cursor c = bdd.query(DBAccess.house_TABLE, 
				new String[] {DBAccess.house_TABLE_COL_ID, DBAccess.house_TABLE_COL_NOM}, 
				DBAccess.house_TABLE_COL_NOM + " = \"" + houseName +"\"", 
				null, null, null, null);

		int houseId = -99;
		
		if (c.getCount() == 0){
			houseId = -1;
		}else if (c.getCount() > 1){
			houseId = -99;
		}else{
			c.moveToFirst();
			houseId = c.getInt(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_ID));
		}
		
		c.close();
 
		return houseId;
	}
}
