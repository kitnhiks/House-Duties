package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.SessionHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity implements OnClickListener {

	private int houseId;
	private int userId;
	private String userName;
	private String userPass;
	
	/** Called when the activity is first created. */
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
        	setContentView(R.layout.error);
        }else{
        
        	setContentView(R.layout.home);
        	
        	// Add buttons listener
	        findViewById(R.id.showLadder).setOnClickListener(this);
	        findViewById(R.id.showHouseTodo).setOnClickListener(this);
	        findViewById(R.id.showUserDone).setOnClickListener(this);
        }
    }
    
    @Override
    public void onClick(View view) {
    	Intent newIntent = new Intent();
    	newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
		newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
		newIntent.putExtra("hiks.petitsplaisirs.userName", userName);
		newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
    	
    	switch (view.getId()) {
		case R.id.showLadder:
			// Affiche les user de la maison
			newIntent.setClass(this, ListUsersActivity.class);
			startActivity(newIntent);
			break;
		case R.id.showHouseTodo:
			// Affiche les tâches à faire de la maison 
			newIntent.setClass(this, ListHouseTasksActivity.class);
			startActivity(newIntent);
			break;
		case R.id.showUserDone:
			// Affiche les tâches faites par un utilisateur
			newIntent.setClass(this, ListUserTasksActivity.class);
			startActivity(newIntent);
			break;
    	}
    }
	/***** Gestion du menu *****/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add_user :
			// go to AddUserTasks
			Intent newIntent = new Intent(this, NewUsersActivity.class);
			newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
			newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
			newIntent.putExtra("hiks.petitsplaisirs.userName", userName);
			newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
			startActivityForResult(newIntent, 1);
			// TODO : on arrive là ???
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
