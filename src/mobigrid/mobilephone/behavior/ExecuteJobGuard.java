package mobigrid.mobilephone.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.mobilephone.state.ExecutorState;
import mobigrid.simulation.behavior.SimulateExecuteProgramGuard;

/**
 * @author arturogarcia
 */
public class ExecuteJobGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        ExecutorState executorState = (ExecutorState) this.getAgent().getState();

        AssignedJob assignedJob = (AssignedJob) eventBESA.getData();

        AgHandlerBESA ah;

        EventBESA event = new EventBESA(SimulateExecuteProgramGuard.class.getName(), assignedJob);
        try {
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
