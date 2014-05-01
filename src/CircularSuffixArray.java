public class CircularSuffixArray {

    private static final int CUTOFF = 15;
    private int[] index;
    private int n;

    public CircularSuffixArray(String s) {
        n = s.length();
        index = new int[n];
        for (int i = 0; i < s.length(); i++) {
            index[i] = i;
        }

        sort(s, 0, n - 1, 0);

    }

    public int length() {
        return n;
    }

    /**
     * returns index of ith sorted suffix
     * 
     * @param i
     *            the index of the ith sorted suffix
     * @return
     */
    public int index(int i) {
        return index[i];
    }

    // Return the (offset)th character of the suffix beginning in s at index
    // suffix.
    private char charAt(String s, int suffix, int offset) {
        return s.charAt((suffix + offset) % n);
    }

    // 3-way String Quicksort circular suffixes of string s from lo to hi
    // starting at index offset. Code adapted from
    // http://algs4.cs.princeton.edu/51radix/Quick3string.java.html
    private void sort(String s, int lo, int hi, int offset) {
        if (hi - lo <= CUTOFF) {
            insertion(s, lo, hi, offset);
            return;
        }
        int lt = lo, gt = hi, piv = charAt(s, index[lo], offset), eq = lo + 1;
        while (eq <= gt) {
            int t = charAt(s, index[eq], offset);
            if (t < piv)
                exch(lt++, eq++);
            else if (t > piv)
                exch(eq, gt--);
            else
                eq++;
        }
        sort(s, lo, lt - 1, offset);
        if (piv >= 0)
            sort(s, lt, gt, offset + 1);
        sort(s, gt + 1, hi, offset);
    }

    private void exch(int i, int j) {
        int tmp = index[i];
        index[i] = index[j];
        index[j] = tmp;
    }

    // Insertion sort starting at index offset. Code adapted from
    // http://algs4.cs.princeton.edu/51radix/Quick3string.java.html
    private void insertion(String s, int lo, int hi, int offset) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(s, j, j - 1, offset); j--)
                exch(j, j - 1);
    }

    // Is suffix i less than suffix j, starting at offset
    private boolean less(String s, int i, int j, int offset) {
        int oi = index[i], oj = index[j];
        for (; offset < n; offset++) {
            int ival = charAt(s, oi, offset), jval = charAt(s, oj, offset);
            if (ival < jval)
                return true;
            else if (ival > jval)
                return false;
        }
        return false;
    }

    public static void main(String[] args) {
        int SCREEN_WIDTH = 80;
        String s = StdIn.readString();
        int n = s.length();
        int digits = (int) Math.log10(n) + 1;
        String fmt = "%" + (digits == 0 ? 1 : digits) + "d ";
        StdOut.printf("String length: %d\n", n);
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < n; i++) {
            StdOut.printf(fmt, i);
            for (int j = 0; j < (SCREEN_WIDTH - digits - 1) && j < n; j++) {
                char c = s.charAt((j + csa.index(i)) % n);
                if (c == '\n')
                    c = ' ';
                StdOut.print(c);
            }
            StdOut.printf(fmt, csa.index(i));
            StdOut.println();
        }
    }
}
