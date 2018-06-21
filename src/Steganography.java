import stenography.EasyBufferedImage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by elliottcrifasi on 9/15/16.
 */
public class Steganography {

     /**
     *
     * @param filename name of the file to hide the message in
     * @param message String representation of message to encode
     * @return
     * @throws IOException
     */

     public static void main(String[] args) {
         try {
             retrieveMessage("boxes.png");
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    public static void hideMessage(String filename, String message) throws IOException {

        // create image
        EasyBufferedImage image = EasyBufferedImage.createImage(filename);

        // get the message in Bits using bit operators
        // and place into Array List
        ArrayList<Integer> bitList = new ArrayList<>();
        for (int i = 0; i < message.length(); i++) {
            int mask = (char) 128;  // set the bitmask
            char c = message.charAt(i);
            for (int bitpos = 7; bitpos >= 0; bitpos--) {

                // pull out the current bit
                int bit = (c & mask) == 0 ? 0 : 1;
                bitList.add(bit);
                // move one bit to the right
                mask >>= 1;
            }

        }

        // create int to hold red value
        int RED = 0;

        // create array of pixel values
        int[] pixels = image.getPixels1D(RED);

        // create int to hold pixel length
        int length = pixels.length;

        // create int to hold binary string length
        int stringLength;

        // int array to hold each binary digit it is to be the size
        // of the bitlist but with 8 extra elements so that each
        // message ending will be denoted by 8 0's
        int[] binaryString = new int[bitList.size()+8];


        // loop through array takes bits from ArrayList and place in array
        for (int i = 0; i < bitList.size(); i++) {
            binaryString[i] = bitList.get(i);
        }
        // fill all empty spots with 0
        for (int b = bitList.size(); b < binaryString.length; b++) {
            binaryString[b] = 0;

        }

        stringLength = binaryString.length;

        // change least order bit to correct value
        for (int c = 0; c < stringLength; c++) {
            // create int to represent current pixel
            int currentPixel = pixels[c];

            // if the current pixel is odd and the binary string starts with 0
            // then we need to change the pixel to be even so that the binary
            // string ends with 0
            if ((binaryString[c] == 0) && currentPixel % 2 == 1) {
                pixels[c]+= 1;
            }
            // if the current pixel is even and the binary string starts with 1
            // then we need to change the pixel to be odd so that the binary
            // string ends with 1
            else if ((binaryString[c] == 1) && currentPixel % 2 == 0) {
                pixels[c]+= 1;
            }

        }

        // set image to edited array
            image.setPixels(pixels, RED);

        // create new file name for modified message
            filename = filename.substring(0,filename.length()-4) + ".stega.png";

        // save the image with correct name
            image.save(filename,2);

        // show the image
            image.show("modified");
    }

    /**
     *
     * @param filename name of the file to decode
     * @return it returns the decoded message as a String
     * @throws IOException
     */
    public static String retrieveMessage(String filename) throws IOException {

        // create instance of EasyBufferedImage object
        EasyBufferedImage image = EasyBufferedImage.createImage(filename);

        // create int variable to hold the color red identifier
        int RED = 0;

        // get the red band of pixels and store in int array
        int[] pixels = image.getPixels1D(RED);

        // create int variable to hold the length of array
        int length = pixels.length;

        // create string to hold the string in binary digits
        String binary = "";

        // create string to hold the message once it has been
        // decoded
        String message = "";

        // loop through array to find the characters of message
        // we do not loop through entire length for efficiency
        // assuming that the message is not extremely large
        for (int i = 0; i < length/50; i++) {

            // if the pixel is even then its binary string ends in 0
            if (pixels[i] % 2 == 0) {
                binary += "0";

            }

            // if the pixel is odd then the binary string ends in 1
            else if (pixels[i] % 2 == 1) {
                binary += "1";

            }

        }

        // loop through array convert int to characters into a string
        for (int i = 0; i < binary.length()/8; i++) {

            // variable to hold the char in the form of int
            int a = Integer.parseInt(binary.substring(8*i,(i+1)*8),2);

            // if the number is not then add to message string
            if (a != 0) {

                // append the char to the message typecasting the int into a char
                message += (char) (a);
            }
            // if the number is 0 then we have reached the end of the message
            // because each message is denoted by 8 zeros
            else {
                break;
            }
        }
            // return decoded message
        return message;
    }
}
