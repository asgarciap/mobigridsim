package mobigrid.gridserver.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the jobs currently running in the simulation
 * @author arturogarcia
 */
public class MainSupervisorState extends StateBESA {

    private Map<Integer, List<JobDescription>> JobList;

    public MainSupervisorState() {
        JobList = new HashMap<>();
    }

    public void addJob(JobDescription job, int nodeId) {
        boolean found = false;
        if(JobList.containsKey(nodeId)) {
            for(JobDescription j : JobList.get(nodeId)) {
                if(j.getName().equals(job.getName())) {
                    j = job;
                    found = true;
                }
            }
            if(!found)
                JobList.get(nodeId).add(job);
        }
    }

    public List<JobDescription> getJobsByNodeId(int id) {
        for(Map.Entry<Integer,List<JobDescription>> item : JobList.entrySet()) {
            if(item.getKey() == id)
                return item.getValue();
        }
        return new ArrayList<>();
    }

    public List<JobDescription> getAllJobs() {
        List<JobDescription> jobs = new ArrayList<>();
        for(Map.Entry<Integer,List<JobDescription>> item : JobList.entrySet()) {
                jobs.addAll(item.getValue());
        }
        return jobs;
    }

    public JobDescription getJob(String name, int nodeId) {
        if(JobList.containsKey(nodeId)) {
            for (JobDescription j : JobList.get(nodeId))
                if (j.getName().equals(name))
                    return j;
        }
        return null;
    }
}
