package mobigrid.simulation.state;

import BESA.Kernell.Agent.StateBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.*;
import mobigrid.dashboard.state.GridJob;
import mobigrid.dashboard.state.SimulationDashboard;
import mobigrid.mobilephone.SupervisorAgent;
import mobigrid.mobilephone.state.SupervisorState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the current state of the simulation.
 * It has access to the mobile nodes
 * that are part of the grid.
 * @author arturogarcia
 */
public class SimulationState extends StateBESA {

    private Map<Integer, MobilePhone> MobilePhones;
    private Map<String, GridJobData> Downloads;
    private Map<String, AssignedJob> Jobs;
    private boolean CollaborationEnabled;

    public SimulationState() {
        MobilePhones = new HashMap<>();
        Downloads = new HashMap<>();
        Jobs = new HashMap<>();
        CollaborationEnabled = false;
    }

    public void enableColaboration() {
        CollaborationEnabled = true;
    }

    public boolean isColaborationEnabled() {
        return CollaborationEnabled;
    }

    public void addMobileNode(MobileNodeDescription nodeDescription) {
        MobilePhone mp = new MobilePhone(nodeDescription);
        MobilePhones.put(nodeDescription.getId(), mp);
    }

    public void addDownload(int nodeId, String dataId, float dataSize) {
        GridJobData data = new GridJobData(nodeId, dataId, dataSize);
        Downloads.put(nodeId+dataId,data);
    }

    public void addJob(int nodeId, AssignedJob assignedJob) {
        Jobs.put(nodeId+assignedJob.getJobDescription().getName(), assignedJob);
    }

    public void processJobs() {
        if(Jobs.size() > 0) {
            for (Map.Entry<String, AssignedJob> jobs : Jobs.entrySet()) {
                if (executeJob(jobs.getValue().getNodeId(), jobs.getValue().getJobDescription())) {
                    jobs.getValue().getJobDescription().setState(GridJobStateEnum.FINISHED);
                }
            }
        }
    }

    public AssignedJob getNextJobFinished() {
        AssignedJob job = null;
        for (Map.Entry<String, AssignedJob> jobs : Jobs.entrySet()) {
            if(jobs.getValue().getJobDescription().getState() == GridJobStateEnum.FINISHED) {
                job = jobs.getValue();
                Jobs.remove(jobs.getKey());
                return job;
            }
        }
        return null;
    }

    public boolean executeJob(int phoneId, JobDescription jobDescription) {
        MobilePhone phone = MobilePhones.getOrDefault(phoneId, null);
        if(phone != null) {
            Program program = new Program(jobDescription.getName(), jobDescription.getProgramFileSize(), (int) jobDescription.getComputationalTime());

            //Assign input files references to the program instance
            for (Map.Entry<String, Float> inputFile : jobDescription.getInputFiles().entrySet()) {
                program.addInputData(inputFile.getKey(), inputFile.getValue());
            }

            try {
                if(phone.installProgram(program)) {
                    jobDescription.updateComputationTime();
                    return phone.executeProgram(program.getName());
                }
            } catch (Exception ex) {
                ReportBESA.error(ex);
                return false;
            }
        }
        return false;
    }

    public void processDownloads() {
        if(Downloads.size() > 0) {
            for (Map.Entry<String, GridJobData> downloads : Downloads.entrySet()) {
                if (downloadData(downloads.getValue().getNodeId(), downloads.getValue().getDataId(), downloads.getValue().getDataSize())) {
                    downloads.getValue().setDownloaded(true);
                }
            }
        }
    }

    public List<AssignedJob> cleanOrphanJobs() {
        //remove jobs that can be finished due to phone disconnection
        int removed = 0;
        List<AssignedJob> tmp = new ArrayList<>();
        for (Map.Entry<String, AssignedJob> jobs : Jobs.entrySet()) {
            MobilePhone phone = getMobilePhoneById(jobs.getValue().getNodeId());
            if(phone == null || phone.getCurrentStatus().getState() == NodeStateEnum.DISCONNECTED) {
                tmp.add(jobs.getValue());
                removed++;
            }
        }

        if(removed > 0) {
            for (AssignedJob j : tmp) {
                Jobs.remove(j.getNodeId() + j.getJobDescription().getName());
            }
        }
        return tmp;
    }

    public List<GridJobData> cleanOrphanDownloads() {
        //remove jobs that can be finished due to phone disconnection
        int removed = 0;
        List<GridJobData> tmp = new ArrayList<>();
        for (Map.Entry<String, GridJobData> data : Downloads.entrySet()) {
            MobilePhone phone = getMobilePhoneById(data.getValue().getNodeId());
            if(phone == null || phone.getCurrentStatus().getState() == NodeStateEnum.DISCONNECTED) {
                tmp.add(data.getValue());
                removed++;
            }
        }
        if(removed > 0) {
            for(GridJobData d : tmp) {
                Downloads.remove(d.getNodeId()+d.getDataId());
            }
        }
        return tmp;
    }

    public int cleanDisconnectedPhones() {
        List<Integer> toRemove = new ArrayList<>();
        for (Map.Entry<Integer, MobilePhone> phones : MobilePhones.entrySet()) {
            if (phones.getValue().getState() == NodeStateEnum.DISCONNECTED)
                toRemove.add(phones.getKey());
        }
        int removed = 0;
        for(Integer id: toRemove) {
            MobilePhones.remove(id);
            removed++;
        }
        return removed;
    }

    public GridJobData getNextDataDownloaded() {
        GridJobData retData = null;
        for (Map.Entry<String, GridJobData> data : Downloads.entrySet()) {
            if(data.getValue().isDownloaded()) {
                retData = data.getValue();
                Downloads.remove(data.getKey());
                return retData;
            }
        }
        return null;
    }

    public boolean downloadData(int phoneId, String dataId, float dataSize) {
        MobilePhone phone = MobilePhones.getOrDefault(phoneId, null);
        try {
            if(phone != null)
                return phone.downloadData(dataId, dataSize);
        }catch (Exception ex) {
            if(phone.eraseNextDataBuffer() == 0.0)
                return false;
        }
        return false;
    }

    public void removeMobileNode(MobileNodeDescription nodeDescription) {
        if(MobilePhones.containsKey(nodeDescription.getId()))
            MobilePhones.remove(nodeDescription.getId());
    }

    public List<MobileNodeDescription> getMobileNodesStatus() {
        List<MobileNodeDescription> md = new ArrayList<>();
        for (Map.Entry<Integer, MobilePhone> phones : MobilePhones.entrySet()) {
            MobileNodeDescription mdn = phones.getValue().getCurrentStatus();
            md.add(mdn);
        }
        return md;
    }

    public MobileNodeDescription getMobileNodeStatus(int id) {
        MobileNodeDescription r = null;
        for (Map.Entry<Integer, MobilePhone> phones : MobilePhones.entrySet()) {
            if(phones.getKey() == id) {
                r = new MobileNodeDescription(phones.getValue().getCurrentStatus());
            }
        }
        return r;
    }

    public int getTotalJobs() {
        return Jobs.size();
    }

    public int getTotalDownloads() {
        return Downloads.size();
    }

    public int getTotalPhones() {
        return MobilePhones.size();
    }

    public MobilePhone getMobilePhoneById(int id) {
        for (Map.Entry<Integer, MobilePhone> phones : MobilePhones.entrySet()) {
            if(phones.getKey() == id)
                return phones.getValue();
        }
        return null;
    }
}
