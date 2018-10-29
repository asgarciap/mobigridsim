package mobigrid.dashboard;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;
import mobigrid.dashboard.state.SimulationDashboard;

/**
 * Dashboard Agent.
 * @author arturogarcia
 */
public class DashboardAgent extends AgentBESA {

    public DashboardAgent(String alias, StateBESA state, StructBESA struct, double password) throws KernellAgentExceptionBESA {
        super(alias, state, struct, password);
    }

    public void setupAgent() {
        SimulationDashboard sd = (SimulationDashboard) getState();
        sd.run();
    }

    public void shutdownAgent() {

    }

}
