package com.aikidonord.metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Stage {
	
	// Date de début du stage, si stage sur plusieurs jour, date du premier jour
	private Date dateDebut;
	private String tarif;
	private String type;
	private String description;
	
	// map lieu peut contenir les clés "salle", "rue", "codepostal", "ville"
	private HashMap<String, String> mapLieu = new HashMap<String, String>();
	
	// liste de tableau de String représentant les animateurs. 0 -> nom; 1-> grade
	private List<String[]> listeAnimateur = new ArrayList<String[]>(); 
	
	/* map contenant les horaires
	 * les dates des jours en clé, la liste des horaire des jours en valeurs.
	 *  chaque valeur est une liste contenant les horaires dans un tableau 0 -> heureDebut, 1 -> heureFin, 2 -> description spécifique à l'horaire
	 */
	private LinkedHashMap<Date, List<String[]>> horaires = new LinkedHashMap<Date, List<String[]>>();
	
	/**
	 * Constructeur vide. On laisse l'application remplir les données
	 */
	public Stage() {
		
	}

	/**
	 * @return the dateDebut
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut the dateDebut to set
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return the tarif
	 */
	public String getTarif() {
		return tarif;
	}

	/**
	 * @param tarif the tarif to set
	 */
	public void setTarif(String tarif) {
		this.tarif = tarif;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the mapLieu
	 */
	public HashMap<String, String> getMapLieu() {
		return mapLieu;
	}

	/**
	 * @return the listeAnimateur
	 */
	public List<String[]> getListeAnimateur() {
		return listeAnimateur;
	}

	/**
	 * @return the horaires
	 */
	public LinkedHashMap<Date, List<String[]>> getHoraires() {
		return horaires;
	} 
	
	
	/**
	 * ajoute un animateur à la liste
	 * @param tab tableau de String représentant l'animateur
	 */
	public void addANimateur(String[] tab) {
		this.listeAnimateur.add(tab);
	}
	
	
	/**
	 * ajoute un horaire à la liste
	 * @param d
	 * @param horaire
	 */
	public void addHoraire(Date d, String[] horaire) {
		
		List<String[]> tmp; 
		
		if (this.horaires.containsKey(d)) {
			tmp = this.horaires.get(d);
		} else {
			tmp = new ArrayList<String[]>();	
		}
		
		tmp.add(horaire);
		this.horaires.put(d, tmp);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param mapLieu the mapLieu to set
	 */
	public void setMapLieu(HashMap<String, String> mapLieu) {
		this.mapLieu = mapLieu;
	}
	
	
	
	
	
	
	

}
