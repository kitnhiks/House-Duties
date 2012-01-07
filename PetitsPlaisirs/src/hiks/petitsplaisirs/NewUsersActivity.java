package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.ErrorHandler;
import hiks.petitsplaisirs.dao.HouseHandler;
import hiks.petitsplaisirs.dao.HouseHandlerSQL;
import hiks.petitsplaisirs.dao.SessionHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class NewUsersActivity extends Activity implements OnClickListener{
	private int houseId;
	private int userId;
	private String userName;
	private String userPass;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Set Layout
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		houseId = extras.getInt("hiks.petitsplaisirs.houseId");
		userName = extras.getString("hiks.petitsplaisirs.userName");
		userPass = extras.getString("hiks.petitsplaisirs.userPass");

		SessionHandler sh = new SessionHandler(this);
		// TODO : check userID ??? over securitas
		userId = sh.checkUser(houseId, userName, userPass);

		if (userId < 0){
			// TODO : gérer la securitas, ouvrir une activité différente ?
			setContentView(R.layout.error);
		}else{

			setContentView(R.layout.new_user);

			// Add buttons listener
			View validButton = findViewById(R.id.validButton);
			validButton.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.validButton:
			String newUserName = ((EditText) findViewById(R.id.newUserName)).getText().toString();
			String newUserPass = ((EditText) findViewById(R.id.newUserPass)).getText().toString();
			String newUserEmail = ((EditText) findViewById(R.id.newUserEmail)).getText().toString();
			int res = createUser(newUserName, newUserPass, newUserEmail);
			if (res == ErrorHandler.MANDATORY_FIELDS) { // Champs obligatoires
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.missing_mandatories)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
			}else if (res == ErrorHandler.USER_ALREADY_EXISTS) { // Cas d'un habitant déjà existant
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.user_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
			}else if (res == ErrorHandler.UNKNOWN) { // Cas d'erreur
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.unknown_error)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
			}else{
				setResult(RESULT_OK);
				// TODO : Envoyer un mail au nouvel utilisateur
				new AlertDialog.Builder(this)
				.setTitle(R.string.info_title)
				.setMessage(R.string.added_user)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
				finish();
			}
			break;
		}
	}

	/**
	 * Ajoute un nouvel habitant
	 * @return : ErrorHandler.MANDATORY_FIELDS s'il manque des champs obligatoires,
	 * 			 ErrorHandler.UNKNOWN en cas de problème, 
	 * 			 sinon l'id du user créé
	 */
	private int createUser(String newUserName, String newUserPass, String newUserEmail) {
		int returnValue = ErrorHandler.UNKNOWN;
		// On vérifie les champs obligatoires

		if (newUserName == "" || newUserPass == "" || newUserEmail == ""){
			return ErrorHandler.MANDATORY_FIELDS;
		}


		// TODO : check arguments formats
		
		// On crée l'habitant
		HouseHandlerSQL hc = new HouseHandlerSQL(this);
		returnValue = hc.addUser(newUserName, newUserPass, newUserEmail, houseId);
		
		return returnValue;
	}
}