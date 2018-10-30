package mobigrid.simulation;

import BESA.Kernell.Agent.AgentBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Kernell.Agent.StructBESA;

public class ProcessSimulationAgent extends AgentBESA {

    public ProcessSimulationAgent(String alias, StateBESA state, StructBESA struct, double password) throws KernellAgentExceptionBESA {
        super(alias, state, struct, password);
    }

    public void setupAgent() {

    }

    public void shutdownAgent() {

    }
}
