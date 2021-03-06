package mobigrid.dashboard.state;

import mobigrid.common.NodeStateEnum;
import mobigrid.common.MobileNodeDescription;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MobileNodeTableModel extends AbstractTableModel {
    private List<MobileNodeDescription> Nodes;

    public MobileNodeTableModel() {
        this.Nodes = new ArrayList<>();
    }

    public MobileNodeTableModel(List<MobileNodeDescription> nodes) {
        this.Nodes = nodes;
    }

    public int getRowCount() {
        return Nodes.size();
    }

    public int getColumnCount() {
        return 8;
    }

    public String getColumnName(int column) {
        String name = "";
        switch (column) {
            case 0:
                name = "Id";
                break;
            case 1:
                name = "State";
                break;
            case 2:
                name = "Running Process";
                break;
            case 3:
                name = "Remain Battery Level (%)";
                break;
            case 4:
                name = "Total RAM (KB)";
                break;
            case 5:
                name = "Available RAM (KB)";
                break;
            case 6:
                name = "Total Disk (MB)";
                break;
            case 7:
                name = "Available Disk (MB)";
                break;
        }
        return name;
    }

    public Class<?> getColumnClass(int index) {
        Class type = Float.class;
        switch (index) {
            case 0:
                type = Integer.class;
                break;
            case 1:
                type = NodeStateEnum.class;
                break;
            case 2:
                type = String.class;
                break;
        }
        return type;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        MobileNodeDescription node = Nodes.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = node.getId();
                break;
            case 1:
                value = node.getState();
                break;
            case 2:
                value = node.getRunningProcess();
                break;
            case 3:
                value = node.getBatteryLevel();
                break;
            case 4:
                value = node.getTotalRam();
                break;
            case 5:
                value = node.getAvailableRam();
                break;
            case 6:
                value = node.getTotalDisk();
                break;
            case 7:
                value = node.getAvailableDisk();
                break;
        }
        return value;
    }

    public void setNodeList(List<MobileNodeDescription> nodes) {
        Nodes = nodes;
    }

    public void updateNodeStatus(MobileNodeDescription node) {
        boolean found = false;
        if(Nodes.size() > 0) {
            for (MobileNodeDescription n : Nodes) {
                if (n.getId() == node.getId()) {
                    n = node;
                    found = true;
                }
            }
        }
        if(Nodes.size() == 0 || !found)
            Nodes.add(node);
    }
}
