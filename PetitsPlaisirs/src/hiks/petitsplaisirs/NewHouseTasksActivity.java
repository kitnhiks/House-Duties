package hiks.petitsplaisirs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * Gère l'ajout des taches d'une maison (houseId)
 * @author hiks
 *
 */
public class NewHouseTasksActivity extends ListTasksActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int onTaskClicked(AdapterView<?> parent, View view, int position, long id) {
		int res = 0;
		if (listeTasks != null){
			res = th.addTaskToHouse(listeTasks[position], houseId);
		}
		return res;
	}
}
