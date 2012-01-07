package hiks.petitsplaisirs.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBAccess extends SQLiteOpenHelper {
	
	public static final int VERSION_BDD = 6;
	public static final String NAME_BDD = "petitsplaisirs.db";
	
	public static final String categorie_TABLE = "pp_categorie";
	public static final String categorie_TABLE_COL_ID = "pp_categorie_ID";
	public static final String categorie_TABLE_COL_NOM = "pp_categorie_NOM";
	
	public static final String categorie_house_TABLE = "pp_categorie_house";
	
	public static final String house_TABLE = "pp_house";
	public static final String house_TABLE_COL_ID = "pp_house_ID";
	public static final String house_TABLE_COL_NOM = "pp_house_NOM";
	public static final String house_TABLE_COL_MDP = "pp_house_MDP";
	
	// TODO : rename tout en anglais
	public static final String tache_TABLE = "pp_tache";
	public static final String tache_TABLE_COL_ID = "pp_tache_ID";
	public static final String tache_TABLE_COL_IDCATEGORIE = "pp_tache_IDCATEGORIE";
	public static final String tache_TABLE_COL_NOM = "pp_tache_NOM";
	public static final String tache_TABLE_COL_POINT = "pp_tache_POINT";
	
	public static final String tache_house_TABLE = "pp_tache_house";
	public static final String tache_house_TABLE_COL_IDMAISON = "pp_tache_house_IDMAISON";
	public static final String tache_house_TABLE_COL_IDTACHE = "pp_tache_house_IDTACHE";
	public static final String tache_house_TABLE_COL_IDRELATION = "pp_tache_house_IDRELATION";
	public static final String tache_house_TABLE_COL_TODO = "pp_tache_house_TODO";
	public static final String tache_house_TABLE_COL_PRIORITE = "pp_tache_house_PRIORITE";
	public static final String tache_house_TABLE_COL_DEADLINE = "pp_tache_house_DEADLINE";
	public static final String tache_house_TABLE_COL_FAITLE = "pp_tache_house_FAITLE";
	
	public static final String tache_user_TABLE = "pp_tache_user";
	public static final String tache_user_TABLE_COL_IDTACHE = "pp_tache_user_IDTACHE";
	public static final String tache_user_TABLE_COL_IDUSER = "pp_tache_user_IDUSER";
	public static final String tache_user_TABLE_COL_IDMAISON = "pp_tache_user_IDMAISON";
	public static final String tache_user_TABLE_COL_IDRELATION = "pp_tache_user_IDRELATION";
	public static final String tache_user_TABLE_COL_TODO = "pp_tache_user_TODO";
	public static final String tache_user_TABLE_COL_PRIORITE = "pp_tache_user_PRIORITE";
	public static final String tache_user_TABLE_COL_DEADLINE = "pp_tache_user_DEADLINE";
	public static final String tache_user_TABLE_COL_FAITLE = "pp_tache_user_FAITLE";
	
	public static final String user_TABLE = "pp_user";
	public static final String user_TABLE_COL_ID = "pp_user_ID";
	public static final String user_TABLE_COL_NOM = "pp_user_NOM";
	public static final String user_TABLE_COL_MDP = "pp_user_MDP";
	public static final String user_TABLE_COL_EMAIL = "pp_user_EMAIL";
	
	public static final String user_house_TABLE = "pp_user_house";
	public static final String user_house_TABLE_COL_ID = "pp_user_house_ID";
	public static final String user_house_TABLE_COL_IDUSER = "pp_user_house_IDUSER";
	public static final String user_house_TABLE_COL_IDHOUSE = "pp_user_house_IDHOUSE";
	public static final String user_house_TABLE_COL_ISADMIN = "pp_user_house_ISADMIN";
	
	private static final String CREATE_TABLE_categorie = 
		"CREATE TABLE "+categorie_TABLE+" ("+
		categorie_TABLE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
		categorie_TABLE_COL_NOM+" TEXT"+
		");";
	
	private static final String CREATE_TABLE_categorie_house = 
		"CREATE TABLE "+categorie_house_TABLE+" ("
		+"IDMAISON INTEGER,"
		+"IDCATEGORIE INTEGER,"
		+"NOM TEXT,"
		+"PRIMARY KEY (IDMAISON,IDCATEGORIE)"+
		");";
	
	private static final String CREATE_TABLE_house = 
		"CREATE TABLE "+house_TABLE+" ("+
		house_TABLE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
		house_TABLE_COL_NOM+" TEXT,"+
		house_TABLE_COL_MDP+" TEXT"+
		");";
	
	private static final String CREATE_TABLE_tache = 
		"CREATE TABLE "+tache_TABLE+" ("+
		tache_TABLE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
		tache_TABLE_COL_IDCATEGORIE+" INTEGER,"+
		tache_TABLE_COL_NOM+" TEXT,"+
		tache_TABLE_COL_POINT+" INTEGER"+
		");";
	
	private static final String CREATE_TABLE_tache_house = 
		"CREATE TABLE "+tache_house_TABLE+" ("+
		tache_house_TABLE_COL_IDTACHE+" INTEGER,"+
		tache_house_TABLE_COL_IDMAISON+" INTEGER,"+
		tache_house_TABLE_COL_IDRELATION+" TEXT,"+
		tache_house_TABLE_COL_TODO+" INTEGER,"+
		tache_house_TABLE_COL_PRIORITE+" INTEGER,"+
		tache_house_TABLE_COL_DEADLINE+" REAL,"+
		tache_house_TABLE_COL_FAITLE+" REAL"+
		");";
		
	private static final String CREATE_TABLE_tache_user = 
		"CREATE TABLE "+tache_user_TABLE+" ("+
		tache_user_TABLE_COL_IDTACHE+" INTEGER,"+
		tache_user_TABLE_COL_IDUSER+" INTEGER,"+
		tache_user_TABLE_COL_IDMAISON+" INTEGER,"+
		tache_user_TABLE_COL_IDRELATION+" TEXT,"+
		tache_user_TABLE_COL_TODO+" INTEGER,"+
		tache_user_TABLE_COL_PRIORITE+" INTEGER,"+
		tache_user_TABLE_COL_DEADLINE+" REAL,"+
		tache_user_TABLE_COL_FAITLE+" REAL"+
		");";
	
	private static final String CREATE_TABLE_user = 
		"CREATE TABLE "+user_TABLE+" ("+
		user_TABLE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
		user_TABLE_COL_NOM+" TEXT,"+
		user_TABLE_COL_MDP+" TEXT,"+
		user_TABLE_COL_EMAIL+" TEXT"+
		");";
	
	private static final String CREATE_TABLE_user_house = 
		"CREATE TABLE "+user_house_TABLE+" ("+
		user_house_TABLE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
		user_house_TABLE_COL_IDUSER+" INTEGER,"+
		user_house_TABLE_COL_IDHOUSE+" INTEGER,"+
		user_house_TABLE_COL_ISADMIN+" INTEGER"+
		");";
	
	private SQLiteDatabase bdd;
	
	public DBAccess(Context context, CursorFactory factory) {
		super(context, NAME_BDD, factory, VERSION_BDD);
	}
	
	public void open(){
		//on ouvre la BDD en écriture
		bdd = this.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(CREATE_TABLE_categorie);
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Vie quotidienne')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Animal')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Enfant')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Bébé')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Fête')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Course')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Ménage')");
		db.execSQL("INSERT INTO "+categorie_TABLE+" ("+categorie_TABLE_COL_NOM+") VALUES ('Travaux')");
		
		/*db.execSQL(CREATE_TABLE_categorie_house);*/
		db.execSQL(CREATE_TABLE_tache);
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (1, 'Préparer repas', 13)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (1, 'Vaiselle', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (1, 'Vider poubelle', 2)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (1, 'Récupérer courrier', 1)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (2, 'Sortir animal', 3)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (2, 'Nourrir animal', 1)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (2, 'Laver animal', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (3, 'Sortir enfant', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (3, 'Garder enfant', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (4, 'Laver bébé', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (4, 'Changer bébé', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (4, 'Nourir bébé', 3)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (5, 'Préparer déco', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (5, 'Préparer nourriture', 13)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (5, 'DJ', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (5, 'Faire le service', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (6, 'Faire les courses', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (6, 'Préparer la liste des courses', 2)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (7, 'Ranger une pièce', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (7, 'Laver une pièce', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (7, 'Aspirateur dans une pièce', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (7, 'Balai dans une pièce', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (7, 'Laver carreau d''une pièce', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (8, 'Réparation', 5)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (8, 'Peinture dans une pièce', 8)");
		db.execSQL("INSERT INTO "+tache_TABLE+" ("+tache_TABLE_COL_IDCATEGORIE+", "+tache_TABLE_COL_NOM+", "+tache_TABLE_COL_POINT+") VALUES (8, 'Papier-peint dans une pièce', 8)");
		
		db.execSQL(CREATE_TABLE_tache_house);
		db.execSQL(CREATE_TABLE_tache_user);
		db.execSQL(CREATE_TABLE_user);
		db.execSQL(CREATE_TABLE_user_house);
		db.execSQL(CREATE_TABLE_house);
	}
 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS "+categorie_TABLE);
		/*db.execSQL("DROP TABLE IF EXISTS "+categorie_house_TABLE);*/
		db.execSQL("DROP TABLE IF EXISTS "+tache_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+tache_house_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+tache_user_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+house_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+user_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+user_house_TABLE);
		onCreate(db);
	}
}
