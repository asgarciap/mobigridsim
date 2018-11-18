package mobigrid.gridserver.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import mobigrid.common.MobileNodeDescription;
import mobigrid.gridserver.state.DispatcherState;

/**
 * Register a new mobile node in the dispatcher.
 * @author arturogarcia
 */
public class UnregisterNodeGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        DispatcherState dispatcherState = (DispatcherState) this.getAgent().getState();
            AgHandlerBESA ah;

            MobileNodeDescription mobileNodeDescription = (MobileNodeDescription) eventBESA.getData();

            dispatcherState.unregisterMobileNode(mobileNodeDescription);
    }
}
