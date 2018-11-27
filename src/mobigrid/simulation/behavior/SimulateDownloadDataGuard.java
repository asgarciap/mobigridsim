package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.AssignedJob;
import mobigrid.common.GridJobData;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.gridserver.behavior.UpdateStatusNodeGuard;
import mobigrid.simulation.state.SimulationState;

/**
 * @author arturogarcia
 */
public class SimulateDownloadDataGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SimulationState se = (SimulationState) this.getAgent().getState();

        synchronized (se) {
            //Get MobileNode description
            GridJobData gridJobData = (GridJobData) eventBESA.getData();

            ReportBESA.info("Agregando datos para descarga. DataId:" + gridJobData.getDataId());
            se.addDownload(gridJobData.getNodeId(), gridJobData.getDataId(), gridJobData.getDataSize());

            AgHandlerBESA ah;

            //Update Node Status in the dashboard
            MobileNodeDescription node = se.getMobileNodeStatus(gridJobData.getNodeId());

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
