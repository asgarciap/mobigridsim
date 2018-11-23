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

        JobDescription jobDescription = (JobDescription) eventBESA.getData();

        dispatcherState.queueJob(jobDescription);
        ReportBESA.info("Nuevo Job agregado a la cola del dispatcher. Total Jobs en cola: "+dispatcherState.getJobsQueueSize());
    }
}
