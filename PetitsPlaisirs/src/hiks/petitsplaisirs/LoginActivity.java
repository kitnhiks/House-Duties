package hiks.petitsplaisirs;


import hiks.petitsplaisirs.dao.SessionHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener, OnFocusChangeListener{

	private String houseName;
	private String userName;
	private String userPass;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent newIntent;
		newIntent = new Intent(this, SplashScreen.class);
		startActivityForResult(newIntent, 0);

		setContentView(R.layout.login);

		// Add buttons listener
		View validButton = findViewById(R.id.validButton);
		validButton.setOnClickListener(this);

		// On met le focus sur le bouton pour pas qu'il soit sur les champs texte
		validButton.setFocusable(true);
		validButton.requestFocus();
		validButton.setFocusableInTouchMode(true);
		validButton.requestFocusFromTouch();
		// Pour que le click passe à travers le onFocus, on ajoute le onTouch event
		validButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if ( event.getAction() == MotionEvent.ACTION_UP) {
					switch (view.getId()) {
					case R.id.validButton:
						handleValidButton();
						break;
					}
				}
				return true;
			}
		});

		//findViewById(R.id.addHouse).setOnClickListener(this);

		// On ajoute des listener sur les champs texte pour gérer le texte qui s'affiche ou pas.
		findViewById(R.id.houseName).setOnFocusChangeListener(this);
		findViewById(R.id.userName).setOnFocusChangeListener(this);
		findViewById(R.id.userPass).setOnFocusChangeListener(this);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.validButton:
			handleValidButton();
			break;
		}
	}

	private void handleValidButton(){
		Intent newIntent;
		houseName = ((EditText) findViewById(R.id.houseName)).getText().toString();
		userName = ((EditText) findViewById(R.id.userName)).getText().toString();		
		userPass = ((EditText) findViewById(R.id.userPass)).getText().toString();
		// Check mandatory
		if ("".equals(houseName) || "".equals(userName) || "".equals(userPass) ||
				getResources().getText(R.string.enter_house_name).equals(houseName) ||
				getResources().getText(R.string.enter_user_name).equals(userName) || 
				getResources().getText(R.string.enter_user_pass).equals(userPass)) {
			/*new AlertDialog.Builder(this)
			.setTitle(R.string.alert_title)
			.setMessage(R.string.missing_mandatories)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			 */
			// TODO : DEBUG
			houseName = "mamienne";
			userName = "hiks";
			userPass = "xanaudin";
		}
		SessionHandler l = new SessionHandler(this);
		int houseId = l.checkHouse(houseName);
		if (houseId == -1){
			new AlertDialog.Builder(this)
			.setTitle(R.string.alert_title)
			.setMessage(R.string.unknown_house)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			return;
		}else if (houseId == -99){
			new AlertDialog.Builder(this)
			.setTitle(R.string.error_title)
			.setMessage(R.string.unknown_error)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			return;
		}

		int userId = l.checkUser(houseId, userName, userPass);
		if (userId == -1){
			new AlertDialog.Builder(this)
			.setTitle(R.string.alert_title)
			.setMessage(R.string.unknown_user)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			return;
		}else if (houseId == -99){
			new AlertDialog.Builder(this)
			.setTitle(R.string.error_title)
			.setMessage(R.string.unknown_error)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			return;
		}

		// Go to House view
		newIntent = new Intent(this, HomeActivity.class);
		newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
		newIntent.putExtra("hiks.petitsplaisirs.userName", userName);
		newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);

		startActivity(newIntent);
	}


	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.houseName:
			if (arg1){
				if (getResources().getText(R.string.enter_house_name).equals(((EditText) findViewById(R.id.houseName)).getText().toString())){
					((EditText) findViewById(R.id.houseName)).setText("");
				}
			}else{
				if ("".equals(((EditText) findViewById(R.id.houseName)).getText().toString())){
					((EditText) findViewById(R.id.houseName)).setText(R.string.enter_house_name);
				}
			}
			break;
		case R.id.userName:
			if (arg1){
				if (getResources().getText(R.string.enter_user_name).equals(((EditText) findViewById(R.id.userName)).getText().toString())){
					((EditText) findViewById(R.id.userName)).setText("");
				}
			}else{
				if ("".equals(((EditText) findViewById(R.id.userName)).getText().toString())){
					((EditText) findViewById(R.id.userName)).setText(R.string.enter_user_name);
				}
			}
			break;
		case R.id.userPass:
			if (arg1){
				((EditText) findViewById(R.id.userPass)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				((EditText) findViewById(R.id.userPass)).setText("");
			}else{
				if ("".equals(((EditText) findViewById(R.id.userPass)).getText().toString())){
					((EditText) findViewById(R.id.userPass)).setInputType(InputType.TYPE_CLASS_TEXT);
					((EditText) findViewById(R.id.userPass)).setText(R.string.enter_user_pass);
				}
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent newIntent;
		switch(item.getItemId()) {
		case R.id.addHouse :
			newIntent = new Intent(this, NewHouseActivity.class);
			startActivity(newIntent);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}