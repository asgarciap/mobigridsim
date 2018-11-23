package mobigrid.gridserver.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.*;

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
        SEAS
    };

    private boolean CollaborationEnabled;

    public DispatcherState() {
        JobsQueue = new LinkedList<>();
        MobileNodes = new LinkedList<>();
        Strategy = DispatcherStrategy.ROUND_ROBIN;
        CollaborationEnabled = false;
    }

    public void enableColaboration() {
        CollaborationEnabled = true;
        Strategy = DispatcherStrategy.SEAS;
    }

    public boolean isColaborationEnabled() {
        return CollaborationEnabled;
    }

    public AssignedJob dispatchNextJob() {
        if(MobileNodes.size() > 0 && JobsQueue.size() > 0) {
            //Round Robin Dispatcher
            if(Strategy == DispatcherStrategy.ROUND_ROBIN) {
                MobileNodeDescription node = MobileNodes.remove();
                AssignedJob assignedJob = null;
                boolean finish = false;
                JobDescription job = JobsQueue.remove();
                while(!finish) {
                    if(node.getState() == NodeStateEnum.READY) {
                        job.setState(GridJobStateEnum.DISPATCHED);
                        assignedJob = new AssignedJob(node.getId(), job);
                        //mode node to the last position of the queue
                        MobileNodes.add(node);
                        finish = true;
                    }else {
                        JobsQueue.add(job);
                        if(node.getState() != NodeStateEnum.DISCONNECTED)
                            MobileNodes.add(node);

                        if(MobileNodes.size() > 0)
                            node = MobileNodes.remove();
                        else
                            finish = true;
                    }
                }
                return assignedJob;
            }else if(Strategy == DispatcherStrategy.SEAS) {
                //Envio los trabajos a los nodos con mas carga
                MobileNodeDescription node = MobileNodes.remove();
                AssignedJob assignedJob = null;
                MobileNodeDescription selected = null;
                boolean finish = false;
                int ix = 0;
                JobDescription job = JobsQueue.remove();
                while(!finish) {
                    if (node.getState() == NodeStateEnum.READY) {
                        if (selected == null || selected.getBatteryLevel() < node.getBatteryLevel())
                            selected = node;
                    }
                    ix++;

                    if(node.getState() != NodeStateEnum.DISCONNECTED)
                        MobileNodes.add(node);

                    if(ix < MobileNodes.size()) {
                        node = MobileNodes.remove();
                    }else {
                        finish = true;
                    }
                }
                if(selected != null) {
                    assignedJob = new AssignedJob(selected.getId(), job);
                }else {
                    JobsQueue.add(job);
                }
                return assignedJob;
            }
        }
        return null;
    }

    public void registerMobileNode(MobileNodeDescription mobileNodeDescription) {
        MobileNodes.add(mobileNodeDescription);
    }

    public void unregisterMobileNode(MobileNodeDescription mobileNodeDescription) {
        if(MobileNodes.size() > 0) {
            int queueSize = MobileNodes.size();
            for(int i=0; i<queueSize;i++) {
                MobileNodeDescription node = MobileNodes.remove();
                if(node.getId() != mobileNodeDescription.getId())
                    MobileNodes.add(node);
            }
        }
    }

    public void queueJob(JobDescription jobDescription) {
        JobsQueue.add(jobDescription);
    }

    public int getTotalNodes() {
        return MobileNodes.size();
    }

    public int getJobsQueueSize() {
        return JobsQueue.size();
    }
}
