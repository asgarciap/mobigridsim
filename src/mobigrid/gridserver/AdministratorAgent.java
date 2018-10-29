package mobigrid.gridserver;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;

/**
 * Administrator Agent
 * Receives a Periodical Event to check if it's time
 * to send a new Job or add new mobile node to the
 * simulated grid.
 * @author arturogarcia
 */
public class AdministratorAgent extends AgentBESA {

    public AdministratorAgent(String alias, StateBESA state, StructBESA struct, double password) throws KernellAgentExceptionBESA {
        super(alias, state, struct, password);
    }

    public void setupAgent() {

    }

    public void shutdownAgent() {

    }

}
