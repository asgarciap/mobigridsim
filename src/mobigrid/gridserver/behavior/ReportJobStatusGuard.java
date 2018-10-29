package mobigrid.gridserver.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.JobDescription;
import mobigrid.dashboard.behavior.UpdateJobStatusGuard;
import mobigrid.dashboard.state.SimulationDashboard;
import mobigrid.gridserver.state.MainSupervisorState;
import mobigrid.simulation.behavior.AddMobileNodeGuard;

/**
 * Guard executed when there is an update in the
 * job list.
 * This could be because a new job had been added
 * to the simulation or a job already added to the
 * grid had changed its state.
 * This guard is used to update the job status
 * in the main supervisor agent.
 * @author arturogarcia
 */
public class ReportJobStatusGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {

        AgHandlerBESA ah;

        //Get the agent state
        MainSupervisorState mss = (MainSupervisorState) this.getAgent().getState();

        //Get Job updated
        AssignedJob assignedJob = (AssignedJob) eventBESA.getData();

        JobDescription job = mss.getJob(assignedJob.getJobDescription().getName(),assignedJob.getNodeId());

        if(job != null) {
            job.setState(assignedJob.getJobDescription().getState());
        }else {
            job = assignedJob.getJobDescription();
            mss.addJob(job,assignedJob.getNodeId());
        }

        EventBESA event = new EventBESA(UpdateJobStatusGuard.class.getName(), job);
        try {
            //update the job status in the dashboard
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DASHBOARD.toString());

            //send the event to the dashboard
            ah.sendEvent(event);
        }catch(ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
