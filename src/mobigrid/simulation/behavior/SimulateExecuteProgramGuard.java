package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.simulation.state.SimulationState;

/**
 * @author arturogarcia
 */
public class SimulateExecuteProgramGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SimulationState se = (SimulationState) this.getAgent().getState();

        //Get MobileNode description
        AssignedJob assignedJob = (AssignedJob) eventBESA.getData();

        AgHandlerBESA ah;

        //now we need no notify the dashboard than a new mobile node had been added
        EventBESA event = new EventBESA(ProcessSimulateExecuteProgramGuard.class.getName(), assignedJob.getJobDescription());
        try {
            //get the dashboard agent handler
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.PROCESS_SIMULATION.toString()+assignedJob.getNodeId());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
