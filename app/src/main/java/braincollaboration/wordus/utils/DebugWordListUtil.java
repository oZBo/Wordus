package braincollaboration.wordus.utils;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.model.Word;

/**
 * Created by evhenii on 31.07.17.
 */

public class DebugWordListUtil {

    public static List<Word> getStubWordList(){
        ArrayList<Word> list = new ArrayList<>();
        for(int i = 0; i < 23; i++){
            Word word = new Word();
            word.setWordName("Aaa");
            word.setWordDescription("Ffff");
            list.add(word);
        }
        Word word = new Word();
        word.setWordName("Bbb");
        word.setWordDescription("zxc");
        list.add(word);
        word = new Word();
        word.setWordName("Ggg");
        word.setWordDescription("zxc");
        list.add(word);
        word = new Word();
        word.setWordName("Kkk");
        word.setWordDescription("zxc");
        list.add(word);
        word = new Word();
        word.setWordName("Ppp");
        word.setWordDescription("zxc");
        list.add(word);
        word = new Word();
        word.setWordName("Zzzz");
        word.setWordDescription("zxc");
        list.add(word);
        return list;
    }

}
