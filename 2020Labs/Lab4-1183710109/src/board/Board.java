package board;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import planningEntry.*;
import planningEntryCollection.*;
import resource.*;

import java.awt.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;

public abstract class Board {
    protected final JFrame frame = new JFrame();
    protected final PlanningEntryCollection planningEntryCollection;

    public Board(PlanningEntryCollection planningEntryCollection) {
        this.planningEntryCollection = planningEntryCollection;
    }

    /**
     * visualize planning entries at current time in chosen location of the type
     * @param strCurrentTime
     * @param strLocation
     * @param intType
     */
    public abstract void visualize(String strCurrentTime, String strLocation, int intType);

    /**
     * show all entries using r
     * @param r
     */
    public abstract void showEntries(Resource r);

    /**
     * make a table
     * @param vData
     * @param vName
     * @param title
     */
    protected void makeTable(Vector<Vector<?>> vData, Vector<String> vName, String title) {
        DefaultTableModel dataModel = new DefaultTableModel(vData, vName);
        JTable table = new JTable();
        table.setModel(dataModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // table.getColumn("").setPreferredWidth(0);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);
        frame.setTitle(title);
        frame.setBounds(100, 100, 100, 100);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * iterator
     * @return iterator
     */
    public Iterator<PlanningEntry<Resource>> iterator() {
        return planningEntryCollection.getAllPlanningEntries().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;
        return Objects.equals(planningEntryCollection, board.planningEntryCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frame, planningEntryCollection);
    }

}
