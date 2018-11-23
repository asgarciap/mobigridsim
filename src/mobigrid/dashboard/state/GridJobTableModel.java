package mobigrid.dashboard.state;

import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GridJobTableModel extends AbstractTableModel {

    private List<JobDescription> Jobs;

    public GridJobTableModel() {
        this.Jobs = new ArrayList<>();
    }

    public GridJobTableModel(List<JobDescription> jobs) {
        this.Jobs = jobs;
    }

    public int getRowCount() {
        return Jobs.size();
    }

    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int column) {
        String name = "";
        switch (column) {
            case 0:
                name = "Name";
                break;
            case 1:
                name = "State";
                break;
            case 2:
                name = "Required RAM (KB)";
                break;
            case 3:
                name = "Required Disk Space (MB)";
                break;
            case 4:
                name = "Computational Time (secs)";
                break;
            case 5:
                name = "Work Complete (%)";
                break;
        }
        return name;
    }

    public Class<?> getColumnClass(int index) {
        Class type = Float.class;
        switch (index) {
            case 0:
                type = String.class;
                break;
            case 1:
                type = GridJobStateEnum.class;
                break;
        }
        return type;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        JobDescription job = Jobs.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = job.getName();
                break;
            case 1:
                value = job.getState();
                break;
            case 2:
                value = job.getRequiredRam();
                break;
            case 3:
                value = job.getInputFilesTotalSize();
                break;
            case 4:
                value = job.getComputationalTime();
                break;
            case 5:
                value = job.getWorkComplete();
                break;
        }
        return value;
    }

    public void setJobList(List<JobDescription> jobs) {
        Jobs = jobs;
    }

    public void updateJobStatus(JobDescription job) {
        boolean found = false;
        for (JobDescription j : Jobs) {
            if(j.getName().equals(job.getName())) {
                j = job;
                found = true;
            }
        }
        if(!found)
            Jobs.add(job);
    }
}
