package hiks.petitsplaisirs;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashScreen extends Activity {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 2000;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Message msg = new Message(); 
        msg.what = STOPSPLASH; 
        splashHandler.sendMessageDelayed(msg, SPLASHTIME); 
    }

    private Handler splashHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
	    	switch (msg.what) {
	    		case STOPSPLASH:
	    			setResult(RESULT_OK);
	    			finish();
			    	//remove SplashScreen from view
//	    			Intent newIntent = new Intent(PetitsPlaisirs.this, LoginActivity.class);
//	    			startActivity(newIntent);
			    	break;
	    	}
	    	super.handleMessage(msg);
    	}
    };
}