package mobigrid.mobilephone.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AssignedJob;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;
import mobigrid.mobilephone.state.SupervisorState;

/**
 * Guard executed when a job finish its execution
 * @author arturogarcia
 */
public class JobExecutedGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        SupervisorState ss = (SupervisorState) this.getAgent().getState();
        synchronized (ss) {
            JobDescription j = (JobDescription) eventBESA.getData();
            AssignedJob assignedJob = ss.getAssignedJobByName(j.getName());
            if (assignedJob != null) {
                assignedJob.getJobDescription().setState(GridJobStateEnum.FINISHED);
            } else {
                ReportBESA.error("Job with name " + j.getName() + " reported as finished not found in job pending list.");
            }
        }
    }
}
