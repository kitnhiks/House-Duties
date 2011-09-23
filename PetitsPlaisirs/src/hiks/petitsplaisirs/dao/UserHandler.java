package hiks.petitsplaisirs.dao;

import hiks.petitsplaisirs.model.User;
import android.content.Context;
import android.database.Cursor;

public class UserHandler {

	// TODO : coller les infos de base dans DBAccess ou autre static
 
	private DBAccess maBase;
 
	public UserHandler(Context context){
		//On créer la BDD et sa table
		maBase = new DBAccess(context, null);
	}
	
	public UserHandler(Context context, DBAccess base){
		//On créer la BDD et sa table
		maBase = base;
	}
	
	public DBAccess getBase(){
		return maBase;
	}
	
	/**
	 * Renvoie les utilisateurs ordonnés par point puis alphabétiquement.
	 * @param houseId
	 * @return Le Ladder
	 */
	public User[] getUsers(int houseId){
		maBase.open();
		
		String strQuery = "(" +
		DBAccess.user_TABLE+
		" LEFT JOIN "+
		DBAccess.tache_user_TABLE+
		" ON ("+
			DBAccess.tache_user_TABLE_COL_IDUSER+
			" = "+DBAccess.user_TABLE_COL_ID+
			" AND "+
			DBAccess.tache_user_TABLE_COL_FAITLE + " is not null"+
			")" +
		")" +
		" LEFT JOIN "+
		DBAccess.tache_TABLE+
		" ON ("+DBAccess.tache_user_TABLE_COL_IDTACHE+
		" = "+DBAccess.tache_TABLE_COL_ID+")";
		Cursor c = maBase.getBDD().query(
				strQuery
				, 
 				new String[] {
					DBAccess.user_TABLE_COL_ID, 
					DBAccess.user_TABLE_COL_NOM,
					"SUM ("+DBAccess.tache_TABLE_COL_POINT+") as points"
					}, 
				DBAccess.user_TABLE_COL_IDHOUSE + " = \"" + houseId +"\"", 
				null, DBAccess.user_TABLE_COL_ID, null, "points DESC, "+DBAccess.user_TABLE_COL_NOM);

		
		int nbUsers = c.getCount();
 
		User[] lu = new User[nbUsers];
		User u;
		int cpt = 0;
		c.moveToFirst();
        while (c.isAfterLast() == false) {
        	u = new User(c.getString(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_NOM)));
        	u.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_ID)));
        	u.setPoints(c.getInt(c.getColumnIndexOrThrow("points")));
        	lu[cpt] = u;
        	cpt++;
       	    c.moveToNext();
        }
		c.close();
		maBase.close();
		
		return lu;
	}
}
