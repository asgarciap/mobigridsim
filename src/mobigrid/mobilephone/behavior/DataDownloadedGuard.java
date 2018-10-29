package mobigrid.mobilephone.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import mobigrid.common.GridJobData;
import mobigrid.common.JobDescription;
import mobigrid.mobilephone.state.SupervisorState;

/**
 * @author arturogarcia
 */
public class DataDownloadedGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SupervisorState ss = (SupervisorState) this.getAgent().getState();
        GridJobData d = (GridJobData) eventBESA.getData();
        ss.removeDownloadingFile(d.getDataId());
    }
}
