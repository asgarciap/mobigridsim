package mobigrid.gridserver.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.AssignedJob;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author arturogarcia
 */
public class DispatcherState extends StateBESA {

    private int TotalNodes;
    private int LastNodeDispatched;

    public DispatcherState(int totalNodes) {
        TotalNodes = totalNodes;
        LastNodeDispatched = 0;
    }

    public AssignedJob dispatchJob(JobDescription jobDescription) {
        LastNodeDispatched++;
        jobDescription.setState(GridJobStateEnum.DISPATCHED);
        AssignedJob assignedJob = new AssignedJob(LastNodeDispatched, jobDescription);
        if(LastNodeDispatched == TotalNodes)
            LastNodeDispatched = 0;
        return assignedJob;
    }
}
