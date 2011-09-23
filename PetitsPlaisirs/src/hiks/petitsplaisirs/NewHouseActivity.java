package hiks.petitsplaisirs;

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
	private String newUserName;
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
			newUserName = ((EditText) findViewById(R.id.newUserName)).getText().toString();
			newUserPass = ((EditText) findViewById(R.id.newUserPass)).getText().toString();

			int houseId = createHouse();
		    if (houseId == -1) { // Cas des champs obligatoires manquant
				new AlertDialog.Builder(this)
					.setTitle(R.string.alert_title)
					.setMessage(R.string.missing_mandatories)
					.setPositiveButton(R.string.alert_ok_button, null)
					.show();
		    } else if (houseId == -2) { // Cas d'une maison déjà existante
				new AlertDialog.Builder(this)
				.setTitle(R.string.alert_title)
				.setMessage(R.string.house_already_exists)
				.setPositiveButton(R.string.alert_ok_button, null)
				.show();
		    } else if (houseId == -99) { // Cas d'erreur
				new AlertDialog.Builder(this)
					.setTitle(R.string.alert_title)
					.setMessage(R.string.unknown_error)
					.setPositiveButton(R.string.alert_ok_button, null)
					.show();
		    }else{
				// On va à la Home
				Intent newIntent = new Intent(this, HomeActivity.class);
				newIntent.putExtra("hiks.petitsplaisirs.houseId", houseId);
				newIntent.putExtra("hiks.petitsplaisirs.userName", newUserName);
				newIntent.putExtra("hiks.petitsplaisirs.userPass", newUserPass);
				startActivity(newIntent);
		    }
		    break;
		}
    }

    /**
     * Ajoute une nouvelle maison
     * @return : -1 s'il manque des champs obligatoire,
     * 			-2 si la maison existe déjà 
     * 			-99 en cas de problème, 
     * 			sinon l'id de la maison créée
     */
    private int createHouse() {
		int returnValue = -99;
		// On vérifie les champs obligatoires
		if ("".equals(newHouseName) || "".equals(newUserName) || "".equals(newUserPass)) {
		    returnValue = -1;
		} else {
		    // On crée la maison
		    HouseHandler hc = new HouseHandlerSQL(this);
		    returnValue = hc.addHouse(newHouseName, newHouseName, newUserName, newUserPass);
		    if (returnValue == -1){returnValue = -2;}// Cas d'une maison déjà existante
		}
		return returnValue;
    }
}
