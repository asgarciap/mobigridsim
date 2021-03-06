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
        ReportBESA.info("--------Verificando estado de Jobs y Descargas----------");
        synchronized (ss) {
            AgHandlerBESA ah;
            //Iterates over all assigned jobs
            AssignedJob assignedJob = ss.getNextAssignedJob();
            while (assignedJob != null) {
                //Check for files to download
                if(assignedJob.getJobDescription().getState() == GridJobStateEnum.QUEUED) {
                    for (Map.Entry<String, Float> item : assignedJob.getJobDescription().getInputFiles().entrySet()) {
                        ss.addDownloadingFile(item.getKey());
                        GridJobData d = new GridJobData(assignedJob.getNodeId(), item.getKey(), item.getValue());
                        EventBESA event = new EventBESA(DownloadDataGuard.class.getName(), d);
                        try {
                            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DOWNLOADER.toString());
                            //send the event.
                            ah.sendEvent(event);

                            assignedJob.getJobDescription().setState(GridJobStateEnum.DOWNLOADING);

                            //send event to the main supervisor
                            EventBESA eventSup = new EventBESA(ReportJobStatusGuard.class.getName(), assignedJob);
                            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
                            ah.sendEvent(eventSup);
                        } catch (ExceptionBESA ex) {
                            ReportBESA.error(ex);
                        }
                    }
                //check if we have a job to run
                }else if(assignedJob.getJobDescription().getState() == GridJobStateEnum.DOWNLOADING) {
                    if (ss.isAssignedJobReadyToRun(assignedJob)) {
                        assignedJob.getJobDescription().setState(GridJobStateEnum.READY);
                        try {
                            EventBESA event = new EventBESA(ExecuteJobGuard.class.getName(), assignedJob);
                            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.EXECUTOR.toString());

                            //send the event
                            ah.sendEvent(event);
                            assignedJob.getJobDescription().setState(GridJobStateEnum.RUNNING);

                            //send event to the main supervisor
                            this.reportAssignedJobSupervisor(assignedJob);
                        } catch (ExceptionBESA ex) {
                            ReportBESA.error(ex);
                        }
                    } else if(assignedJob.isTimedOut()){
                        assignedJob.getJobDescription().setState(GridJobStateEnum.DISCARDED);
                        this.reportAssignedJobSupervisor(assignedJob);
                        ss.removeAssignedJob(assignedJob);
                    } else {
                        ReportBESA.info("Esperando por descarga de datos del Job: "+assignedJob.getJobDescription().getName()+" en el Nodo:"+assignedJob.getNodeId()+ " Descargas Activas: "+ss.getTotalDownloadingFiles());
                    }
                //check for jobs finished
                }else if(assignedJob.getJobDescription().getState() == GridJobStateEnum.FINISHED) {
                    this.reportAssignedJobSupervisor(assignedJob);
                    ss.removeAssignedJob(assignedJob);
                } else if(assignedJob.getJobDescription().getState() == GridJobStateEnum.RUNNING && assignedJob.isTimedOut()) {
                    assignedJob.getJobDescription().setState(GridJobStateEnum.DISCARDED);
                    this.reportAssignedJobSupervisor(assignedJob);
                    ss.removeAssignedJob(assignedJob);
                }else {
                    ;//ReportBESA.error("Job Assignado con estado no procesable. NodoId: "+assignedJob.getNodeId()+" - Job: "+assignedJob.getJobDescription().getName()+" - Estado: "+assignedJob.getJobDescription().getState());
                }
                assignedJob = ss.getNextAssignedJob();
            }
        }
    }

    public void reportAssignedJobSupervisor(AssignedJob assignedJob) {
        AgHandlerBESA ah;
        try {
            //Send Event to Main Supervisor to report aborted job
            EventBESA eventSup = new EventBESA(ReportJobStatusGuard.class.getName(), assignedJob);
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
            ah.sendEvent(eventSup);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}