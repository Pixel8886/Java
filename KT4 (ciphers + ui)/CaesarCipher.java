
public class CaesarCipher {


    static final String RU = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    static final String EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String encrypt(String text, int key) {
        return apply(text, key);
    }


    public static String decrypt(String text, int key) {
        return apply(text, -key);
    }


    private static String apply(String text, int key) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = shiftChar(chars[i], key);
        }
        return new String(chars);
    }


    static int letterIndex(char c) {
        char upper = Character.toUpperCase(c);
        int i = RU.indexOf(upper);
        return i >= 0 ? i : EN.indexOf(upper);
    }


    static char shiftChar(char c, int shift) {
        char upper = Character.toUpperCase(c);
        String alphabet = RU.indexOf(upper) >= 0 ? RU : EN;
        int i = alphabet.indexOf(upper);
        if (i < 0) {
            return c;
        }



        char result = alphabet.charAt(Math.floorMod(i + shift, alphabet.length()));

        return Character.isLowerCase(c) ? Character.toLowerCase(result) : result;
    }
}
