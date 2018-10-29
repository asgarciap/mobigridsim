package mobigrid.mobilephone.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Log.ReportBESA;
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
        JobDescription j = (JobDescription) eventBESA.getData();
        JobDescription jl = ss.getJobByName(j.getName());
        if(jl != null) {
            jl.setState(GridJobStateEnum.FINISHED);
        }
        else {
            ReportBESA.error("Job with name " + j.getName() + " reported as finished not found in job pending list.");
        }
    }
}
