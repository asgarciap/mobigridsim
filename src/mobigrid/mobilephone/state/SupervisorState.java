package mobigrid.mobilephone.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;
import mobigrid.simulation.state.MobilePhone;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stores the jobs currently running in a mobile node.
 * @author arturogarcia
 */
public class SupervisorState extends StateBESA {

    private List<String> DownloadingFilesList;
    private List<JobDescription> JobList;
    private int NodeId;

    public SupervisorState(int nodeId) {
        DownloadingFilesList = new ArrayList<>();
        JobList = new ArrayList<>();
        NodeId = nodeId;
    }

    public int getNodeId() {
        return NodeId;
    }

    public void addJob(JobDescription job) {
        job.setState(GridJobStateEnum.QUEUED);
        JobList.add(job);
    }

    public void removeJob(JobDescription job) {
        JobList.remove(job);
    }

    public JobDescription getJobByName(String name) {
        for(JobDescription j : JobList) {
            if(j.getName().equals(name))
                return j;
        }
        return null;
    }

    public void addDownloadingFile(String name) {
        DownloadingFilesList.add(name);
    }

    public void removeDownloadingFile(String name) {
        DownloadingFilesList.remove(name);
    }

    public JobDescription getNextQueuedJob() {
        for(JobDescription j : JobList) {
            if(j.getState() == GridJobStateEnum.QUEUED)
                return j;
        }
        return null;
    }

    public JobDescription getNextDownloadingJob() {
        for(JobDescription j : JobList) {
            if(j.getState() == GridJobStateEnum.DOWNLOADING)
                return j;
        }
        return null;
    }

    public JobDescription getNextFinishedJob() {
        for(JobDescription j : JobList) {
            if(j.getState() == GridJobStateEnum.FINISHED)
                return j;
        }
        return null;
    }

    public boolean isJobReadyToRun(JobDescription job) {
        for(Map.Entry<String,Float> item : job.getInputFiles().entrySet()) {
            if(DownloadingFilesList.contains(item.getKey()))
                return false;
        }
        return true;
    }
}
