package mobigrid.gridserver.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.JobDescription;
import mobigrid.gridserver.state.DispatcherState;
import mobigrid.mobilephone.behavior.DispatchJobGuard;

/**
 * Guard executed  periodically to check if
 * there are new jobs queued to  dispatch
 * to the mobile nodes.
 * If so, the jobs are dispatched to every node
 * in the simulation.
 * @author arturogarcia
 */
public class DispatchJobsGuard extends PeriodicGuardBESA {

    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        //Get the agent state
        DispatcherState dispatcherState = (DispatcherState) this.getAgent().getState();
            AgHandlerBESA ah;

            AssignedJob assignedJob = dispatcherState.dispatchNextJob();
            if (assignedJob != null) {

                EventBESA event = new EventBESA(DispatchJobGuard.class.getName(), assignedJob.getJobDescription());
                EventBESA eventStatus = new EventBESA(ReportJobStatusGuard.class.getName(), assignedJob);
                try {
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString() + assignedJob.getNodeId());
                    //send the event.
                    ah.sendEvent(event);

                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
                    //send the event
                    ah.sendEvent(eventStatus);
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
            }
    }
}
