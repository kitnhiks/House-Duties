package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.HouseHandler;
import hiks.petitsplaisirs.dao.HouseHandlerSQL;
import hiks.petitsplaisirs.dao.SessionHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

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
			int res = createUser(newUserName, newUserPass);
			if (res == -1) { // Cas des champs obligatoires manquant
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.missing_mandatories)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
			} else if (res == -99) { // Cas d'erreur
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.unknown_error)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
			}else{
				setResult(RESULT_OK);
				finish();
			}
			break;
		}
	}

	/**
	 * Ajoute un nouvel habitant
	 * @return : -1 s'il manque des champs obligatoires,
	 * 			-99 en cas de problème, 
	 * 			sinon l'id de la maison créée
	 */
	private int createUser(String newUserName, String newUserPass) {
		int returnValue = -99;
		// On vérifie les champs obligatoires

		if (newUserName == "" || newUserPass == ""){
			return -1;
		}
		// On crée l'habitant
		HouseHandler uc = new HouseHandlerSQL(this);
		returnValue = uc.addUser(newUserName, newUserPass, houseId);

		return returnValue;
	}
}
