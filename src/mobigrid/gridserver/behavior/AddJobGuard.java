package mobigrid.gridserver.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.JobDescription;
import mobigrid.gridserver.state.DispatcherState;
import mobigrid.mobilephone.behavior.DispatchJobGuard;

/**
 * Guard executed when a new grid job needs to be
 * added to the simulation.
 * This guard triggers the dispatcher algorithm to
 * select the best mobile node to execute this job
 * and then dispatch it in order to start
 * the execution of the job in the grid.
 * @author arturogarcia
 */
public class AddJobGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        DispatcherState dispatcherState = (DispatcherState) this.getAgent().getState();

        AgHandlerBESA ah;

        JobDescription jobDescription = (JobDescription) eventBESA.getData();

        AssignedJob assignedJob = dispatcherState.dispatchJob(jobDescription);

        EventBESA event = new EventBESA(DispatchJobGuard.class.getName(), jobDescription);
        EventBESA eventStatus = new EventBESA(ReportJobStatusGuard.class.getName(), assignedJob);
        try {
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString()+assignedJob.getNodeId());
            //send the event.
            ah.sendEvent(event);

            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.MAIN_SUPERVISOR.toString());
            //send the event
            ah.sendEvent(eventStatus);
        }catch(ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
