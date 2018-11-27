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
        SimulationState simulationState = (SimulationState) this.getAgent().getState();

        synchronized (simulationState) {
            AssignedJob assignedJob = (AssignedJob) eventBESA.getData();

            AgHandlerBESA ah;

            //now we need no notify the dashboard than a new mobile node had been added
            EventBESA event = new EventBESA(JobExecutedGuard.class.getName(), assignedJob.getJobDescription());
            try {
                //get the dashboard agent handler
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.EXECUTOR.toString());
                //send to it the event
                ah.sendEvent(event);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }

            //Update Node Status in the dashboard
            MobileNodeDescription node = simulationState.getMobileNodeStatus(assignedJob.getNodeId());

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
