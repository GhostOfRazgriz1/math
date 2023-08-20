package matrix;

/**
 *
 */
public interface Matrix {
    /**
     * Returns the number of rows of the matrix.
     * @return this.rows
     */
    long getRows();
    long getCols();
    double entryAt(int row, int col);
    void setEntry(int row, int col, double val);
    void setToIdentity();
    void add(Matrix operand);
    void subtract(Matrix operand);
    void multiply(Matrix operand);
    boolean isZero();
    void transpose();
    void ref();
    double determinant();
    void augment(Matrix matrix);
    void rref();
    Matrix inverse();
}
