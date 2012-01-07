package hiks.petitsplaisirs.dao;

import hiks.petitsplaisirs.model.House;
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
	 * Renvoie la liste des maisons d'un user
	 * @param userId
	 * @return La liste des maisons
	 */
	public House[] getUserHouses(int userId){
		maBase.open();
		
		String strQuery = 
			"(" +
				DBAccess.house_TABLE+
				" INNER JOIN "+
					DBAccess.user_house_TABLE+
					" ON ("+
						DBAccess.user_house_TABLE_COL_IDHOUSE+
						" = "+DBAccess.house_TABLE_COL_ID+
					")"+
			")";
		
		Cursor c = maBase.getBDD().query(
				strQuery
				, 
 				new String[] {
					DBAccess.house_TABLE_COL_ID, 
					DBAccess.house_TABLE_COL_NOM
					}, 
				DBAccess.user_house_TABLE_COL_IDUSER + " = \"" + userId +"\"", 
				null, null, null, DBAccess.house_TABLE_COL_NOM);

		
		int nbHouses = c.getCount();
 
		House[] lh = new House[nbHouses];
		House h;
		int cpt = 0;
		c.moveToFirst();
        while (c.isAfterLast() == false) {
        	h = new House();
        	h.setId(c.getInt(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_ID)));
        	h.setNom(c.getString(c.getColumnIndexOrThrow(DBAccess.house_TABLE_COL_NOM)));
        	lh[cpt] = h;
        	cpt++;
       	    c.moveToNext();
        }
		c.close();
		maBase.close();
		
		return lh;
	}
	
	
	/**
	 * Renvoie les utilisateurs ordonnés par point puis alphabétiquement.
	 * @param houseId
	 * @return Le Ladder
	 */
	public User[] getUsers(int houseId){
		maBase.open();
		
		String strQuery = 
		"(" +
			"(" +
				DBAccess.user_TABLE+
				" INNER JOIN "+
					DBAccess.user_house_TABLE+
					" ON ("+
						DBAccess.user_house_TABLE_COL_IDUSER+
						" = "+DBAccess.user_TABLE_COL_ID+
					")"+
			")"+
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
			" ON ("+
				DBAccess.tache_user_TABLE_COL_IDTACHE+
				" = "+DBAccess.tache_TABLE_COL_ID+
			")";
		
		Cursor c = maBase.getBDD().query(
				strQuery
				, 
 				new String[] {
					DBAccess.user_TABLE_COL_ID, 
					DBAccess.user_TABLE_COL_EMAIL,
					DBAccess.user_TABLE_COL_NOM,
					"SUM ("+DBAccess.tache_TABLE_COL_POINT+") as points"
					}, 
				DBAccess.user_house_TABLE_COL_IDHOUSE + " = \"" + houseId +"\"", 
				null, DBAccess.user_TABLE_COL_ID, null, "points DESC, "+DBAccess.user_TABLE_COL_NOM);

		
		int nbUsers = c.getCount();
 
		User[] lu = new User[nbUsers];
		User u;
		int cpt = 0;
		c.moveToFirst();
        while (c.isAfterLast() == false) {
        	u = new User(c.getString(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_EMAIL)));
        	u.setNom(c.getString(c.getColumnIndexOrThrow(DBAccess.user_TABLE_COL_NOM)));
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
