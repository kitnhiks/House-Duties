package hiks.petitsplaisirs.dao;

public abstract class HouseHandler {
 	
	/**
	 * Supprime une maison
	 * @param houseName le nom de la maison
	 * @return nombre de ligne supprimées si la suppression a eu lieu (-1 si la maison n'existait pas, -99 en cas de problème)
	 */
	public abstract int removeHouse(String houseName, String housePass);
	
	/**
	 * Ajoute une nouvelle maison
	 * @param houseName le nom de la maison
	 * @param housePass le mot de passe de la maison
	 * @param userName le nom de l'habitant
	 * @param userPass le mot de passe de l'habitant
	 * @return l'id de la maison (-1 si elle existe déjà, -99 en cas de problème)
	 */
	public abstract int addHouse(String houseName, String housePass, String userName, String userPass);
 
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
	public abstract int addUsers(String[] usersName, String[] usersPass, int houseId);
}
