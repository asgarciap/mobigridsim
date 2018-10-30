package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.JobDescription;
import mobigrid.mobilephone.behavior.JobExecutedGuard;
import mobigrid.simulation.state.ProcessSimulationState;
import mobigrid.simulation.state.SimulationState;

/**
 * Guard executed when have finished the simulation of
 * executing a program in a mobile node.
 * @author arturogarcia
 */
public class ExecuteProgramSimulatedGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        ProcessSimulationState processSimulationState = (ProcessSimulationState) this.getAgent().getState();

        JobDescription jobDescription = (JobDescription) eventBESA.getData();

        AgHandlerBESA ah;

        //now we need no notify the dashboard than a new mobile node had been added
        EventBESA event = new EventBESA(JobExecutedGuard.class.getName(), jobDescription);
        try {
            //get the dashboard agent handler
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.EXECUTOR.toString()+processSimulationState.getPhoneId());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
