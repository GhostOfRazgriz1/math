package struct.table.impl;

import struct.table.Table;
import struct.table.TableOD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Table2<T> implements Table<T> {
    public static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        Node<T> top;
        Node<T> bot;

        public Node() {
            left = null;
            right = null;
            top = null;
            bot = null;
        }

        public void setValue(T val) {
            this.value = val;
        }

        public T value() {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private class Table2Iterator implements Iterator<Node<T>> {
        Node<T> horizontal, vertical;
        public Table2Iterator(Node<T> node) {
            horizontal = node;
            vertical = node;
        }
        @Override
        public boolean hasNext() {
            if (horizontal.right != null) {
                return true;
            } else if (vertical.bot != null) {
                return true;
            } else if (horizontal.left != null && vertical.top != null) {
                return true;
            } else {
                return false;
            }
        }
        @Override
        public Node<T> next() {
            Node<T> value = horizontal;
            if (horizontal.right != null) {
                horizontal = horizontal.right;
            } else if (vertical.bot != null) {
                horizontal = vertical.bot;
                vertical = vertical.bot;
            } else {
                horizontal = new Node<>();
                vertical = horizontal;
            }
            return value;
        }
    }

    /**
     * Representation of this.
     */
    private Node<T> rep;
    /**
     * Number of rows.
     */
    private long rows;
    /**
     * Number of columns.
     */
    private long cols;
    /**
     * Size of the table.
     */
    private long size;

    /**
     * Creates a full row of {@code Node} nodes with size {@code long} cols.
     *
     * @param cols number of cols to be created
     * @return the first node
     */
    private Node<T> createFullRow(long cols) {
        Node<T> node = new Node<>();
        for (int i = 0; i < cols - 1; i++) {
            Node<T> right = new Node<>();
            node.right = right;
            right.left = node;
            node = node.right;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Creates a full column of {@code Node} nodes with size {@code long} rows
     *
     * @param rows number of rows
     * @return the first node
     */
    private Node<T> createFullCol(long rows) {
        Node<T> node = new Node<>();
        for (int i = 0; i < rows - 1; i++) {
            Node<T> bot = new Node<>();
            node.bot = bot;
            bot.top = node;
            node = node.bot;
        }
        while (node.top != null) {
            node = node.top;
        }
        return node;
    }

    /**
     * Merges {@code Node<T>} top to {@code Node<T>} bot
     * @param top
     *          the top row
     * @param bot
     *          the bottom row
     */
    private void mergeRows(Node<T> top, Node<T> bot) {
        Node<T> temp = top;
        for (int i = 0; i < this.cols; i++) {
            temp.bot = bot;
            bot.top = temp;
            if (temp.right != null) {
                temp = temp.right;
                bot = bot.right;
            }
        }
    }

    /**
     * Merges {@code Node<T>} left to {@code Node<T>} right
     * @param left
     *          the top row
     * @param right
     *          the bottom row
     */
    private void mergeCols(Node<T> left, Node<T> right) {
        Node<T> temp = left;
        for (int i = 0; i < this.rows; i++) {
            temp.right = right;
            right.left = temp;
            if (temp.bot != null) {
                temp = temp.bot;
                right = right.bot;
            }
        }
    }

    /**
     * Returns the {@code Node<T>} at row index {@code long} r and column index {@code long} c.
     * @param r
     *          row index of the node
     * @param c
     *          column index of the node
     * @return
     *          the node at this.rep(r, c)
     */
    private Node<T> getNodeAt(long r, long c) {
        Node<T> temp = this.rep;
        for (int i = 0; i < r; i++) {
            temp = temp.bot;
        }
        for (int i = 0; i < c; i++) {
            temp = temp.right;
        }
        return temp;
    }

    /**
     * Creates new object.
     * @param r
     *          number of rows
     * @param c
     *          number of columns
     */
    private void createNewRep(long r, long c) {
        this.rep = this.createFullCol(r);
        Node<T> temp = this.rep;
        for (int i = 0; i < c - 1; i++) {
            Node<T> next = this.createFullCol(r);
            this.mergeCols(temp, next);
            temp = temp.right;
        }
    }

    public Table2(long rows, long cols) {
        this.rows = rows;
        this.cols = cols;
        this.size = rows * cols;
        this.createNewRep(rows, cols);
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new Table2Iterator(this.rep);
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
    public long size() {
        return this.size;
    }

    @Override
    public T entry(long r, long c) {
        return getNodeAt(r, c).value;
    }

    public void setEntry(long r, long c, T value) {
        Node<T> node = getNodeAt(r, c);
        node.setValue(value);
    }

    @Override
    public void addRow() {
        Node<T> temp = this.rep;
        Node<T> row = this.createFullRow(this.cols);
        while(temp.bot != null) {
            temp = temp.bot;
        }
        this.mergeRows(temp, row);
        this.rows++;
        this.size = this.rows * this.cols;
    }

    @Override
    public void addCol() {
        Node<T> temp = this.rep;
        Node<T> col = this.createFullCol(this.rows);
        while(temp.right != null) {
            temp = temp.right;
        }
        this.mergeCols(temp, col);
        this.cols++;
        this.size = this.rows * this.cols;
    }

    @Override
    public List<Node<T>> getFullRow(long r) {
        Node<T> temp = this.rep;
        List<Node<T>> list = new ArrayList<>();

        for (int i = 0; i < r; i++) {
            temp = temp.bot;
        }
        while (temp != null) {
            list.add(temp);
            temp = temp.right;
        }
        return list;
    }

    public List<Table2.Node<T>> getFullCol(long c) {
        Node<T> temp = this.rep;
        List<Node<T>> list = new ArrayList<>();

        for (int i = 0; i < c; i++) {
            temp = temp.right;
        }
        while (temp != null) {
            list.add(temp);
            temp = temp.bot;
        }
        return list;
    }

    @Override
    public void swapValues(long r1, long c1, long r2, long c2) {
        Node<T> n1 = getNodeAt(r1, c1);
        Node<T> n2 = getNodeAt(r2, c2);

        Node<T> temp = new Node<>();

        temp.value = n1.value;
        n1.value = n2.value;
        n2.value = temp.value;
    }

    @Override
    public void rightJoin(Table<T> table) {
        this.cols += table.getCols();
        Table2<T> t = (Table2<T>) table;
        Node<T> temp = this.rep;

        while (temp.right != null) {
            temp = temp.right;
        }
        this.mergeCols(temp, t.rep);
    }
    @Override
    public Table<T> subTable(long startRow, long startCol, long rowLength, long colLength) {
        Table2<T> temp = new Table2<>(rowLength, colLength);
        for (long i = startRow; i < startRow + rowLength; i++) {
            for (long j = startCol; j < startCol + colLength; j++) {
                temp.setEntry(i - startRow, j - startCol, this.getNodeAt(i, j).value);
            }
        }
        return temp;
    }

    @Override
    public void bottomJoin(Table<T> table) {
        this.rows += table.getRows();
        Table2<T> t = (Table2<T>) table;
        Node<T> temp = this.rep;

        while (temp.bot != null) {
            temp = temp.bot;
        }
        this.mergeRows(temp, t.rep);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table2<?> t = (Table2<?>) o;
        if (this.rows != t.getRows() || this.cols != t.getCols()) return false;
        boolean equals = true;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (!this.entry(i, j).equals(t.entry(i, j))) equals = false;
            }
        }
        return equals;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> temp = this.rep;
        Node<T> temp2 = this.rep;

        for (int i = 0; i < this.rows; i++) {
            sb.append("[");
            for (int j = 0; j < this.cols; j++) {
                if (temp.right != null) {
                    sb.append(temp.value).append(" ");
                    temp = temp.right;
                } else {
                    sb.append(temp.value);
                }
            }
            sb.append("]\n");
            temp2 = temp2.bot;
            temp = temp2;
        }

        return sb.toString();

    }
}
