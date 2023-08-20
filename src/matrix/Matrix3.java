package matrix;

import com.sun.istack.internal.NotNull;
import struct.table.Table;
import struct.table.impl.Table2;
import utils.DoubleUtil;

import java.util.List;

public class Matrix3 extends MatrixSecondary {
    private long rows;
    private long cols;
    private Table<Double> matrix;

    private void createNewRep(long rows, long cols) {
        this.matrix = new Table2<>(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.matrix.setEntry(i, j, 0.0);
            }
        }
    }

    private long firstNonZeroRowOfColExcludingFirstNRows(long col, long n) {
        long index = n;
        if (col < this.cols) {
            List<Table2.Node<Double>> fullCol = this.matrix.getFullCol(col);
            while (index < this.rows && Double.compare(fullCol.get((int) index).value(), 0.0) == 0) {
                index++;
            }
            if (index == this.rows) {
                index = -1;
            }
        } else {
            index = -1;
        }
        return index;
    }

    private boolean isColZero(long c) {
        for (int i = 0; i < this.rows; i++) {
            if (Double.compare(0.0, this.matrix.entry(i, c)) != 0) {
                return false;
            }
        }
        return true;
    }

    private long firstNonZeroCol() {
        for (int i = 0; i < this.cols; i++) {
            if (!isColZero(i)) {
                return i;
            }
        }
        return -1;
    }

    private void swapRow(long r1, long r2) {
        if (r1 != r2) {
            for (int i = 0; i < this.cols; i++) {
                this.matrix.swapValues(r1, i, r2, i);
            }
        }
    }

    private void rowSubtraction(long r1, long r2) {
        List<Table2.Node<Double>> row1 = this.matrix.getFullRow(r1);
        List<Table2.Node<Double>> row2 = this.matrix.getFullRow(r2);
        for (int i = 0; i < row1.size(); i++) {
            Double v1 = row1.get(i).value();
            Double v2 = row2.get(i).value();
            row1.get(i).setValue(v1 - v2);
        }
    }

    private void rowMultiplication(long r, double k) {
        List<Table2.Node<Double>> row = this.matrix.getFullRow(r);
        for (int i = 0; i < row.size(); i++) {
            Double value = row.get(i).value();
            row.get(i).setValue(value * k);
        }
    }

    private void rowDivision(long r, double k) {
        List<Table2.Node<Double>> row = this.matrix.getFullRow(r);
        for (int i = 0; i < row.size(); i++) {
            Double value = row.get(i).value();
            row.get(i).setValue(value / k);
        }
    }

    private boolean isZeroP() {
        for (Table2.Node<Double> node : this.matrix) {
            if (Double.compare(0.0, node.value()) != 0) {
                return false;
            }
        }
        return false;
    }

    private void pRef() {
        if (!isZeroP()) {
            long colIndex = firstNonZeroCol();
            for (int i = 0; i < this.rows && (i + colIndex < this.cols); i++) {
                long nZeroEntry = firstNonZeroRowOfColExcludingFirstNRows(i + colIndex, i);
                if (nZeroEntry != -1) {
                    this.swapRow(i, nZeroEntry);
                }
                double fixedEntry = this.matrix.entry(i, colIndex + i);
                for (int j = i + 1; j < this.rows; j++) {
                    double leadingVal = this.matrix.entry(j, colIndex + i);
                    if (Double.compare(leadingVal, 0.0) != 0) {
                        /*
                         * Normalizes fixed row
                         */
                        this.rowDivision(i, fixedEntry);
                        /*
                         * Multiplies fixed ro so that it has the same coefficient as the other rows
                         */
                        this.rowMultiplication(i, leadingVal);
                        this.rowSubtraction(j, i);
                        /*
                         * Restores fixed row
                         */
                        this.rowDivision(i, leadingVal);
                        this.rowMultiplication(i, fixedEntry);
                    }
                }
            }
        }
    }

    private Matrix pSubMatrix(long startRow, long startCol, long rowLength, long colLength) {
        Matrix3 temp = new Matrix3(rowLength, colLength);
        temp.matrix = this.matrix.subTable(startRow, startCol, rowLength, colLength);
        return temp;
    }

    public Matrix3(long rows, long cols) {
        this.rows = rows;
        this.cols = cols;
        this.createNewRep(rows, cols);
    }

    public Matrix3(@NotNull Matrix matrix) {
        this.rows = matrix.getRows();
        this.cols = matrix.getCols();
        this.createNewRep(rows, cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix.setEntry(i, j, matrix.entryAt(i, j));
            }
        }
    }

    @Override
    public long getRows() {
        return this.rows;
    }

    @Override
    public long getCols() {
        return this.cols;
    }

    @Override
    public double entryAt(int row, int col) {
        return matrix.entry(row, col);
    }

    public double entryAt(long row, long col) {
        return matrix.entry(row, col);
    }

    @Override
    public void setEntry(int row, int col, double val) {
        matrix.setEntry(row, col, val);
    }

    public void setEntry(long row, long col, double val) {
        matrix.setEntry(row, col, val);
    }

    @Override
    public void setToIdentity() {
        assert this.rows == this.cols : "Error setting to identity: the matrix is not square!";

        for (int i = 0; i < this.rows; i++) {
            this.matrix.setEntry(i, i, 1.0);
        }
    }

    @Override
    public void add(Matrix operand) {
        assert operand.getRows() == this.rows : "Error adding matrix: number of rows do not match.";
        assert operand.getCols() == this.cols : "Error adding matrix: number of columns do not match.";

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                double op1 = this.matrix.entry(i, j);
                this.matrix.setEntry(i, j, op1 + operand.entryAt(i, j));
            }
        }
    }

    @Override
    public void subtract(Matrix operand) {
        assert operand.getRows() == this.rows : "Error adding matrix: number of rows do not match.";
        assert operand.getCols() == this.cols : "Error adding matrix: number of columns do not match.";

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                double op1 = this.matrix.entry(i, j);
                this.matrix.setEntry(i, j, op1 - operand.entryAt(i, j));
            }
        }
    }

    @Override
    public void multiply(Matrix operand) {
        assert this.cols == operand.getRows() : "Error multiplying matrix: operand has incorrect dimension.";

        Matrix3 result = new Matrix3(this.rows, operand.getCols());

        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                for (int k = 0; k < operand.getRows(); k++) {
                    double entry = result.matrix.entry(i, j);
                    result.matrix.setEntry(i, j, entry + this.matrix.entry(i, k) * operand.entryAt(k, j));
                }
            }
        }

        this.matrix = result.matrix;
        this.rows = result.rows;
        this.cols = result.cols;
    }

    @Override
    public boolean isZero() {
        return isZeroP();
    }

    @Override
    public void ref() {
        this.pRef();
    }

    @Override
    public double determinant() {
        assert this.rows == this.cols : "Unable to calculate determinant: the matrix is not square!";

        Matrix3 temp = new Matrix3(this);
        temp.ref();
        double det = 1.0;
        for (int i = 0; i < temp.rows; i++) {
            det *= temp.matrix.entry(i, i);
        }
        return det;
    }

    @Override
    public void transpose() {
        Table<Double> t2 = new Table2<>(this.cols, this.rows);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                t2.setEntry(j, i, this.matrix.entry(i, j));
            }
        }
        this.rows = t2.getRows();
        this.cols = t2.getCols();
        this.matrix = t2;
    }

    @Override
    public void augment(Matrix matrix) {
        Matrix3 temp = new Matrix3(matrix);
        this.matrix.rightJoin(temp.matrix);
        this.cols += temp.cols;
    }

    @Override
    public void rref() {
        this.pRef();
        if (!isZeroP()) {
            for (int i = 0; i < this.rows && i < this.cols; i++) {
                double pivot = this.matrix.entry(i, i);
                if (Double.compare(pivot, 0.0) != 0) {
                    this.rowDivision(i, pivot);
                    for (int j = 0; j < this.rows; j++) {
                        if (i != j) {
                            double current = matrix.entry(j, i);
                            if (Double.compare(current, 0.0) != 0) {
                                this.rowMultiplication(i, current);
                                this.rowSubtraction(j, i);
                                this.rowDivision(i, current);
                            }
                        }
                    }
                }
            }
            long nonZeroCol = firstNonZeroCol();
            if (nonZeroCol > 0) {
                Matrix3 left = (Matrix3) this.pSubMatrix(0, 0, this.rows, nonZeroCol);
                Matrix3 right = (Matrix3) this.pSubMatrix(0, nonZeroCol, this.rows, this.cols - nonZeroCol);
                right.rref();
                left.augment(right);
                this.matrix = left.matrix;
            }
        }
    }

    @Override
    public Matrix inverse() {
        assert Double.compare(this.determinant(), 0.0) != 0: "Error calculating inverse: determinant is zero!";

        Matrix3 temp = new Matrix3(this);
        Matrix ag = new Matrix3(this.rows, this.cols);
        ag.setToIdentity();
        temp.augment(ag);
        temp.rref();

        return temp.pSubMatrix(0, ag.getCols(), this.rows, ag.getCols());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            sb.append("[");
            for (int j = 0; j < this.cols; j++) {
                double entry = this.matrix.entry(i, j);
                if (entry < 0) {
                    entry = DoubleUtil.round(entry, 5);
                }else {
                    entry = DoubleUtil.round(entry, 6);
                }

                if (j == this.cols - 1) {
                    sb.append(entry);
                } else {
                    sb.append(entry).append(" ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
