package application.controller;

import application.entity.WordsList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppRestController {

    @PostMapping(value = "/words")
    public WordsList wordGame(@RequestBody WordsList JsonWords){
        int count = 0;
        String lastLetter = "";

        for(String word : JsonWords.getWords()){
            word = word.trim();
            if(!word.isEmpty()){
                if(count == 0){
                    lastLetter = word.substring(word.length()-1);
                    count++;
                    continue;
                }
                if(word.substring(0,1).equalsIgnoreCase(lastLetter)){
                    lastLetter = word.substring(word.length()-1);
                    count++;
                }else break;
            }else break;
        }
        String[] correctWords = new String[count];
        for(int i = 0; i < count; i++){
            correctWords[i] = JsonWords.getWords()[i];
        }
        JsonWords.setWords(correctWords);
        return JsonWords;
    }


}
