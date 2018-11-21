package mobigrid.dashboard.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.state.SimulationDashboard;

/**
 * Guard executed when there is an update in the
 * mobile node list.
 * This could be because a new node had been added
 * to the simulation or a mobile node already added to the
 * grid had changed its state.
 * This guard is used to update the dashboard.
 * @author arturogarcia
 */
public class UpdateNodesStatusGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {

        ReportBESA.info("Actualizando informacion de nodo en Dasboard");
        //Get the agent state
        SimulationDashboard sd = (SimulationDashboard) this.getAgent().getState();
        //Get mobile node updated
        MobileNodeDescription node = (MobileNodeDescription) eventBESA.getData();

        //Update dashboard
        if(node != null) {
            sd.updateMobileNode(node);
            ReportBESA.info("Nuevo estado de nodo: "+node.getId()+" - "+node.getState());
        }
    }
}
