package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.*;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.simulation.state.ProcessSimulationState;
import mobigrid.simulation.state.SimulationState;

/**
 * @author arturogarcia
 */
public class ProcessSimulateExecuteProgramGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        ProcessSimulationState processSimulationState = (ProcessSimulationState) this.getAgent().getState();

        JobDescription jobDescription = (JobDescription) eventBESA.getData();

        if(processSimulationState.executeJob(jobDescription))
            jobDescription.setState(GridJobStateEnum.FINISHED);
        else
            jobDescription.setState(GridJobStateEnum.DISCARDED);

        AgHandlerBESA ah;

        AssignedJob assignedJob = new AssignedJob(processSimulationState.getPhoneId(), jobDescription);

        //now we need no notify the dashboard than a new mobile node had been added
        EventBESA event = new EventBESA(ExecuteProgramSimulatedGuard.class.getName(), assignedJob);
        try {
            //get the dashboard agent handler
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
