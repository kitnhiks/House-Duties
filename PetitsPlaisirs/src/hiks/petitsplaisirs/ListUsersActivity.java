package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.UserHandler;
import hiks.petitsplaisirs.model.User;
import hiks.petitsplaisirs.ListScreen;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Liste les user d'une maison (houseId)
 * @author hiks
 *
 */
public class ListUsersActivity extends ListScreen{

	private String[] usersNames;
	private User[] listeUsers;
	private UserHandler uh; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void generateLayout(){
		setContentView(R.layout.list_users);

	}

	protected void generateList(){

		uh = new UserHandler(this);
		listeUsers = uh.getUsers(houseId);
		int nbUsers = listeUsers.length;

		usersNames = new String[nbUsers];
		ListView lv = (ListView) findViewById(R.id.listUsers);

		for (int i=0; i<nbUsers; i++){
			usersNames[i] = listeUsers[i].getNom()+"("+listeUsers[i].getPoints()+")";
		}

		if (nbUsers == 0){
			usersNames = new String[1];
			usersNames[0] = getApplicationContext().getString(R.string.no_user);
		}else{
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent newIntent = new Intent();
					newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
					newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
					newIntent.putExtra("hiks.petitsplaisirs.userName", userName);
					newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
					// go to ListUserTasks
					if (listeUsers[position].getId() == userId){
						newIntent.setClass(parent.getContext(), ListUserTasksActivity.class);
						startActivityForResult(newIntent, 1);
					}else{
						newIntent.setClass(parent.getContext(), ListAnotherUserTasksActivity.class);
						newIntent.putExtra("hiks.petitsplaisirs.anotherUserId", listeUsers[position].getId());
						newIntent.putExtra("hiks.petitsplaisirs.anotherUserName", listeUsers[position].getNom());
						startActivityForResult(newIntent, 1);
					}
				}
			});
		}
		
		lv.setAdapter(new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1 , 
				usersNames));
	}
}
