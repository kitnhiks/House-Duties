package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.TaskHandler;
import hiks.petitsplaisirs.model.Category;
import hiks.petitsplaisirs.model.Task;
import hiks.petitsplaisirs.ListScreen;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Interface d'une liste de tâches
 * @author hiks
 *
 */
public abstract class ListTasksActivity extends ListScreen {

	protected Category[] listeCategories;
	protected Task[] listeTasks;
	protected String[] tasksNames;
	protected TaskHandler th;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void generateLayout(){
		setContentView(R.layout.list_tasks);

		// Add buttons listener
		findViewById(R.id.addTasks).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				// go to AddUserTasks
				Intent newIntent = new Intent(view.getContext(), ListHouseTasksActivity.class);
				newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
				newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
				newIntent.putExtra("hiks.petitsplaisirs.userName", userName);
				newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
				startActivityForResult(newIntent, 1);
			}
		});
	}

	protected void generateList(){
		th = new TaskHandler(this);
		listeTasks = th.getTasks();
		int nbTasks = listeTasks.length;

		tasksNames = new String[nbTasks];
		ListView lv = (ListView) findViewById(R.id.listTasks);

		for (int i=0; i<nbTasks; i++){
			tasksNames[i] = listeTasks[i].getNom();
		}

		if (nbTasks == 0){
			tasksNames = new String[1];
			tasksNames[0] = getApplicationContext().getString(R.string.no_task);
		}else{

			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.added_task), Toast.LENGTH_SHORT).show();
					setResult(onTaskClicked(parent, view, position, id));
					finish();
				}
			});
		}
		
		lv.setAdapter(new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1 , 
				tasksNames));
	}

	protected abstract int onTaskClicked(AdapterView<?> parent, View view, int position, long id);
}
