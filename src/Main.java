import matrix.Matrix;
import matrix.Matrix1;
import matrix.Matrix3;
import struct.table.Table;
import struct.table.impl.Table2;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public final class Main {
    public static void main(String[] args) {
        Matrix3 m1 = new Matrix3(3, 3);
        Matrix3 m2 = new Matrix3(3, 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m1.setEntry(i, j, randi());
            }
        }

        System.out.println(m1);
        Matrix inverse = m1.inverse();
        System.out.println(inverse);

        m1.multiply(inverse);
        System.out.println(m1);
   }

    private static int randi() {
        return (int) (Math.random() * 10.0);
    }

    private static String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result = "";
        for (int i = 0; i < length; i++) {
            result += chars.charAt((int) (Math.random() * 51.0));
        }
        return result;
    }
}