package mobigrid.mobilephone.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.AssignedJob;
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
    private List<AssignedJob> AssignedJobList;
    private int CurrentDownloadingIndex;
    private int CurrentAssignedIndex;
    public SupervisorState() {
        DownloadingFilesList = new ArrayList<>();
        AssignedJobList = new ArrayList<>();
        CurrentAssignedIndex = 0;
        CurrentDownloadingIndex = 0;
    }

    public void addAssignedJob(AssignedJob assignedJob) {
        assignedJob.getJobDescription().setState(GridJobStateEnum.QUEUED);
        AssignedJobList.add(assignedJob);
    }

    public void removeAssignedJob(AssignedJob job) {
        AssignedJobList.remove(job);
    }

    public AssignedJob getAssignedJobByName(String name) {
        for(AssignedJob j : AssignedJobList) {
            if(j.getJobDescription().getName().equals(name))
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

    public int getTotalDownloadingFiles() {
        return DownloadingFilesList.size();
    }

    public AssignedJob getNextAssignedJob() {
        AssignedJob j = null;
        if(CurrentAssignedIndex < AssignedJobList.size()){
            j = AssignedJobList.get(CurrentAssignedIndex);
            CurrentAssignedIndex++;
        }else {
            CurrentAssignedIndex = 0;
        }
        return j;
    }

    public AssignedJob getNextDownloadingAssignedJob() {

        for(AssignedJob j : AssignedJobList) {
            if(j.getJobDescription().getState() == GridJobStateEnum.DOWNLOADING)
                return j;
        }
        return null;
    }

    public AssignedJob getNextFinishedAssignedJob() {
        for(AssignedJob j : AssignedJobList) {
            if(j.getJobDescription().getState() == GridJobStateEnum.FINISHED)
                return j;
        }
        return null;
    }

    public boolean isAssignedJobReadyToRun(AssignedJob job) {

        if(job.getJobDescription().getWorkComplete() > 0)
            return true; //hack

        for(Map.Entry<String,Float> item : job.getJobDescription().getInputFiles().entrySet()) {
            if(DownloadingFilesList.contains(item.getKey()))
                return false;
        }
        return true;
    }
}
