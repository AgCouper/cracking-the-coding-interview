/*
 * Given two strings, write a method to decide
 * if one is a permutation of the other.
 */

import java.util.Arrays;

/*
 * Things to remember:
 *     [Min, Max]: Min + (int)(Math.random() * ((Max - Min) + 1))
 */
class Alg {
    public static boolean isPermutation(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        }

        if (s1.length() != s2.length()) {
            return false;
        }

        // The idea is simple: the number of the each character
        // occurrence must be the same for each string
        int cnt[] = new int[256];

        for (int i = 0; i < s1.length(); ++i) {
            int charCode = (int)s1.charAt(i);
            cnt[charCode]++;

            charCode = (int)s2.charAt(i);
            cnt[charCode]--;
        }

        for (int i = 0; i < cnt.length; ++i) {
            if (cnt[i] != 0) {
                return false;
            }
        }

        return true;
    }
}

class Main {
    private static String randomAsciiString(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            int ch = (int)(Math.random() * 255 + 1);
            sb.append((char)ch);
        }

        return sb.toString();
    }

    private static String randomStringPermutation(String s) {
        boolean isPosOccupied[] = new boolean[s.length()];

        StringBuilder sb = new StringBuilder();
        sb.setLength(isPosOccupied.length);
        for (int i = 0; i < isPosOccupied.length; ++i) {
            int newPos = (int)(Math.random() * isPosOccupied.length);
            if (isPosOccupied[newPos]) {
                for (int j = 0; j < isPosOccupied.length; ++j) {
                    if (!isPosOccupied[j]) {
                        newPos = j;
                        break;
                    }
                }
            }

            isPosOccupied[newPos] = true;
            sb.setCharAt(newPos, s.charAt(i));
        }

        return sb.toString();
    }

    private static boolean isPermutationSlow(String s1, String s2) {
        char s1Arr[] = s1.toCharArray();
        Arrays.sort(s1Arr);

        char s2Arr[] = s2.toCharArray();
        Arrays.sort(s2Arr);

        return Arrays.equals(s1Arr, s2Arr);
    }

    private static boolean randomTest(int totalIters) {
        for (int i = 0; i < totalIters; ++i) {
            int strLen = (int)(Math.random() * 4096) + 1;
            String s1 = randomAsciiString(strLen);

            boolean isPermutation = Math.random() < 0.5;
            String s2;
            if (isPermutation) {
                s2 = randomStringPermutation(s1);
            } else {
                s2 = randomAsciiString(strLen);
                // it might happened that we random generated a permutation
                isPermutation = isPermutationSlow(s1, s2);
            }

            boolean res = Alg.isPermutation(s1, s2);
            if (res != isPermutation) {
                System.err.println("FAIL. Expected: " + isPermutation);
                System.err.println("First string:\n" + Arrays.toString(s1.chars().map(ch -> (int)ch).toArray()));
                System.err.println("Second string:\n" + Arrays.toString(s2.chars().map(ch -> (int)ch).toArray()));

                return false;
            }
        }

        System.err.println("PASS");
        return true;
    }

    public static void main(String[] args) {
        boolean passed = randomTest(10000);
        if (!passed) {
            System.exit(1);
        }
    }
}

