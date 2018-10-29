package mobigrid.dashboard.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import mobigrid.common.JobDescription;
import mobigrid.dashboard.state.SimulationDashboard;

/**
 * Guard executed when there is an update in the
 * job list.
 * This could be because a new job had been added
 * to the simulation or a job already added to the
 * grid had changed its state.
 * This guard is used to update the dashboard.
 * @author arturogarcia
 */
public class UpdateJobStatusGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {

        //Get the agent state
        SimulationDashboard sd = (SimulationDashboard) this.getAgent().getState();
        //Get Job updated
        JobDescription job = (JobDescription) eventBESA.getData();
        //Update Dashboard
        sd.updateJob(job);
    }
}
