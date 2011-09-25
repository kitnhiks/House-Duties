package hiks.petitsplaisirs.model;

import java.io.Serializable;

public class House implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private int id;
	private String nom;
	private String mdp;
	private User[] users;

	public House(){}
 
	public House(String nom, String mdp){
		this.nom = nom;
		this.mdp = mdp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}
	
	public String toString(){
		// TODO : ajouter les users
		return "ID : "+id+"\nNom : "+nom+"\nMdp : "+mdp;
	}
}
