package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.GridJobData;
import mobigrid.common.GridJobStateEnum;
import mobigrid.common.JobDescription;
import mobigrid.simulation.state.ProcessSimulationState;

/**
 * @author arturogarcia
 */
public class ProcessSimulateDownloadDataGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        ProcessSimulationState processSimulationState = (ProcessSimulationState) this.getAgent().getState();

        GridJobData gridJobData = (GridJobData) eventBESA.getData();

        if(processSimulationState.downloadData(gridJobData.getDataId(), gridJobData.getDataSize())) {
            gridJobData.setDownloaded(true);
        }
        else {
            gridJobData.setDownloaded(false);
        }

        AgHandlerBESA ah;

        EventBESA event = new EventBESA(DownloadDataSimulatedGuard.class.getName(), gridJobData);
        try {
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
