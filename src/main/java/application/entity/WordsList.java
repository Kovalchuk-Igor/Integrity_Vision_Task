package application.entity;

public class WordsList {

    private String[] words;

    public WordsList(){}

    public WordsList(String[] words){
        this.words = words;
    }

    public void setWords(String[] words){
        this.words = words;
    }

    public String[] getWords(){
        return words;
    }

}
