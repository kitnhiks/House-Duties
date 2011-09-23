package hiks.petitsplaisirs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * Gère l'ajout des taches d'un user (userId)
 * @author hiks
 *
 */
public class NewUserTasksActivity extends ListTasksActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int onTaskClicked(AdapterView<?> parent, View view, int position, long id) {
		int res = 0;
		if (listeTasks != null){
			res = th.addTaskToUser(listeTasks[position], userId);
		}
		return res;
	}
}
