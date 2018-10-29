package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.simulation.state.SimulationState;

/**
 * Guard executed when a new mobile node needs to be
 * added to the simulation.
 * This result in a new Mobile Phone object created
 * and maintained by the Simulation Agent, but also
 * a set of agents than will control this new mobile
 * phone.
 * The agents: Supervisor, Downloader and Executor
 * are created when this guard is executed.
 * @author arturogarcia
 */
public class AddMobileNodeGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SimulationState se = (SimulationState) this.getAgent().getState();

        //Get MobileNode description
        MobileNodeDescription node = (MobileNodeDescription) eventBESA.getData();

        //add it to the simulation
        se.addMobileNode(node);

        AgHandlerBESA ah;

        //now we need no notify the dashboard than a new mobile node had been added
        EventBESA event = new EventBESA(UpdateNodesStatusGuard.class.getName(), node);
        try {
            //get the dashboard agent handler
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DASHBOARD.toString());
            //send to it the event
            ah.sendEvent(event);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
