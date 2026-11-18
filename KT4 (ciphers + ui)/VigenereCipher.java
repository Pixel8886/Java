
public class VigenereCipher {


    public static String encrypt(String text, String key) {
        return apply(text, key, 1);
    }



    public static String decrypt(String text, String key) {
        return apply(text, key, -1);
    }


    private static String apply(String text, String key, int direction) {


        if (key.isEmpty()) {
            throw new IllegalArgumentException("Введите ключ");
        }


        int[] shifts = new int[key.length()];
        for (int i = 0; i < shifts.length; i++) {
            shifts[i] = CaesarCipher.letterIndex(key.charAt(i));
            if (shifts[i] < 0) {
                throw new IllegalArgumentException("Ключ должен состоять только из букв");
            }
        }


        char[] chars = text.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (CaesarCipher.letterIndex(chars[i]) < 0) {
                continue;
            }

            chars[i] = CaesarCipher.shiftChar(chars[i], direction * shifts[pos]);

            pos = (pos + 1) % shifts.length;
        }
        return new String(chars);
    }
}
