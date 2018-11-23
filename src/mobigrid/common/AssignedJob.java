package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;

public class AssignedJob extends DataBESA {

    private int NodeId;
    private JobDescription JobDescription;
    //number of units of time the job had been assigned
    private int TotalAvailableTime;
    private int ConsumedTime;

    public AssignedJob(int nodeId, JobDescription job) {
        NodeId = nodeId;
        JobDescription = job;
        //job will have computational time + 50% of time to finish
        TotalAvailableTime = (int) (job.getComputationalTime()*1.5);
   }

    public int getNodeId() {
        return NodeId;
    }

    public void advanceSimulation() {
        ConsumedTime++;
    }

    public boolean isTimedOut() {
        return (ConsumedTime > TotalAvailableTime);
    }

    public mobigrid.common.JobDescription getJobDescription() {
        return JobDescription;
    }
}
