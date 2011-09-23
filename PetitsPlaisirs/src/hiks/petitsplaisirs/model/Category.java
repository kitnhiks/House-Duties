package hiks.petitsplaisirs.model;

public class Category {
	private int id;
	private String nom;
	private Task[] tasksList;

	public Category(){}
 
	public Category(String nom){
		this.setNom(nom);
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param tasksList the tasksList to set
	 */
	public void setTasksList(Task[] tasksList) {
		this.tasksList = tasksList;
	}

	/**
	 * @return the tasksList
	 */
	public Task[] getTasksList() {
		return tasksList;
	}
}
