package mobigrid.gridserver;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;

/**
 * Mobile Execution Node Agent.
 * @author arturogarcia
 */
public class MainSupervisorAgent extends AgentBESA {

    public MainSupervisorAgent(String alias, StateBESA state, StructBESA struct, double passorwd) throws KernellAgentExceptionBESA {
        super(alias, state, struct, passorwd);
    }

    public void setupAgent() {

    }

    public void shutdownAgent() {

    }

}
