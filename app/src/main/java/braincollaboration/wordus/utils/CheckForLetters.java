package braincollaboration.wordus.utils;

public class CheckForLetters {
    public static String checkIsThisALetters(String text) {
        char[] word = text.toCharArray();
        String upperWord = "";
        int notLetters = 0;
        // checks is a target word consist of letters
        for (char aWord : word) {
            if (!Character.isLetter(aWord)) {
                notLetters++;
            }
        }

        // makes first letter in to upper case
        if (notLetters == 0) {
            word[0] = Character.toUpperCase(word[0]);

            for (char aWord : word) {
                upperWord += Character.toString(aWord);
            }
        }
        return upperWord;
    }
}
