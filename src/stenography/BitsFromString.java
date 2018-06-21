package stenography;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)


import java.util.ArrayList;

public class BitsFromString {
    public BitsFromString() {
    }

    public static void main(String[] args) {
        String str = "My dog has fleas.";
        bitsFromString2(str);
    }

    public static ArrayList<Integer> bitsFromString(String str) {
        ArrayList<Integer> bitList = new ArrayList();

        for(int i = 0; i < str.length(); ++i) {
            int mask = 128;
            char c = str.charAt(i);

            for(int bitpos = 7; bitpos >= 0; --bitpos) {
                int bit = (c & mask) == 0 ? 0 : 1;
                System.out.print(bit);
                bitList.add(bit);
                mask >>= 1;
            }

            System.out.println("  " + c);
        }

        System.out.println();
        System.out.println(bitList);
        return bitList;
    }

    public static void bitsFromString2(String str) {
        ArrayList<Integer> bitList = new ArrayList();

        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);

            for(int pos = 128; pos > 0; pos /= 2) {
                int bit = c / pos % 2;
                bitList.add(bit);
                System.out.print(bit);
            }

            System.out.println("  " + c);
        }

        System.out.println();
        System.out.println(bitList);
    }
}
