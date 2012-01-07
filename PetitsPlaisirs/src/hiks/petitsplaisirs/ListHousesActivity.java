package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.UserHandler;
import hiks.petitsplaisirs.model.House;
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
 * Liste les maisons d'un user (userId)
 * @author hiks
 *
 */
public class ListHousesActivity extends ListScreen{

	private String[] housesNames;
	private House[] listeHouses;
	private UserHandler uh; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void generateLayout(){
		setContentView(R.layout.list_houses);

	}

	protected void generateList(){

		uh = new UserHandler(this);
		listeHouses = uh.getUserHouses(userId);
		int nbHouses = listeHouses.length;

		housesNames = new String[nbHouses];
		ListView lv = (ListView) findViewById(R.id.listHouses);

		for (int i=0; i<nbHouses; i++){
			housesNames[i] = listeHouses[i].getNom();
		}

		if (nbHouses == 0){
			housesNames = new String[1];
			housesNames[0] = getApplicationContext().getString(R.string.no_house);
		}else{
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// go to Homepage de la house
					Intent newIntent = new Intent();
					newIntent.setClass(parent.getContext(), HomeActivity.class);
					newIntent.putExtra("hiks.petitsplaisirs.houseId", listeHouses[position].getId());
					newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
					newIntent.putExtra("hiks.petitsplaisirs.userEmail", userEmail);
					newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
					startActivity(newIntent);
				}
			});
		}
		
		lv.setAdapter(new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1 , 
				housesNames));
	}
}
