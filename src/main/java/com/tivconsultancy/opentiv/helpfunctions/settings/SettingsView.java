/* 
 * Copyright 2020 TIVConsultancy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tivconsultancy.opentiv.helpfunctions.settings;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class SettingsView extends JPanel {

    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    List<Settings> oSettings;
    public ClusterView oClusterView = new ClusterView();

    public SettingsView(List<Settings> oSettings) {
        super(new GridLayout(1, 0));
        this.oSettings = oSettings;

        rootNode = new DefaultMutableTreeNode("Settings");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new DefaultTreeModelListener());

        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new SettingsViewListener(tree, oClusterView));

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
        initTree();
    }

    public void initTree() {

        List<DefaultMutableTreeNode> SettingNodes = new ArrayList<>();

        for (Settings oSet : oSettings) {
            DefaultMutableTreeNode Settings1 = addObject(null, oSet.getType());

            for (SettingsCluster oCluster : oSet.getClusters()) {
                addObject(Settings1, oCluster);
            }
        }
    }

    public DefaultMutableTreeNode addToRoot(Object child) {
        DefaultMutableTreeNode parentNode = rootNode;
        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode
                = new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent,
                                 parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public class SettingsViewListener implements TreeSelectionListener {

        protected JTree tree;
        public ClusterView oClusterView;

        public SettingsViewListener(JTree tree, ClusterView oClusterView) {
            this.tree = tree;
            this.oClusterView = oClusterView;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            /* if nothing is selected */
            if (node == null) {
                return;
            }

            /* retrieve the node that was selected */
            Object nodeInfo = node.getUserObject();
            System.out.println(nodeInfo);
            if(nodeInfo instanceof SettingsCluster){
                oClusterView.setText((SettingsCluster) nodeInfo);
            }            
        }
    }

    public class ClusterView extends JPanel {

//        JLabel text = new JLabel("Start");
        JTable table;

        public ClusterView() {

            String[] columnNames = {"Name", "Value"};

            DefaultTableModel dtm = new DefaultTableModel(new Object[1][2], columnNames);
            List<SettingObject> ls = new ArrayList();
            ls.add(new SettingObject("test", "test", SettingObject.SettingsType.String));
            SettingsTableModel stm = new SettingsTableModel(ls);
            table = new JTable(stm);
//            this.add(text);
            this.add(table);
        }

        public void setText(SettingsCluster oCluster) {
            clearTabel();
            SettingsTableModel stm = (SettingsTableModel) table.getModel();
//            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
            for (SettingObject o : oCluster.lsSettings) {
                stm.addRow(o);
//                dtm.addRow(new Object[]{o.sName, o.sValue, o.sValue.getClass()});
            }
        }

        public void clearTabel() {
            SettingsTableModel stm = (SettingsTableModel) table.getModel();
            stm.clearTable();
//            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
//            if (dtm.getRowCount() > 0) {
//                for (int i = dtm.getRowCount() - 1; i > -1; i--) {
//                    dtm.removeRow(i);
//                }
//            }            
        }

    }

    public class SettingsTableModel extends AbstractTableModel {

        private List<SettingObject> SettingsList;

        private final String[] columnNames = new String[]{"Name", "Value"};

        private final Class[] columnClass = new Class[]{String.class, Object.class};

        public SettingsTableModel(List<SettingObject> SettingsList) {
            this.SettingsList = SettingsList;
        }

        public void clearTable() {
            int iSize = SettingsList.size();
            SettingsList.clear();
            fireTableRowsDeleted(0, iSize);
        }

        public void addRow(SettingObject rowData) {
            SettingsList.add(rowData);
            fireTableRowsInserted(0, SettingsList.size());
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];            
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClass[columnIndex];
        }

        @Override
        public int getRowCount() {
            return SettingsList.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            SettingObject row = SettingsList.get(rowIndex);
            if (0 == columnIndex) {
                return row.sName;
            } else if (1 == columnIndex) {
                return row.sValue;
            }
            fireTableRowsInserted(0, SettingsList.size());
            return null;            
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex == 0){
                return false;
            }
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            SettingObject row = SettingsList.get(rowIndex);
            if (0 == columnIndex) {
                row.sName = aValue.toString();
            } else if (1 == columnIndex) {
                row.sValue = aValue;
            }
        }

    }

}

class DefaultTreeModelListener implements TreeModelListener {

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
//            DefaultMutableTreeNode node;
//            node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
//
//            /*
//             * If the event lists children, then the changed
//             * node is the child of the node we've already
//             * gotten.  Otherwise, the changed node and the
//             * specified node are the same.
//             */
////            int index = e.getChildIndices()[0];
////            node = (DefaultMutableTreeNode) (node.getChildAt(index));

    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
    }
}
