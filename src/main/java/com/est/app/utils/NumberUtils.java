/*
 * $Id: NumberUtils.java 4973 2009-02-02 07:52:47Z lsantha $
 *
 * Copyright (C) 2003-2009 JNode.org
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either ProtocolVersion 2.1 of the License, or
 * (at your option) any later ProtocolVersion.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; If not, write to the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
 
package com.est.app.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author epr
 */
public class NumberUtils {
    /**
     * Convert a float to a string with a given maximum number of fraction digits.
     *
     * @param value
     * @param maxFractionLength
     * @return The string
     */
    public static String toString(float value, int maxFractionLength) {
        String s = Float.toString(value);
        final int idx = s.indexOf('.');
        if (idx >= 0) {
            final int len = Math.min(s.length(), idx + maxFractionLength + 1);
            return s.substring(0, len);
        } else {
            return s;
        }
    }

    /**
     * Converts a byte to an unsigned value.
     */
    public static int toUnsigned(final byte b) {
        return b & 0xFF;
    }

    /**
     * Converts a short to an unsigned value.
     */
    public static int toUnsigned(final short s) {
        return s & 0xFFFF;
    }

    /**
     * Converts an int to an unsigned value.
     */
    public static long toUnsigned(final int i) {
        return i & 0xFFFFFFFFL;
    }

    /**
     * Gets the hexadecimal representation of the given number. The result is
     * prefixed with '0' until the given length is reached.
     *
     * @param number
     * @param length
     * @return String
     */
    public static String hex(int number, int length) {
        StringBuilder buf = new StringBuilder(length);
        int2HexString(buf, number);
        return prefixZero(buf, length);
    }

    /**
     * Gets the hexadecimal representation of the given number that is
     * 8 digits long.
     *
     * @param number
     * @return String
     */
    public static String hex(int number) {
        //return hex(number, 8);
        return hex(number, number > 0xFFFF ? 8 : number > 0xFF ? 4 : 2);
    }

    /**
     * Gets the hexadecimal representation of the given number. The result is
     * prefixed with '0' until the given length is reached.
     *
     * @param number
     * @param length
     * @return String
     */
    public static String hex(long number, int length) {
        StringBuilder buf = new StringBuilder(length);
        long2HexString(buf, number);
        return prefixZero(buf, length);
    }

    /**
     * Gets the hexadecimal representation of the given number that is
     * 16 digits long.
     *
     * @param number
     * @return String
     */
    public static String hex(long number) {
        return hex(number, 16);
    }

