package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;

public class AssignedJob extends DataBESA {

    private int NodeId;
    private JobDescription JobDescription;

    public AssignedJob(int nodeId, JobDescription job) {
        NodeId = nodeId;
        JobDescription = job;
    }

    public int getNodeId() {
        return NodeId;
    }

    public mobigrid.common.JobDescription getJobDescription() {
        return JobDescription;
    }
}
