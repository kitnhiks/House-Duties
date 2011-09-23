package hiks.petitsplaisirs.model;

public class User {
	private int id;
	private String nom;
	private String mdp;
	private int points;
 

	public User(){}
 
	public User(String nom){
		this.nom = nom;
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
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public String toString(){
		return "ID : "+id+"\nNom : "+nom+"\nPoints : "+points;
	}
}