    /**
     * Convert a byte array to a string of hex-numbers
     *
     * @param data
     * @param offset
     * @param length
     * @return String
     */
    public static String hex(byte[] data, int offset, int length) {
        final StringBuilder buf = new StringBuilder(length * 3);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                if ((i % 16) == 0) {
                    buf.append('\n');
                } else {
                    buf.append(' ');
                }
            }
            buf.append(hex(data[offset + i] & 0xFF, 2));
        }
        return buf.toString();
    }

    /**
     * Convert a int array to a string of hex-numbers
     *
     * @param data
     * @param offset
     * @param length
     * @param hexLength
     * @return String
     */
    public static String hex(int[] data, int offset, int length, int hexLength) {
        final StringBuilder buf = new StringBuilder(length * (hexLength + 1));
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                if ((i % 16) == 0) {
                    buf.append('\n');
                } else {
                    buf.append(' ');
                }
            }
            buf.append(hex(data[offset + i], hexLength));
        }
        return buf.toString();
    }

    /**
     * Convert a char array to a string of hex-numbers
     *
     * @param data
     * @param offset
     * @param length
     * @return String
     */
    public static String hex(char[] data, int offset, int length) {
        final StringBuilder buf = new StringBuilder(length * 3);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                if ((i % 16) == 0) {
                    buf.append('\n');
                } else {
                    buf.append(' ');
                }
            }
            buf.append(hex(data[offset + i], 2));
        }
        return buf.toString();
    }


    /**
     * Convert a byte array to a string of hex-numbers
     *
     * @param data
     * @return String
     */
    public static String hex(byte[] data) {
        return hex(data, 0, data.length);
    }

    /**
     * Convert an int array to a string of hex-numbers
     *
     * @param data
     * @param hexLength
     * @return String
     */
    public static String hex(int[] data, int hexLength) {
        return hex(data, 0, data.length, hexLength);
    }

    private static String prefixZero(StringBuilder v, int length) {
        if (v.length() > length) {
            // truncate leading chars
            return v.substring(v.length() - length);
        } else {
            // insert leading '0's
            while (v.length() < length) {
                v.insert(0, '0');
            }
            return v.toString();
        }
    }


    /**
     * This method avoids the use on Integer.toHexString, since this class may be used
     * during the boot-phase when the Integer class in not yet initialized.
     *
     * @param buf
     * @param value
     */
    private static void int2HexString(StringBuilder buf, int value) {
        int rem = value & 0x0F;
        int q = value >>> 4;
        if (q != 0) {
            int2HexString(buf, q);
        }

        if (rem < 10) {
            buf.append((char) ('0' + rem));
        } else {
            buf.append((char) ('A' + rem - 10));
        }
    }

    /**
     * This method avoids the use on Long.toHexString, since this class may be used
     * during the boot-phase when the Long class in not yet initialized.
     *
     * @param buf
     * @param value
     */
    private static void long2HexString(StringBuilder buf, long value) {
//      long rem = value & 0x0F;
        int rem = (int) (value & 0x0FL);
        long q = value >>> 4;
        if (q != 0) {
            long2HexString(buf, q);
        }

        if (rem < 10) {
            buf.append((char) ('0' + rem));
        } else {
            buf.append((char) ('A' + rem - 10));
        }
    }
    
    // Converts a hex string to long
    public static long hexStringToInt(String toConvert)
    {
        long iToReturn = 0;
        int iExp = 0;
        char chByte;

        toConvert = toConvert.replaceAll(" ", "");
        // The string to convert is empty
        if (toConvert.equals(""))
        {
            return 0;
        }
        // The string have more than 8 character (the equivalent value
        // exeeds the DWORD capacyty
        if (toConvert.length() > 16)
        {
            return 0;
        }
        // We convert any character to its Upper case
        toConvert = toConvert.toUpperCase();
        try
        {
            // We calculate the number using the Hex To Decimal formula
            for (int i = toConvert.length() - 1; i >= 0; i--)
            {
                chByte = (char) toConvert.getBytes()[i];
                switch ((int) chByte)
                {
                    case 65:
                        iToReturn += (long) (10 * Math.pow(16.0f, iExp));
                        break;
                    case 66:
                        iToReturn += (long) (11 * Math.pow(16.0f, iExp));
                        break;
                    case 67:
                        iToReturn += (long) (12 * Math.pow(16.0f, iExp));
                        break;
                    case 68:
                        iToReturn += (long) (13 * Math.pow(16.0f, iExp));
                        break;
                    case 69:
                        iToReturn += (long) (14 * Math.pow(16.0f, iExp));
                        break;
                    case 70:
                        iToReturn += (long) (15 * Math.pow(16.0f, iExp));
                        break;
                    default:
                        if ((chByte < 48) || (chByte > 57))
                        {
                            return -1;
                        }
                        iToReturn += (long) Integer.parseInt(((Character) chByte).toString()) * Math.pow(16.0f, iExp);
                        break;
                }
                iExp++;
            }
        }
        catch (Exception ex)
        {
            // Error, return 0
            return 0;
        }
        return iToReturn;
    }
    
	public static byte[] hexStringToByteArray(String s) {
		s = s.replaceAll(" ", "");
		
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	public static List<Integer> getNumbers(int start, int stop){
        List<Integer> numbers = new ArrayList<>();
        for(int i = start; i<=stop; i++){
            numbers.add(i);
        }
        return numbers;
    }

    public static String setLength(int number, int length){
        String result = Integer.toString(number);
        while(result.length() < length){
            result = "0" + result;
        }
        return result;
    }

}
