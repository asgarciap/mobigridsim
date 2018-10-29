package mobigrid.mobilephone.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.*;
import mobigrid.dashboard.behavior.UpdateJobStatusGuard;
import mobigrid.gridserver.behavior.ReportJobStatusGuard;
import mobigrid.gridserver.state.AdministratorState;
import mobigrid.mobilephone.state.SupervisorState;
import mobigrid.simulation.behavior.AddMobileNodeGuard;

import java.util.Map;

/**
 * Periodic Guard to check jobs currently executed
 * in the mobile node.
 * @author arturogarcia
 */
public class CheckJobsGuard extends PeriodicGuardBESA {

    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SupervisorState ss = (SupervisorState) this.getAgent().getState();

        AgHandlerBESA ah;

        //Check for files to download
        JobDescription queuedJob = ss.getNextQueuedJob();
        while(queuedJob != null) {
            for(Map.Entry<String,Float> item : queuedJob.getInputFiles().entrySet()) {
                ss.addDownloadingFile(item.getKey());
                GridJobData d = new GridJobData(item.getKey(), item.getValue());
                EventBESA event = new EventBESA(DownloadDataGuard.class.getName(), d);
                try {
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DOWNLOADER.toString()+ss.getNodeId());
                    //send the event.
                    ah.sendEvent(event);

                    queuedJob.setState(GridJobStateEnum.DOWNLOADING);

                    //send event to the main supervisor
                    AssignedJob assignedJob = new AssignedJob(ss.getNodeId(),queuedJob);
                    EventBESA eventSup = new EventBESA(ReportJobStatusGuard.class.getName(),assignedJob);
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
                    ah.sendEvent(eventSup);
                    assignedJob = null;
                    eventSup = null;
                }catch(ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }

            }
            queuedJob = ss.getNextQueuedJob();
        }

        //Now, check if we have a job to run
        JobDescription downloadingJob = ss.getNextDownloadingJob();
        while(downloadingJob != null) {
            if(ss.isJobReadyToRun(downloadingJob)) {
                downloadingJob.setState(GridJobStateEnum.READY);
                EventBESA event = new EventBESA(ExecuteJobGuard.class.getName(), downloadingJob);
                try {
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.EXECUTOR.toString()+ss.getNodeId());

                    //send the event
                    ah.sendEvent(event);
                    downloadingJob.setState(GridJobStateEnum.RUNNING);

                    //send event to the main supervisor
                    AssignedJob assignedJob = new AssignedJob(ss.getNodeId(),downloadingJob);
                    EventBESA eventSup = new EventBESA(ReportJobStatusGuard.class.getName(),assignedJob);
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
                    ah.sendEvent(eventSup);
                    assignedJob = null;
                    eventSup = null;
                }catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
            }
            downloadingJob = ss.getNextDownloadingJob();
        }

        //Finally check for jobs finished in order to report them to the Main Supervisor.
        JobDescription finishedJob = ss.getNextFinishedJob();
        while(finishedJob != null) {
            try {
                //Send Event to Main Supervisor to report than a new job has finished
                AssignedJob assignedJob = new AssignedJob(ss.getNodeId(), finishedJob);
                EventBESA eventSup = new EventBESA(ReportJobStatusGuard.class.getName(), assignedJob);
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
                ah.sendEvent(eventSup);
                ss.removeJob(finishedJob);
                assignedJob = null;
                eventSup = null;
            }catch(ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
            finishedJob = ss.getNextFinishedJob();
        }
    }
}
