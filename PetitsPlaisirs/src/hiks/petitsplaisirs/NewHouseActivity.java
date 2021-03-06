package hiks.petitsplaisirs;

import hiks.petitsplaisirs.dao.ErrorHandler;
import hiks.petitsplaisirs.dao.HouseHandler;
import hiks.petitsplaisirs.dao.HouseHandlerSQL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class NewHouseActivity extends Activity implements OnClickListener{

	private String newHouseName;
	private String newHousePass;
	private String newUserName;
	private String newUserEmail;
	private String newUserPass;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		// Set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_house);
	
		// Add buttons listener
		View validButton = findViewById(R.id.validButton);
		validButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
		switch (view.getId()) {
		case R.id.validButton:
			newHouseName = ((EditText) findViewById(R.id.newHouseName)).getText().toString();
			newHousePass = ((EditText) findViewById(R.id.newHousePass)).getText().toString();
			newUserName = ((EditText) findViewById(R.id.newUserName)).getText().toString();
			newUserEmail = ((EditText) findViewById(R.id.newUserEmail)).getText().toString();
			newUserPass = ((EditText) findViewById(R.id.newUserPass)).getText().toString();

			int houseId = createHouse();
		    if (houseId == ErrorHandler.MANDATORY_FIELDS) { // Cas des champs obligatoires manquant
				new AlertDialog.Builder(this)
					.setTitle(R.string.alert_title)
					.setMessage(R.string.missing_mandatories)
					.setPositiveButton(R.string.alert_ok_button, null)
					.show();
		    } else if (houseId == ErrorHandler.HOUSE_ALREADY_EXISTS) { // Cas d'une maison d�j� existante
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.house_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
		    } else if (houseId == ErrorHandler.EMAIL_ALREADY_EXISTS) { // Cas d'un email d�j� existante
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.email_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
		    } else if (houseId == ErrorHandler.USER_ALREADY_EXISTS) { // Cas d'un email d�j� existante
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.user_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
		    } else if (houseId == ErrorHandler.UNKNOWN) { // Cas d'erreur
				new AlertDialog.Builder(this)
					.setTitle(R.string.alert_title)
					.setMessage(R.string.unknown_error)
					.setPositiveButton(R.string.alert_ok_button, null)
					.show();
		    }else{
				// On va � la Home
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
     * Ajoute une nouvelle maison
     * @return : ErrorHandler.MANDATORY_FIELDS s'il manque des champs obligatoire,
     * 			ErrorHandler.ALREADY_EXISTS si la maison existe d�j� 
     * 			ErrorHandler.UNKNOWN en cas de probl�me, 
     * 			sinon l'id de la maison cr��e
     */
    private int createHouse() {
		int returnValue = ErrorHandler.UNKNOWN;
		// On v�rifie les champs obligatoires
		if ("".equals(newHouseName) || "".equals(newUserName) || "".equals(newUserEmail) || "".equals(newUserPass) || "".equals(newHousePass)) {
		    returnValue = ErrorHandler.MANDATORY_FIELDS;
		} else {
		    // On cr�e la maison
			HouseHandlerSQL hc = new HouseHandlerSQL(this);
		    returnValue = hc.addHouse(newHouseName, newHousePass, newUserEmail, newUserName, newUserPass);
		    // TODO : sortir la cr�ation de l'habitant de la cr�ation de la maison
		}
		return returnValue;
    }
}
