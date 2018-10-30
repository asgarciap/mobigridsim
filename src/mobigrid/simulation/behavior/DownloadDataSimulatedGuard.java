package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.GridJobData;
import mobigrid.common.JobDescription;
import mobigrid.mobilephone.behavior.DataDownloadedGuard;
import mobigrid.mobilephone.behavior.JobExecutedGuard;
import mobigrid.simulation.state.ProcessSimulationState;
import mobigrid.simulation.state.SimulationState;

/**
 * Guard executed when have finished the simulation of
 * executing a program in a mobile node.
 * @author arturogarcia
 */
public class DownloadDataSimulatedGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        SimulationState simulationState = (SimulationState) this.getAgent().getState();

        GridJobData gridJobData = (GridJobData) eventBESA.getData();

        AgHandlerBESA ah;

        EventBESA event = new EventBESA(DataDownloadedGuard.class.getName(), gridJobData);
        try {
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString()+gridJobData.getNodeId());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
