/**
 * 
 */
package com.gl.base.common.utils;

import java.security.SecureRandom;

/**
 * 
 * @author QIANG
 *
 */
public class UUIDGenerator {
    private static final SecureRandom numberGenerator = new SecureRandom();


    public static String randomUUID() {
        /* random */
        byte[] randomBytes = new byte[16];
        numberGenerator.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f; /* clear version */
        randomBytes[6] |= 0x40; /* set to version 4 */
        randomBytes[8] &= 0x3f; /* clear variant */
        randomBytes[8] |= 0x80; /* set to IETF variant */

        /* uuid */
        long msb = 0;
        long lsb = 0;
        assert randomBytes.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (randomBytes[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (randomBytes[i] & 0xff);

        return toString(msb, lsb);
    }


    private static String toString(long mostSigBits, long leastSigBits) {
        return (digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4) + digits(mostSigBits, 4)
                + digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12));
    }


    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }


    public static void main(String[] args) {
        // long start = System.currentTimeMillis();
        // for (int i = 0; i < 10000; i++) {
        // System.out.println(randomUUID());
        // // System.out.println(UUID.randomUUID());
        // }
        // System.out.println((System.currentTimeMillis() - start));
    }
}
