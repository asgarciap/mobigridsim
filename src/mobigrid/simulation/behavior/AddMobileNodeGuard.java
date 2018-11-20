package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.Agent.StructBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.gridserver.behavior.RegisterNodeGuard;
import mobigrid.mobilephone.DownloaderAgent;
import mobigrid.mobilephone.ExecutorAgent;
import mobigrid.mobilephone.SupervisorAgent;
import mobigrid.mobilephone.behavior.*;
import mobigrid.mobilephone.state.DownloaderState;
import mobigrid.mobilephone.state.ExecutorState;
import mobigrid.mobilephone.state.SupervisorState;
import mobigrid.simulation.ProcessSimulationAgent;
import mobigrid.simulation.state.MobilePhone;
import mobigrid.simulation.state.ProcessSimulationState;
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

        //now we need no notify the dispatcher than a new mobile node had been added
        EventBESA eventDispatcher = new EventBESA(RegisterNodeGuard.class.getName(), node);
        try {
            //get the dispatcher agent handler
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DISPATCHER.toString());
            //send to it the event
            ah.sendEvent(eventDispatcher);
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }

    }
}
