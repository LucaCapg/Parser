package com.company.Controller.dialog;

import com.company.Controller.customtableheader.GroupableTableHeader;
import com.company.Controller.customtableheader.ColumnGroup;
import com.company.Model.DUT;
import com.company.Controller.GUIUtility;
import com.company.Model.Mobile;
import com.company.Utility;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class BandCombinationDialog extends JDialog {
    private JScrollPane scrollPane;
    private JTable v10i0Tabl2;
    private int purpose;
    private int startingIndex = 0;
    private String[] columnNames;

    public BandCombinationDialog(Mobile device, int purpose) {
        super();

        setTitle(titleFormatter(purpose));
        switch (purpose){
            case GUIUtility.DL_1020:
                columnNames = new String[]{"Index",
                        "E-UTRA CA Configuration", "BCS","CA Type",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers"};
                startingIndex = 4;
                break;

			case GUIUtility.DL_1310:
				columnNames = new String[]{"Index",
						"E-UTRA CA Configuration", "BCS","CA Type",
						"Band","Class","Layers",
						"Band","Class","Layers",
						"Band","Class","Layers",
						"Band","Class","Layers",
						"Band","Class","Layers"};
				startingIndex = 4;
				break;

            case GUIUtility.DL_10i0:
                columnNames = new String[]{"Index",
                        "E-UTRA CA Configuration", "BCS","CA Type",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers"};
                startingIndex = 4;
                break;

            case GUIUtility.UL_10i0:
                columnNames = new String[]{"Index",
                        "E-UTRA CA Configuration","CA Type",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers"};
                startingIndex = 3;
                break;

            case GUIUtility.UL_1020:
                columnNames = new String[]{"Index",
                        "E-UTRA CA Configuration","CA Type",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers"};
                startingIndex = 3;
                break;

			case GUIUtility.UL_1310:
				columnNames = new String[]{"Index",
						"E-UTRA CA Configuration","CA Type",
						"Band","Class","Layers",
						"Band","Class","Layers",
						"Band","Class","Layers",
						"Band","Class","Layers",
						"Band","Class","Layers"};
				startingIndex = 3;
				break;

            case GUIUtility.DL_ENNR:
                columnNames = new String[]{"Index",
                        "EN-DC Configuration",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers", "Modulation", "SCS", "BandWidth", "ChannelBW90Mhz",
                        "Band","Class","Layers", "Modulation", "SCS", "BandWidth", "ChannelBW90Mhz"};
                startingIndex = 2;
                break;

            case GUIUtility.UL_ENNR:
                columnNames = new String[]{"Index",
                        "EN-DC Configuration",
                        "Band","Class","Layers",
                        "Band","Class","Layers",
                        "Band","Class","Layers", "Modulation", "SCS", "BandWidth", "ChannelBW90Mhz"};
                startingIndex = 2;
                break;

            default: System.out.println("Error in band dialog table");
                break;
        }
        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        getContentPane().setLayout(new BorderLayout());


        Object[][] data = GUIUtility.tableDataFormatter(device,purpose);
        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(data,columnNames);

        v10i0Tabl2 = new JTable( dm ) {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        UIManager.put("Table.focusCellHighlightBorder",
                new BorderUIResource(BorderFactory.createLineBorder(Color.red)));
        v10i0Tabl2.setIntercellSpacing(new Dimension(0,0));
        TableColumnModel cm = v10i0Tabl2.getColumnModel();
        if(purpose == GUIUtility.DL_ENNR) {
	        ColumnGroup column4G_1 = new ColumnGroup("4G");
	        column4G_1.add(cm.getColumn(startingIndex));
	        column4G_1.add(cm.getColumn(startingIndex+1));
	        column4G_1.add(cm.getColumn(startingIndex+2));
	        ColumnGroup column4G_2 = new ColumnGroup("4G");
	        column4G_2.add(cm.getColumn(startingIndex+3));
	        column4G_2.add(cm.getColumn(startingIndex+4));
	        column4G_2.add(cm.getColumn(startingIndex+5));
	        ColumnGroup column4G_3 = new ColumnGroup("4G");
	        column4G_3.add(cm.getColumn(startingIndex+6));
	        column4G_3.add(cm.getColumn(startingIndex+7));
	        column4G_3.add(cm.getColumn(startingIndex+8));
	        ColumnGroup column4G_4 = new ColumnGroup("4G");
	        column4G_4.add(cm.getColumn(startingIndex+9));
	        column4G_4.add(cm.getColumn(startingIndex+10));
	        column4G_4.add(cm.getColumn(startingIndex+11));
	        ColumnGroup column4G_5 = new ColumnGroup("4G");
	        column4G_5.add(cm.getColumn(startingIndex+12));
	        column4G_5.add(cm.getColumn(startingIndex+13));
	        column4G_5.add(cm.getColumn(startingIndex+14));
	        ColumnGroup column5G_1 = new ColumnGroup("5G");
	        column5G_1.add(cm.getColumn(startingIndex+15));
	        column5G_1.add(cm.getColumn(startingIndex+16));
	        column5G_1.add(cm.getColumn(startingIndex+17));
	        column5G_1.add(cm.getColumn(startingIndex+18));
	        column5G_1.add(cm.getColumn(startingIndex+19));
	        column5G_1.add(cm.getColumn(startingIndex+20));
	        column5G_1.add(cm.getColumn(startingIndex+21));
	        ColumnGroup column5G_2 = new ColumnGroup("5G");
	        column5G_2.add(cm.getColumn(startingIndex+22));
	        column5G_2.add(cm.getColumn(startingIndex+23));
	        column5G_2.add(cm.getColumn(startingIndex+24));
	        column5G_2.add(cm.getColumn(startingIndex+25));
	        column5G_2.add(cm.getColumn(startingIndex+26));
	        column5G_2.add(cm.getColumn(startingIndex+27));
	        column5G_2.add(cm.getColumn(startingIndex+28));


	        GroupableTableHeader header = (GroupableTableHeader)v10i0Tabl2.getTableHeader();
	        header.addColumnGroup(column4G_1);
	        header.addColumnGroup(column4G_2);
	        header.addColumnGroup(column4G_3);
	        header.addColumnGroup(column4G_4);
	        header.addColumnGroup(column4G_5);
	        header.addColumnGroup(column5G_1);
	        header.addColumnGroup(column5G_2);

        } else if(purpose == GUIUtility.UL_ENNR) {
	        ColumnGroup column4G_1 = new ColumnGroup("4G");
	        column4G_1.add(cm.getColumn(startingIndex));
	        column4G_1.add(cm.getColumn(startingIndex+1));
	        column4G_1.add(cm.getColumn(startingIndex+2));
	        ColumnGroup column4G_2 = new ColumnGroup("4G");
	        column4G_2.add(cm.getColumn(startingIndex+3));
	        column4G_2.add(cm.getColumn(startingIndex+4));
	        column4G_2.add(cm.getColumn(startingIndex+5));
	        ColumnGroup column5G_1 = new ColumnGroup("5G");
	        column5G_1.add(cm.getColumn(startingIndex+6));
	        column5G_1.add(cm.getColumn(startingIndex+7));
	        column5G_1.add(cm.getColumn(startingIndex+8));
	        column5G_1.add(cm.getColumn(startingIndex+9));
	        column5G_1.add(cm.getColumn(startingIndex+10));
	        column5G_1.add(cm.getColumn(startingIndex+11));
	        column5G_1.add(cm.getColumn(startingIndex+12));


	        GroupableTableHeader header = (GroupableTableHeader)v10i0Tabl2.getTableHeader();
	        header.addColumnGroup(column4G_1);
	        header.addColumnGroup(column4G_2);
	        header.addColumnGroup(column5G_1);

        } else {
	        ColumnGroup pccColumn = new ColumnGroup("PCC");
	        pccColumn.add(cm.getColumn(startingIndex));
	        pccColumn.add(cm.getColumn(startingIndex+1));
	        pccColumn.add(cm.getColumn(startingIndex+2));
	        ColumnGroup sccColumn = new ColumnGroup("SCC1");
	        sccColumn.add(cm.getColumn(startingIndex+3));
	        sccColumn.add(cm.getColumn(startingIndex+4));
	        sccColumn.add(cm.getColumn(startingIndex+5));
	        ColumnGroup tccColumn = new ColumnGroup("SCC2");
	        tccColumn.add(cm.getColumn(startingIndex+6));
	        tccColumn.add(cm.getColumn(startingIndex+7));
	        tccColumn.add(cm.getColumn(startingIndex+8));
	        ColumnGroup fccColumn = new ColumnGroup("SCC3");
	        fccColumn.add(cm.getColumn(startingIndex+9));
	        fccColumn.add(cm.getColumn(startingIndex+10));
	        fccColumn.add(cm.getColumn(startingIndex+11));
	        ColumnGroup lccColumn = new ColumnGroup("SCC4");
	        lccColumn.add(cm.getColumn(startingIndex+12));
	        lccColumn.add(cm.getColumn(startingIndex+13));
	        lccColumn.add(cm.getColumn(startingIndex+14));
	        //sccColumn.add(tccColumn);

	        GroupableTableHeader header = (GroupableTableHeader)v10i0Tabl2.getTableHeader();
	        header.addColumnGroup(pccColumn);
	        header.addColumnGroup(sccColumn);
	        header.addColumnGroup(tccColumn);
	        header.addColumnGroup(fccColumn);
	        header.addColumnGroup(lccColumn);
        }
        v10i0Tabl2.setEnabled(false);
        scrollPane = new JScrollPane(v10i0Tabl2);
        add(scrollPane);
        pack();
        setVisible(true);
    }

    /**
     * Private support method to set title of the dialog
     * @param purpose the int describing the title
     * @return the decoded string
     */
    private String titleFormatter (int purpose){
        String title = "";
        switch (purpose) {
            case GUIUtility.DL_10i0: title = GUIUtility.BAND_COMBINATIONS_V10I0_DL_TITLE;
                break;
			case GUIUtility.DL_1310: title = GUIUtility.BAND_COMBINATIONS_V1310_DL_TITLE;
				break;
            case GUIUtility.UL_10i0: title = GUIUtility.BAND_COMBINATIONS_V10I0_UL_TITLE;
                break;
            case GUIUtility.DL_1020: title = GUIUtility.BAND_COMBINATIONS_V1020_DL_TITLE;
                break;
			case GUIUtility.UL_1310: title = GUIUtility.BAND_COMBINATIONS_V1310_UL_TITLE;
            case GUIUtility.UL_1020: title = GUIUtility.BAND_COMBINATIONS_V1020_UL_TITLE;
                break;
            default: title = Utility.MISSING_CODE;
                break;
        }
        return title;
    }
}
