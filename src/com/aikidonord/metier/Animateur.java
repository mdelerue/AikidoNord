package com.aikidonord.metier;

/**
 * User: Marc Delerue
 * Date: 31/05/13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class Animateur {

    private int id;
    private String nom;
    private String prenom;
    private String grade;


    public Animateur(int id, String nom, String prenom, String grade) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.grade = grade;

    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
