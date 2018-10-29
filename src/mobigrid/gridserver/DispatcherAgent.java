package mobigrid.gridserver;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;

public class DispatcherAgent extends AgentBESA {

    public DispatcherAgent(String alias, StateBESA state, StructBESA struct, double passorwd) throws KernellAgentExceptionBESA {
        super(alias, state, struct, passorwd);
    }

    public void setupAgent() {

    }

    public void shutdownAgent() {

    }

}
