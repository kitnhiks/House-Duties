package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.TaskHandler;
import hiks.petitsplaisirs.model.Task;
import hiks.petitsplaisirs.ListScreen;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Liste les taches d'un user (userId)
 * @author hiks
 *
 */
public class ListAnotherUserTasksActivity extends ListScreen{

	private int anotherUserId;
	private String anotherUserName;

	private String[] tasksNames;
	private Task[] listeTasks;
	private TaskHandler th;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void generateLayout(){

		anotherUserId = getIntent().getExtras().getInt("hiks.petitsplaisirs.anotherUserId");
		anotherUserName = getIntent().getExtras().getString("hiks.petitsplaisirs.anotherUserName");

		setContentView(R.layout.list_another_user_tasks);

		TextView txt = (TextView)findViewById(R.id.listUserTasksTxt);
		txt.setText(txt.getText()+" "+anotherUserName);
	}

	public void generateList(){
		th = new TaskHandler(this);
		listeTasks = th.getTasksByUser(anotherUserId);
		int nbTasks = listeTasks.length;

		tasksNames = new String[nbTasks];
		ListView lv = (ListView) findViewById(R.id.listUserTasks);

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
					// When clicked, show a toast with the TextView text
					Task clickedTask = listeTasks[position];
					Toast.makeText(getApplicationContext(), clickedTask.toString(), Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		lv.setAdapter(new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1 , 
				tasksNames));
	}
}
