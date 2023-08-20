package struct.table;

import struct.table.impl.Table2;

import java.util.List;

public interface Table<T> extends Iterable<Table2.Node<T>>{
    /**
     * Returns number of rows of this
     * @return this.rows
     */
    long getRows();
    /**
     * Returns number of columns of this
     * @return this.cols
     */
    long getCols();
    /**
     * Returns the size of this
     * @return this.size
     */
    long size();
    /**
     * Reports the value of {@code Node<T>} at ({@code long} r, {@code long} c)
     * @param r
     *          row index of the node
     * @param c
     *          column index of the node
     * @return
     *          this.rep(r, c).value
     */
    T entry(long r, long c);

    /**
     * Set the value of {@code Node<T>} at ({@code long} r, {@code long} c)
     * @param r
     *          row index of the node
     * @param c
     *          column index of the node
     * @param value
     *          value to be set to the node
     */
    void setEntry(long r, long c, T value);
    /**
     * Adds a row to the bottom of this.rep
     */
    void addRow();
    /**
     * Adds a column to the right end of this.rep
     */
    void addCol();

    /**
     * Returns {@code List<Node<T>>} whose row index is {@code long} r
     * @param r
     *          index of row
     * @return
     *      all nodes whose row index is r
     */
    List<Table2.Node<T>> getFullRow(long r);

    /**
     * Returns {@code List<Node<T>>} whose column index is {@code long} r
     * @param c
     *          index of column
     * @return
     *      all nodes whose column index is c
     */
    List<Table2.Node<T>> getFullCol(long c);

    /**
     * Swaps the values of {@code Node<T>} n1 and {@code Node<T>} n2
     * @param r1
     *          row index of node 1
     * @param c1
     *          column index of node 1
     * @param r2
     *          row index of node 2
     * @param c2
     *          column index of node 2
     */
    void swapValues(long r1, long c1, long r2, long c2);

    /**
     * Augments a table to the right of this.
     * @param table
     *          another table that will be joined to the right of this.rep
     */
    void rightJoin(Table<T> table);

    /**
     * Augments a table to the bottom of this.
     * @param table
     *          another table that will be joined to the bottom of this.rep
     */
    void bottomJoin(Table<T> table);

    /**
     *
     * @param startRow
     * @param startCol
     * @param rowLength
     * @param colLength
     * @return
     */
    Table<T> subTable(long startRow, long startCol, long rowLength, long colLength);
}
