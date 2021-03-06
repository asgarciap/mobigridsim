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
import mobigrid.gridserver.behavior.UpdateStatusNodeGuard;
import mobigrid.simulation.state.SimulationState;

/**
 * @author arturogarcia
 */
public class SimulateExecuteProgramGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SimulationState se = (SimulationState) this.getAgent().getState();

        synchronized (se) {
            //Get MobileNode description
            AssignedJob assignedJob = (AssignedJob) eventBESA.getData();

            AgHandlerBESA ah;

            se.addJob(assignedJob.getNodeId(), assignedJob);

            //Update Node Status in the dashboard
            MobileNodeDescription node = se.getMobileNodeStatus(assignedJob.getNodeId());

            //now we need no notify the dashboard than a mobile node had been updated
            EventBESA eventUpdate = new EventBESA(UpdateNodesStatusGuard.class.getName(), node);
            try {
                //get the dashboard agent handler
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DASHBOARD.toString());
                //send to it the event
                ah.sendEvent(eventUpdate);

                EventBESA eventUpdateDispatcher = new EventBESA(UpdateStatusNodeGuard.class.getName(), node);
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DISPATCHER.toString());
                ah.sendEvent(eventUpdateDispatcher);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }
    }
}
