package mobigrid.mobilephone.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;
import mobigrid.gridserver.behavior.AddJobGuard;
import mobigrid.mobilephone.state.SupervisorState;

/**
 * Guard executed when a job had been paused due to
 * desconnection of a node.
 * It must be re added to the dispatcher
 * @author arturogarcia
 */
public class JobPausedGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        SupervisorState ss = (SupervisorState) this.getAgent().getState();
        synchronized (ss) {
            JobDescription j = (JobDescription) eventBESA.getData();
            AssignedJob assignedJob = ss.getAssignedJobByName(j.getName());
            if (assignedJob != null) {
                assignedJob.getJobDescription().setState(GridJobStateEnum.PAUSED);
                EventBESA event = new EventBESA(AddJobGuard.class.getName(), assignedJob.getJobDescription());
                AgHandlerBESA ah;
                try {
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DISPATCHER.toString());
                    ah.sendEvent(event);
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
            } else {
                ReportBESA.error("Job with name " + j.getName() + " reported as finished not found in job pending list.");
            }
        }
    }
}
