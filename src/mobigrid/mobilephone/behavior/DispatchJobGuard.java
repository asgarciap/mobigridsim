package mobigrid.mobilephone.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import mobigrid.common.AssignedJob;
import mobigrid.common.JobDescription;
import mobigrid.mobilephone.state.SupervisorState;

/**
 * Guard executed when a new job is received by
 * the mobile node supervisor in order to be executed.
 * @author arturogarcia
 */
public class DispatchJobGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SupervisorState ss = (SupervisorState) this.getAgent().getState();

        synchronized (ss) {
            //Get Job description
            AssignedJob assignedJob = (AssignedJob) eventBESA.getData();

            //add it to the simulation
            ss.addAssignedJob(assignedJob);
        }
    }
}
