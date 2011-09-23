package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.SessionHandler;
import android.content.Intent;
import android.os.Bundle;


/**
 * Liste 
 * @author hiks
 *
 */
public abstract class ListScreen extends android.app.Activity {
	protected int houseId;
	protected int userId;
	protected String userName;
	protected String userPass;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        houseId = extras.getInt("hiks.petitsplaisirs.houseId");
        userName = extras.getString("hiks.petitsplaisirs.userName");
        userPass = extras.getString("hiks.petitsplaisirs.userPass");
        
        SessionHandler sh = new SessionHandler(this);
        // TODO : check userID ??? over securitas
        userId = sh.checkUser(houseId, userName, userPass);

        if (userId < 0){
        	// TODO : gérer la securitas, ouvrir une activité différente ?
        	setContentView(R.layout.error);
        }else{
        	generateLayout();
        	generateList();
        }
    }
    
    protected abstract void generateLayout();
    
    protected abstract void generateList();
    
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) 
    {
    	generateList();
    }
}
