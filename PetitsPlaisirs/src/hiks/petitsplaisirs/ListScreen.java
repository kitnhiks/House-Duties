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
	protected int userId;
	protected String userEmail;
	protected String userPass;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        userEmail = extras.getString("hiks.petitsplaisirs.userEmail");
        userPass = extras.getString("hiks.petitsplaisirs.userPass");
        
        SessionHandler sh = new SessionHandler(this);
        // TODO : check userID ??? over securitas
        userId = sh.checkUser(userEmail, userPass);

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
