package mobigrid.mobilephone;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;

/**
 * Mobile Execution Node Agent.
 * @author arturogarcia
 */
public class SupervisorAgent extends AgentBESA {

    public SupervisorAgent(String alias, StateBESA state, StructBESA struct, double passwd) throws KernellAgentExceptionBESA {
        super(alias, state, struct, passwd);
    }

    public void setupAgent() {

    }

    public void shutdownAgent() {

    }

}
