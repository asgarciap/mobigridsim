package mobigrid.dashboard.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.state.GridJobTableModel;
import mobigrid.dashboard.state.MobileNodeTableModel;

import javax.swing.*;
import java.util.List;

public class SimulationDashboard extends StateBESA {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTable nodesTable;
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnReload;
    private JTable jobsTable;

    private MobileNodeTableModel NodesModel;
    private GridJobTableModel JobsModel;

    public SimulationDashboard() {
        NodesModel = new MobileNodeTableModel();
        JobsModel = new GridJobTableModel();
    }
    public void run() {

        JFrame frame = new JFrame("MobiGridSim");
        frame.setContentPane(this.mainPanel);
        this.nodesTable.setModel(NodesModel);
        this.jobsTable.setModel(JobsModel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateAllMobileNodes(List<MobileNodeDescription> nodes) {
        NodesModel.setNodeList(nodes);
        NodesModel.fireTableDataChanged();
    }

    public void updateAllJobs(List<JobDescription> jobs) {
        JobsModel.setJobList(jobs);
        JobsModel.fireTableDataChanged();
    }

    public void updateJob(JobDescription job) {
        JobsModel.updateJobStatus(job);
        JobsModel.fireTableDataChanged();
    }

    public void updateMobileNode(MobileNodeDescription node) {
        NodesModel.updateNodeStatus(node);
        NodesModel.fireTableDataChanged();
    }
}
