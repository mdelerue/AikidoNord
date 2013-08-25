package com.aikidonord.metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;


import android.os.Parcel;
import android.os.Parcelable;

public class Stage implements Parcelable {

    // Date de début du stage, si stage sur plusieurs jour, date du premier jour
    private Date dateDebut;
    private String tarif;
    private String type;
    private String description;
    private String img = "";
    ;
    private String id;

    // map lieu peut contenir les clés "salle", "rue", "codepostal", "ville"
    private HashMap<String, String> mapLieu = new HashMap<String, String>();

    // liste de tableau de String représentant les animateurs. 0 -> nom; 1->
    // grade
    private List<String[]> listeAnimateur = new ArrayList<String[]>();

    /*
     * map contenant les horaires les dates des jours en clé, la liste des
     * horaire des jours en valeurs. chaque valeur est une liste contenant les
     * horaires dans un tableau 0 -> heure, 1 -> description
     * spécifique à l'horaire
     */
    private LinkedHashMap<Date, List<String[]>> horaires = new LinkedHashMap<Date, List<String[]>>();

    /**
     * Constructeur vide. On laisse l'application remplir les données
     */
    public Stage() {

    }

    public Stage(Parcel in) {
        readFromParcel(in);
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
     *
     * @param tab tableau de String représentant l'animateur
     */
    public void addANimateur(String[] tab) {
        this.listeAnimateur.add(tab);
    }

    /**
     * ajoute un horaire à la liste
     *
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


    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * écrit l'objet dans un parcel pour les map et les liste, on déconstruit
     * pour reconstruire à la lecture
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tarif);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeLong(this.dateDebut.getTime());

        // animateurs
        int nbAnimateur = this.listeAnimateur.size();
        // on stock le nombre d'entrée
        dest.writeInt(nbAnimateur);

        // et chaque tableau de String
        for (int i = 0; i < nbAnimateur; i++) {
            dest.writeStringArray(this.listeAnimateur.get(i));
        }

        int nbHoraire = this.horaires.size();
        // on stocke le nombre d'entrée jour
        dest.writeInt(nbHoraire);

        // et pour chaque jour
        for (Entry<Date, List<String[]>> entry : this.horaires.entrySet()) {

            dest.writeLong(entry.getKey().getTime());
            List<String[]> ls = entry.getValue();

            // le nombre d'horaire
            dest.writeInt(ls.size());

            for (int i = 0; i < ls.size(); i++) {
                // et les horaires 1 par 1
                dest.writeStringArray(ls.get(i));
            }

        }

        // le nombre d'entrée dans lieu
        dest.writeInt(this.mapLieu.size());
        for (Entry<String, String> entry : this.mapLieu.entrySet()) {
            // et le couple clé valeur
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }

    }

    /**
     * Set les données à partir de ce qui est contenu dans le Parcel
     *
     * @param in
     */
    public void readFromParcel(Parcel in) {

        this.setTarif(in.readString());
        this.setType(in.readString());
        this.setDescription(in.readString());
        this.setDateDebut(new Date(in.readLong()));


        // animateurs
        int nbAnimateur = in.readInt();

        for (int i = 0; i < nbAnimateur; i++) {
            this.addANimateur(in.createStringArray());
        }

        int nbHoraire = in.readInt();

        // et pour chaque jour
        for (int i = 0; i < nbHoraire; i++) {

            Date d = new Date(in.readLong());

            int nbH = in.readInt();
            for (int j = 0; j < nbH; j++) {
                String[] st = in.createStringArray();
                this.addHoraire(d, st);
            }

        }

        int nbLieu = in.readInt();

        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < nbLieu; i++) {
            map.put(in.readString(), in.readString());

        }

        this.setMapLieu(map);

    }

    public static final Parcelable.Creator<Stage> CREATOR = new Parcelable.Creator<Stage>() {
        public Stage createFromParcel(Parcel in) {
            return new Stage(in);
        }

        public Stage[] newArray(int size) {
            return new Stage[size];
        }
    };

}
