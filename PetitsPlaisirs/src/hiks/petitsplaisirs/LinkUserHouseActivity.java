package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.ErrorHandler;
import hiks.petitsplaisirs.dao.HouseHandler;
import hiks.petitsplaisirs.dao.HouseHandlerSQL;
import hiks.petitsplaisirs.dao.SessionHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LinkUserHouseActivity extends Activity implements OnClickListener{

	private String houseName;
	private String housePass;
	private String newUserName;
	private String newUserEmail;
	private String newUserPass;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		// Set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.link_user_house);
	
		// Add buttons listener
		View validButton = findViewById(R.id.validButton);
		validButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
		switch (view.getId()) {
		case R.id.validButton:
			houseName = ((EditText) findViewById(R.id.houseName)).getText().toString();
			housePass = ((EditText) findViewById(R.id.housePass)).getText().toString();
			newUserName = ((EditText) findViewById(R.id.newUserName)).getText().toString();
			newUserEmail = ((EditText) findViewById(R.id.newUserEmail)).getText().toString();
			newUserPass = ((EditText) findViewById(R.id.newUserPass)).getText().toString();

			int houseId = linkNewUserToHouse();
		    if (houseId == ErrorHandler.MANDATORY_FIELDS) { // Cas des champs obligatoires manquant
				new AlertDialog.Builder(this)
					.setTitle(R.string.alert_title)
					.setMessage(R.string.missing_mandatories)
					.setPositiveButton(R.string.alert_ok_button, null)
					.show();
		    } else if (houseId == ErrorHandler.EMAIL_ALREADY_EXISTS) { // Cas d'un email déjà existante
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.email_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
		    } else if (houseId == ErrorHandler.USER_ALREADY_EXISTS) { // Cas d'un email déjà existante
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.user_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
		    } else if (houseId==ErrorHandler.NOT_EXISTS){
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.unknown_house)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
			} else if (houseId == ErrorHandler.UNKNOWN) { // Cas d'erreur
				new AlertDialog.Builder(this)
					.setTitle(R.string.alert_title)
					.setMessage(R.string.unknown_error)
					.setPositiveButton(R.string.alert_ok_button, null)
					.show();
		    }else{
				// On va à la Home
				/*Intent newIntent = new Intent(this, HomeActivity.class);*/
		    	Intent newIntent = new Intent(this, LoginActivity.class);
				newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
				newIntent.putExtra("hiks.petitsplaisirs.userEmail", newUserEmail);
				newIntent.putExtra("hiks.petitsplaisirs.userPass", newUserPass);
				/*startActivity(newIntent);*/
		    	// On retourne au login
				setResult(RESULT_OK, newIntent);
				finish();
		    }
		    break;
		}
    }

    /**
     * lie un nouvel user à une maison
     * @return : ErrorHandler.MANDATORY_FIELDS s'il manque des champs obligatoire,
     * 			ErrorHandler.USER_ALREADY_EXISTS si le user existe déjà 
     * 			ErrorHandler.UNKNOWN en cas de problème, 
     * 			sinon l'id du user créé
     */
    private int linkNewUserToHouse() {
		int returnValue = ErrorHandler.UNKNOWN;
		// On vérifie les champs obligatoires
		if ("".equals(houseName) || "".equals(newUserName) || "".equals(newUserEmail) || "".equals(newUserPass) || "".equals(housePass)) {
		    returnValue = ErrorHandler.MANDATORY_FIELDS;
		} else {
			SessionHandler sh = new SessionHandler(this);
			// On récupère la maison
			int houseId = sh.checkHouse(houseName, housePass);
			if (houseId < 0){
				returnValue = houseId; 
			}else{
			    // On crée le user
				HouseHandlerSQL hc = new HouseHandlerSQL(this);
			    returnValue = hc.addUser(newUserName, newUserPass, newUserEmail, houseId);
			    // TODO : sortir la création de l'habitant de la création de la maison
			}
		}
		return returnValue;
    }
}
