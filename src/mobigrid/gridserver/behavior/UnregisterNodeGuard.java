package mobigrid.gridserver.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.MobileNodeDescription;
import mobigrid.gridserver.state.DispatcherState;

/**
 * Register a new mobile node in the dispatcher.
 * @author arturogarcia
 */
public class UnregisterNodeGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        ReportBESA.info("Desregistrando nodo...");
        //Get the agent state
        DispatcherState dispatcherState = (DispatcherState) this.getAgent().getState();

        MobileNodeDescription mobileNodeDescription = (MobileNodeDescription) eventBESA.getData();

        dispatcherState.unregisterMobileNode(mobileNodeDescription);
    }
}
