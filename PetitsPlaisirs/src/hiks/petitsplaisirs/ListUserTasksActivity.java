package hiks.petitsplaisirs;

import java.util.Date;

import hiks.petitsplaisirs.dao.TaskHandler;
import hiks.petitsplaisirs.model.Task;
import hiks.petitsplaisirs.ListScreen;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Liste les taches d'un user (userId)
 * @author hiks
 *
 */
public class ListUserTasksActivity extends ListScreen{

	private String[] tasksNames;
	private Task[] listeTasks;
	private TaskHandler th;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void generateLayout(){
		setContentView(R.layout.list_user_tasks);

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

		TextView txt = (TextView)findViewById(R.id.listUserTasksTxt);
		txt.setText(txt.getText()+" "+userName);
	}

	protected void generateList(){
		th = new TaskHandler(this);
		listeTasks = th.getUserTodoTasks(userId);
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
			
			registerForContextMenu(lv);

			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Task clickedTask = listeTasks[position];
					int res = th.setTaskDone(clickedTask, new Date());
					if (res == 0){
						Toast.makeText(getApplicationContext(), R.string.unknown_error,
								Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), clickedTask.toString(),
								Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					}
				}
			});
		}
		
		lv.setAdapter(new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1 , 
				tasksNames));
	}

	/***** Gestion du menu *****/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_user_tasks_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add_task :
			// go to AddUserTasks
			Intent newIntent = new Intent(this, ListHouseTasksActivity.class);
			newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
			newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
			newIntent.putExtra("hiks.petitsplaisirs.userName", userName);
			newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
			startActivityForResult(newIntent, 1);
			// TODO : on arrive là ???
			Toast.makeText(getApplicationContext(), "On arrive là !",
					Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_user_tasks_ctx_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Task clickedTask = listeTasks[info.position-1];
		switch(item.getItemId()) {
		case R.id.remove_task :
			th.removeTaskFromUser(clickedTask);
			generateList();
			return true;
		}
		return super.onContextItemSelected(item);
	}
}
