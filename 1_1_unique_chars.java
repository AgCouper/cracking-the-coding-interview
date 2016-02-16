/*
 * Implement an algorithm to determine if a string has all unique characters.
 */

/*
 * Things to remember:
 *    >> shift is signed, use unsigned >>>
 *    Math.random() _will_ return 0 sometime, so if 
 *    random length of array is needed, always add 1.
 */
class Alg {
    public static boolean testCharUniqueness(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }

        /*
         * There are 1 << 16 characters in total.
         * Each int holds 1 << 5 bits. Therefore,
         * we need 1 << (16-5) ints to hold all the bits.
         */
        int flag[] = new int[(1 << 11)];
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            int chVal = (int)ch;
            int idx = chVal >>> 5;
            int off = chVal - (idx << 5);

            if ((flag[idx] & (1 << off)) != 0) {
                return true;
            } else {
                flag[idx] |= (1 << off);
            }
        }

        return false;
    }
}

class Main {
    public static boolean randomTest(int totalIters) {
        for (int i = 0; i < totalIters; ++i) {
            boolean hasDuplicate = Math.random() < 0.5;
            String s = generateRandomString(hasDuplicate);
            boolean res = Alg.testCharUniqueness(s);
            if (res != hasDuplicate) {
                System.err.println("FAIL. Expected: " + hasDuplicate);
                return false;
            }
        }

        System.err.println("PASS");
        return true;
    }

    public static String generateRandomString(boolean hasDuplicate) {
        int length = (int)(Math.random() * 4094) + 1;
        boolean flag[] = new boolean[65535];

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int charCode;
            do {
                charCode = (int)(Math.random() * 65535);
            } while (!hasDuplicate && flag[charCode]);

            flag[charCode] = true;

            b.append((char)charCode);
        }

        if (hasDuplicate) {
            char ch = b.charAt(0);
            b.append(ch);
        }

        return b.toString();
    }

    public static void main(String[] args) {
        boolean passed = randomTest(10000);
        if (!passed) {
            System.exit(1);
        }
    }
}

