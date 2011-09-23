package hiks.petitsplaisirs.dao;

import android.content.Context;

import hiks.petitsplaisirs.model.*;
import hiks.petitsplaisirs.utils.Tools;

public class HouseHandlerRest extends HouseHandler{

	public HouseHandlerRest(Context context){
	}
	
	/**
	 * Supprime une maison
	 * @param houseName le nom de la maison
	 * @return nombre de ligne supprimées si la suppression a eu lieu (-1 si la maison n'existait pas, -99 en cas de problème)
	 */
	public int removeHouse(String houseName, String housePass){
		String METHOD_NAME = "removeHouse"; 
		int returnValue = -99;
		try{
			// Check mandatory
			if ("".equals(houseName) || "".equals(housePass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			houseName = Tools.escapeDataChars(houseName);
			housePass = Tools.escapeDataChars(housePass);
			
			// Check existence de la maison
			
			// Suppression de la maison et des éléments associés (taches, users...)
			returnValue = 1;
			return returnValue;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
	
	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe de l'habitant
	 * @return l'id de la maison (-1 si elle existe déjà, -99 en cas de problème)
	 */
	public int addHouse(String houseName, String housePass, String userName, String userPass){
		int returnValue = -99;
		
		String[] usersName = {userName};
		String[] usersPass = {userPass};
		returnValue = addHouse (houseName, housePass);
		/*if (returnValue > 0){ // Si la maison est bien insérée, on insère le user
			int houseId = returnValue;
			
			returnValue = addUsers(usersName, usersPass, houseId);
			if (returnValue == 0){ // Si tout s'est bien passé, on retourne l'id de la maison
				returnValue = houseId;
			}
		}*/
		
		return returnValue;
	}
	
	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @return l'id de la maison (-1 si elle existe déjà, -99 en cas de problème)
	 */
	private int addHouse(String houseName, String housePass){
		String METHOD_NAME = "addHouse"; 
		int returnValue = -99;
		try{
			// Check mandatory
			if ("".equals(houseName) || "".equals(housePass)) {
				throw new Exception (METHOD_NAME+" : Champs obligatoires manquants");
			}
			
			houseName = Tools.escapeDataChars(houseName);
			housePass = Tools.escapeDataChars(housePass);
			
			long houseId;
			House h = new House();
			h.setNom(houseName);
			h.setMdp(housePass);
			
			final HouseController hc = new HouseController();
			houseId = hc.create(h);
			
			//TODO : à déplacer dans GAE 
			// Check unicité du nom de la maison
			// Insertion de la maison
			// Récupération de l'Id de la maison
			// TODO : passer en long
			returnValue = (int) houseId;
			
			return returnValue;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
 
	/**
	 * Ajoute un nouvel habitant
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe
	 * @param houseId l'id de la maison
	 * @return 0 si tout s'est bien passé (-99 en cas de problème)
	 */
	public int addUser(String userName, String userPass, int houseId){
		String[] usersName = {userName};
		String[] usersPass = {userPass};
		return addUsers(usersName, usersPass, houseId);
	}
	
	/**
	 * Ajoute des nouveaux habitants
	 * @param userName la liste des noms d'habitant
	 * @param userPass la liste des mots de passes
	 * @param houseId l'id de la maison
	 * @return 0 si tout s'est bien passé (-99 en cas de problème)
	 */
	public int addUsers(String[] usersName, String[] usersPass, int houseId){
		String METHOD_NAME = "addUser"; 
		try{
			// Check arguments
			if (usersName == null || usersPass == null || usersName.length != usersPass.length) {
				throw new Exception (METHOD_NAME+" : Mauvais parammètres");
			}
			int nbUser = usersName.length;
			// Check mandatory
			if (nbUser == 0) {
				throw new Exception (METHOD_NAME+" : Aucun habitant à insérer");
			}

			//TODO :
			// Check existence de la maison
			
			// Insertion des habitants de la maison
			
			return 0;
		}catch(Exception e){
			System.out.println(this.getClass().getName()+ " - "+ METHOD_NAME+ " : Erreur : "+e.getMessage());
			return -99;
		}
	}
}
