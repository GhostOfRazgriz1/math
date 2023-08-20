package matrix;

public abstract class MatrixSecondary implements Matrix{
    public static Matrix identity(int r) {
        Matrix matrix = new Matrix3(r, r);
        for (int i = 0; i < r; i++) {
            matrix.setEntry(i, i, 1.0);
        }
        return matrix;
    }

    public static Matrix random(int rows, int cols, double lBound, double uBound) {
        Matrix matrix = new Matrix3(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.setEntry(i, j, Math.random() * (uBound - lBound) + lBound);
            }
        }
        return matrix;
    }
}
