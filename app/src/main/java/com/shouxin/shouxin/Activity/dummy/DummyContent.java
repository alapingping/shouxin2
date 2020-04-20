package com.shouxin.shouxin.Activity.dummy;

import com.shouxin.shouxin.DataModel.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    private  static List<Word> words = new ArrayList<>();

    public static List<Word> getWords() {
        words.add(new Word("字母","A(a)","\t\t拇指伸出，指尖向上，其余四指握拳。","http://www.xkrjy.com/sy/img/1.gif",1));
        words.add(new Word("字母","B(a)","\t\t拇指伸出，指尖向上，其余四指握拳。","http://www.xkrjy.com/sy/img/1.gif",1));
        words.add(new Word("字母","C(a)","\t\t拇指伸出，指尖向上，其余四指握拳。","http://www.xkrjy.com/sy/img/1.gif"));
        words.add(new Word("字母","D(a)","\t\t拇指伸出，指尖向上，其余四指握拳。","http://www.xkrjy.com/sy/img/1.gif"));
        words.add(new Word("字母","E(a)","\t\t拇指伸出，指尖向上，其余四指握拳。","http://www.xkrjy.com/sy/img/1.gif"));
        words.add(new Word("字母","F(a)","\t\t拇指伸出，指尖向上，其余四指握拳。","http://www.xkrjy.com/sy/img/1.gif"));
        words.get(0).setCollected(1);
        return words;
    }
}
