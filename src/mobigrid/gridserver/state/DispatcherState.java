package mobigrid.gridserver.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.AssignedJob;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;

import java.util.*;

/**
 * @author arturogarcia
 */
public class DispatcherState extends StateBESA {

    private Queue<JobDescription> JobsQueue;
    private Queue<MobileNodeDescription> MobileNodes;
    private DispatcherStrategy Strategy;

    public enum DispatcherStrategy {
        ROUND_ROBIN,
        SEAS,
    };

    public DispatcherState() {
        JobsQueue = new LinkedList<>();
        MobileNodes = new LinkedList<>();
        Strategy = DispatcherStrategy.ROUND_ROBIN;
    }

    public AssignedJob dispatchNextJob() {
        if(MobileNodes.size() > 0 && JobsQueue.size() > 0) {
            //Round Robin Dispatcher
            if(Strategy == DispatcherStrategy.ROUND_ROBIN) {
                MobileNodeDescription node = MobileNodes.remove();
                JobDescription job = JobsQueue.remove();
                job.setState(GridJobStateEnum.DISPATCHED);
                AssignedJob assignedJob = new AssignedJob(node.getId(), job);

                //mode node to the last position of the queue
                MobileNodes.add(node);
                return assignedJob;
            }else if(Strategy == DispatcherStrategy.SEAS) {
                //TODO
            }
        }
        return null;
    }

    public void registerMobileNode(MobileNodeDescription mobileNodeDescription) {
        MobileNodes.add(mobileNodeDescription);
    }

    public void unregisterMobileNode(MobileNodeDescription mobileNodeDescription) {
        LinkedList<MobileNodeDescription> temp = new LinkedList<>();
        MobileNodeDescription node = MobileNodes.remove();
        while(node != null) {
            if(node.getId() != mobileNodeDescription.getId())
                temp.add(node);
            node = MobileNodes.remove();
        }
        MobileNodes = temp;
    }

    public void queueJob(JobDescription jobDescription) {
        JobsQueue.add(jobDescription);
    }

    public int getTotalNodes() {
        return MobileNodes.size();
    }
}
