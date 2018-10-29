package mobigrid.mobilephone;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;

/**
 * Mobile Execution Node Agent.
 * @author arturogarcia
 */
public class ExecutorAgent extends AgentBESA {

    public ExecutorAgent(String alias, StateBESA state, StructBESA struct, double password) throws KernellAgentExceptionBESA {
        super(alias, state, struct, password);
    }

    public void setupAgent() {

    }

    public void shutdownAgent() {

    }

}
