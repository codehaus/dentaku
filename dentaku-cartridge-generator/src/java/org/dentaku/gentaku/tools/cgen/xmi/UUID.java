/**
 * Name:        UUIDGen.java
 * Author:      uk-dave (http://www.uk-dave.com)
 * Date:        23th July 2003
 * Description: Generates random-number based UUIDs
 *              This program should really use java.security.SecureRandom to ensure that random numbers are truly random (well, as near as),
 *              but because this program is to be used on TINI's and SNAP's plain old java.util.Random has to be used instead.
 * Useful Links:
 *              What is a UUID: http://www.dsps.net/uuid.html
 *              UUID Spec: http://www.opengroup.org/onlinepubs/9629399/apdxa.htm
 *              Proper Java UUID Generator: http://www.doomdark.org/doomdark/proj/jug/
 */
package org.dentaku.gentaku.tools.cgen.xmi;

import java.util.Random;

public class UUID {
    private static final String hexChars = "0123456789abcdef";
    private static final byte INDEX_TYPE = 6;
    private static final byte INDEX_VARIATION = 8;
    private static final byte TYPE_RANDOM_BASED = 4;
    private Random rnd;

    /**
     * Constructor. Instantiates the rnd object to generate random numbers.
     */
    public UUID() {
        rnd = new Random(System.currentTimeMillis());
    }

    /**
     * Generates a random UUID and returns the String representation of it.
     *
     * @returns a String representing a randomly generated UUID.
     */
    public String generateUUID() {
        // Generate 128-bit random number
        byte[] uuid = new byte[16];
        nextRandomBytes(uuid);

        // Set various bits such as type
        uuid[INDEX_TYPE] &= (byte) 0x0F;
        uuid[INDEX_TYPE] |= (byte) (TYPE_RANDOM_BASED << 4);
        uuid[INDEX_VARIATION] &= (byte) 0x3F;
        uuid[INDEX_VARIATION] |= (byte) 0x80;

        // Convert byte array into a UUID formated string
        StringBuffer b = new StringBuffer(36);
        for (int i = 0; i < 16; i++) {
            if (i == 4 || i == 6 || i == 8 || i == 10) b.append('-');
            int hex = uuid[i] & 0xFF;
            b.append(hexChars.charAt(hex >> 4));
            b.append(hexChars.charAt(hex & 0x0F));
        }

        // Return UUID
        return b.toString();
    }

    /**
     * Generates random bytes and places them into a user-supplied byte array.
     * The number of random bytes produced is equal to the length of the byte array.
     * Nicked from java.util.Random becuase the stupid SNAP board doesn't have this method!
     *
     * @param bytes the non-null byte array in which to put the random bytes.
     */
    private void nextRandomBytes(byte[] bytes) {
        int numRequested = bytes.length;
        int numGot = 0, rand = 0;
        while (true) {
            for (int i = 0; i < 4; i++) {
                if (numGot == numRequested)
                    return;
                rand = (i == 0 ? rnd.nextInt() : rand >> 8);
                bytes[numGot++] = (byte) rand;
            }
        }
    }

}