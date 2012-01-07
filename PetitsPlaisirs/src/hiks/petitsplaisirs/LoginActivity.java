package hiks.petitsplaisirs;


import hiks.petitsplaisirs.dao.ErrorHandler;
import hiks.petitsplaisirs.dao.SessionHandler;
import hiks.petitsplaisirs.dao.UserHandler;
import hiks.petitsplaisirs.model.House;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginActivity extends Activity implements OnClickListener, OnFocusChangeListener{

	private String houseName;
	private String userEmail;
	private String userPass;
	private SharedPreferences prefs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		userEmail = prefs.getString("userEmail", "");
		userPass = prefs.getString("userPass", "");
		boolean isHelpEnabled = prefs.getBoolean("isHelpEnabled", true);
		
		if (isHelpEnabled){
			new AlertDialog.Builder(this)
			.setTitle(R.string.help_title)
			.setMessage(R.string.help_login)
			.setPositiveButton(R.string.help_close_button, null)
			.setNegativeButton(R.string.help_stop_button, 
					new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							SharedPreferences.Editor editor = prefs.edit();
					        editor.putBoolean("isHelpEnabled", false);
					        editor.commit();
						}
			} )
			.show();
		}
		
		
		Intent newIntent;
		newIntent = new Intent(this, SplashScreen.class);
		startActivityForResult(newIntent, 0);

		setContentView(R.layout.login);

		// Add buttons listener
		Button validButton = (Button) findViewById(R.id.validButton);
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
		
		// On ajoute des listener sur les champs texte pour gérer le texte qui s'affiche ou pas.
		findViewById(R.id.userEmail).setOnFocusChangeListener(this);
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
		userEmail = ((EditText) findViewById(R.id.userEmail)).getText().toString();		
		userPass = ((EditText) findViewById(R.id.userPass)).getText().toString();
		// Check mandatory
		if ("".equals(userEmail) || "".equals(userPass) ||
				getResources().getText(R.string.enter_user_email).equals(userEmail) || 
				getResources().getText(R.string.enter_user_pass).equals(userPass)) {
			/*new AlertDialog.Builder(this)
			.setTitle(R.string.alert_title)
			.setMessage(R.string.missing_mandatories)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			 */
			// TODO : DEBUG
			userEmail = "hiks99@toto.com";
			userPass = "password";
		}
		
		// on vérifie l'existence du user
		SessionHandler l = new SessionHandler(this);
		int userId = l.checkUser(userEmail, userPass);
		if (userId == ErrorHandler.NOT_EXISTS){
			new AlertDialog.Builder(this)
			.setTitle(R.string.alert_title)
			.setMessage(R.string.unknown_user)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			return;
		}else if (userId == ErrorHandler.UNKNOWN){
			new AlertDialog.Builder(this)
			.setTitle(R.string.error_title)
			.setMessage(R.string.unknown_error)
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
			return;
		}

		// Save login
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", userEmail);
        editor.putString("userPass", userPass);
        editor.commit();
        
        
        UserHandler uh = new UserHandler(this);
		House [] listeHouses = uh.getUserHouses(userId);
		int nbHouses = listeHouses.length;
		newIntent = new Intent();
		
        if (nbHouses == 0){
        	// TODO : gérer l'association à une maison
        	new AlertDialog.Builder(this)
			.setTitle(R.string.error_title)
			.setMessage(R.string.unknown_error + " (pas de maison)")
			.setPositiveButton(R.string.alert_ok_button, null)
			.show();
        	return;
        }else if (nbHouses == 1){
			// go to Homepage de la house
			newIntent.setClass(this, HomeActivity.class);
			newIntent.putExtra("hiks.petitsplaisirs.houseId", listeHouses[0].getId());
			newIntent.putExtra("hiks.petitsplaisirs.userId", userId);
			newIntent.putExtra("hiks.petitsplaisirs.userEmail", userEmail);
			newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
        }else{
			// Go to House view
        	newIntent.setClass(this, ListHousesActivity.class);
			newIntent.putExtra("hiks.petitsplaisirs.userEmail", userEmail);
			newIntent.putExtra("hiks.petitsplaisirs.userPass", userPass);
        }
        startActivity(newIntent);
	}


	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.userEmail:
			if (arg1){
				if (getResources().getText(R.string.enter_user_email).equals(((EditText) findViewById(R.id.userEmail)).getText().toString())){
					((EditText) findViewById(R.id.userEmail)).setText("");
				}
			}else{
				if ("".equals(((EditText) findViewById(R.id.userEmail)).getText().toString())){
					((EditText) findViewById(R.id.userEmail)).setText(R.string.enter_user_email);
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
			startActivityForResult(newIntent, 1);
			break;
		case R.id.joinHouse :
			newIntent = new Intent(this, LinkUserHouseActivity.class);
			startActivityForResult(newIntent, 1);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK){
			Bundle extras = data.getExtras();
			userEmail = extras.getString("hiks.petitsplaisirs.userEmail");
	        userPass = extras.getString("hiks.petitsplaisirs.userPass");
	        ((EditText) findViewById(R.id.userEmail)).setText(userEmail);	
	        ((EditText) findViewById(R.id.userPass)).setText(userPass);
	        ((Button) findViewById(R.id.validButton)).performClick();
	        		
		}
	}
}