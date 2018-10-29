package mobigrid.dashboard.state;

import mobigrid.common.GridJobData;
import mobigrid.common.GridJobStateEnum;

public class GridJob {
    private int JobId;
    private GridJobStateEnum State;
    private GridJobData DataIn;
    private GridJobData DataOut;
    private float RequiredRam;
    private float RequiredDiskSpace;
    private float TotalComputationTime; //seconds

    public GridJob(int jobId, float requiredRam, float requiredDiskSpace, float totalComputationTime) {
        JobId = jobId;
        RequiredRam = requiredRam;
        RequiredDiskSpace = requiredDiskSpace;
        TotalComputationTime = totalComputationTime;
    }

    public void setDataIn(GridJobData dataIn) {
        DataIn = dataIn;
    }

    public void setDataOut(GridJobData dataOut) {
        DataOut = dataOut;
    }

    public void setState(GridJobStateEnum state) {
        State = state;
    }

    public int getJobId() {
        return JobId;
    }

    public GridJobStateEnum getState() {
        return State;
    }

    public float getRequiredDiskSpace() {
        return RequiredDiskSpace;
    }

    public float getRequiredRam() {
        return RequiredRam;
    }

    public float getTotalComputationTime() {
        return TotalComputationTime;
    }

    public GridJobData getDataIn() {
        return DataIn;
    }

    public GridJobData getDataOut() {
        return DataOut;
    }
}
