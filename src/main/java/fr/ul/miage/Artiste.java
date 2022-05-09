package fr.ul.miage;

public class Artiste {

    String artist;

    public Artiste(String artist){
        this.artist = artist;
    }
    public Artiste(){
        super();
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    


    @Override
    public String toString() {
        return "{" +
            " artist='" + getArtist() + "'" +
            "}";
    }
}
