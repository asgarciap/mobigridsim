package mobigrid.mobilephone.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.GridJobData;
import mobigrid.common.JobDescription;
import mobigrid.mobilephone.state.DownloaderState;
import mobigrid.mobilephone.state.SupervisorState;
import mobigrid.simulation.behavior.SimulateDownloadDataGuard;

/**
 * @author arturogarcia
 */
public class DownloadDataGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {

        DownloaderState downloaderState = (DownloaderState) this.getAgent().getState();

        GridJobData gridJobData = (GridJobData) eventBESA.getData();

        AgHandlerBESA ah;

        EventBESA event = new EventBESA(SimulateDownloadDataGuard.class.getName(), gridJobData);
        try {
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
