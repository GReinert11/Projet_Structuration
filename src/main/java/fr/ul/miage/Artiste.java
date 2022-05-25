package fr.ul.miage;

public class Artiste {

    public Artiste(String name, String playcount, String listener) {
        this.name = name;
        this.playcount = playcount;
        this.listener = listener;
    }



  /*  public Artiste(String name) {
        this.name = name;
    }*/


    public Artiste(String name, String playcount) {
        this.name = name;
        this.playcount = playcount;
    }
    public Artiste(String playcount) {
        this.playcount = playcount;
    }

   



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPlaycount() {
        return playcount;
    }
    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }
    public String getListener() {
        return listener;
    }
    public void setListener(String listener) {
        this.listener = listener;
    }



    private String name;
    private String playcount;
    private String listener;

   
    public Artiste(){
        super();
    }

  
    


    @Override
    public String toString() {
        return "{" +
            " artist='" + getName() + "'" +
            "}";
    }
}
