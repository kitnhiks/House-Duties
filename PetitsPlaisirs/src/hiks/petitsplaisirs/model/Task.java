package hiks.petitsplaisirs.model;

import java.util.Date;

public class Task {
	private int id;
	private String idRelation;
	private String nom;
	private int point;
	private String category;
	private int priority;
	private Date deadline;
	private Date doneDate;
	
	public Task(){}
	 
	public Task(String nom){
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
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}

	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}

	/**
	 * @param cat the cat to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the cat
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * @param idRelation the idRelation to set
	 */
	public void setIdRelation(String idRelation) {
		this.idRelation = idRelation;
	}

	/**
	 * @return the idRelation
	 */
	public String getIdRelation() {
		return idRelation;
	}

	/**
	 * @param doneDate the doneDate to set
	 */
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

	/**
	 * @return the doneDate
	 */
	public Date getDoneDate() {
		return doneDate;
	}
	
}
