package mobigrid.mobilephone.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.GridJobData;
import mobigrid.mobilephone.state.SupervisorState;

/**
 * @author arturogarcia
 */
public class DataDownloadAbortedGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SupervisorState ss = (SupervisorState) this.getAgent().getState();
        synchronized (ss) {
            GridJobData d = (GridJobData) eventBESA.getData();
            ReportBESA.info("Descarga de datos abortada - Datos Id: "+d.getDataId());
            ss.removeDownloadingFile(d.getDataId());
        }
    }
}
