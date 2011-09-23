package hiks.petitsplaisirs.dao;

import hiks.petitsplaisirs.model.Container;
import hiks.petitsplaisirs.model.House;
import hiks.petitsplaisirs.utils.EngineConfiguration;

import java.util.List;
import org.restlet.resource.ClientResource;
import android.util.Log;

public class HouseController {

	public final ClientResource cr = new ClientResource(EngineConfiguration.gae_path + "/house");

	public HouseController() {
		EngineConfiguration.getInstance();
	}

	public long create(House house) throws Exception {
		final HouseControllerInterface hci = cr.wrap(HouseControllerInterface.class);
		try {
			long res = hci.create(house);
			Log.i("UserController", "Creation success !");
			return res;
		} catch (Exception e) {
			Log.i("UserController", "Creation failed !");
			throw e;
		}
	}

	public List<Object>  getAllHouses() {
		final HouseControllerInterface hci = cr.wrap(HouseControllerInterface.class);
		Container content = hci.getAllHouses();
		return content.getObjectList();
	}
}