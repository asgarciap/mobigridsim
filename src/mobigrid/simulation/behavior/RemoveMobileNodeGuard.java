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
import mobigrid.common.NodeStateEnum;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.gridserver.behavior.RegisterNodeGuard;
import mobigrid.gridserver.behavior.UnregisterNodeGuard;
import mobigrid.mobilephone.DownloaderAgent;
import mobigrid.mobilephone.ExecutorAgent;
import mobigrid.mobilephone.SupervisorAgent;
import mobigrid.mobilephone.behavior.*;
import mobigrid.mobilephone.state.DownloaderState;
import mobigrid.mobilephone.state.ExecutorState;
import mobigrid.mobilephone.state.SupervisorState;
import mobigrid.simulation.ProcessSimulationAgent;
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
public class RemoveMobileNodeGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        //Get the agent state
        SimulationState se = (SimulationState) this.getAgent().getState();

        //Get MobileNode description
        MobileNodeDescription node = (MobileNodeDescription) eventBESA.getData();

        //add it to the simulation
        se.removeMobileNode(node);

        AgHandlerBESA ah;

        node.setState(NodeStateEnum.DISCONNECTED);

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

        try {
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DOWNLOADER.toString()+node.getId());
            ah.getAg().shutdownAgent();
            getAgent().getAdmLocal().unregisterAgent(AgentNames.DOWNLOADER.toString() + node.getId());

            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString()+node.getId());
            ah.getAg().shutdownAgent();
            getAgent().getAdmLocal().unregisterAgent(AgentNames.SUPERVISOR.toString() + node.getId());

            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.EXECUTOR.toString()+node.getId());
            ah.getAg().shutdownAgent();
            getAgent().getAdmLocal().unregisterAgent(AgentNames.EXECUTOR.toString() + node.getId());
        }catch(ExceptionBESA exb) {
            ReportBESA.error(exb);
        }

        //now we need no notify the dispatcher than a new mobile node had been added
        EventBESA eventDispatcher = new EventBESA(UnregisterNodeGuard.class.getName(), node);
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
