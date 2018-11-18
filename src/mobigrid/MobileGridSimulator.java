package mobigrid;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.KernellAgentExceptionBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.Agent.StructBESA;
import BESA.Kernell.System.AdmBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import mobigrid.common.AgentNames;
import mobigrid.dashboard.DashboardAgent;
import mobigrid.dashboard.behavior.UpdateJobStatusGuard;
import mobigrid.dashboard.behavior.UpdateNodesStatusGuard;
import mobigrid.dashboard.state.SimulationDashboard;
import mobigrid.gridserver.AdministratorAgent;
import mobigrid.gridserver.DispatcherAgent;
import mobigrid.gridserver.MainSupervisorAgent;
import mobigrid.gridserver.behavior.*;
import mobigrid.gridserver.state.AdministratorState;
import mobigrid.gridserver.state.DispatcherState;
import mobigrid.gridserver.state.MainSupervisorState;
import mobigrid.simulation.SimulationAgent;
import mobigrid.simulation.behavior.*;
import mobigrid.simulation.state.SimulationState;

import static java.lang.Thread.sleep;

/**
 * Main class
 * @author arturogarcia
 *
 */
public class MobileGridSimulator {
	
    public static void main(String[] args) {

        AdmBESA admLocal = AdmBESA.getInstance();

        //Simulation Agent
        SimulationState ss = new SimulationState();
        StructBESA simStruct = new StructBESA();
        simStruct.addBehavior("AddMobileBehaviorSim");
        simStruct.addBehavior("SimulateDownloadBehavior");
        simStruct.addBehavior("SimulateExecuteProgramBehavior");

        //Dashboard Agent
        SimulationDashboard sd = new SimulationDashboard();
        StructBESA dashboardStruct = new StructBESA();
        dashboardStruct.addBehavior("DashboardBehavior");

        //Administrator Agent
        AdministratorState as = new AdministratorState("./nodelist.csv", "./joblist.csv");
        StructBESA adminStruct = new StructBESA();
        adminStruct.addBehavior("AdminBehavior");

        //Main Supervisor Agent
        MainSupervisorState mss = new MainSupervisorState();
        StructBESA supervisorStruct = new StructBESA();
        supervisorStruct.addBehavior("UpdateJobStatusBehavior");

        //Dispatcher Agent
        DispatcherState dispatcherState = new DispatcherState();
        StructBESA dispatcherStruct = new StructBESA();
        dispatcherStruct.addBehavior("DispatcherJobBehavior");
        dispatcherStruct.addBehavior("DispatcherNodeBehavior");
        dispatcherStruct.addBehavior("DispatcherDispatchJobsBehavior");

        try {
            simStruct.bindGuard("AddMobileBehaviorSim", AddMobileNodeGuard.class);
            simStruct.bindGuard("SimulateDownloadBehavior", SimulateDownloadDataGuard.class);
            simStruct.bindGuard("SimulateDownloadBehavior", DownloadDataSimulatedGuard.class);
            simStruct.bindGuard("SimulateExecuteProgramBehavior", SimulateExecuteProgramGuard.class);
            simStruct.bindGuard("SimulateExecuteProgramBehavior", ExecuteProgramSimulatedGuard.class);
            SimulationAgent simAgent = new SimulationAgent(AgentNames.SIMULATION.toString(), ss, simStruct, 0.91);
            simAgent.start();

            dashboardStruct.bindGuard("DashboardBehavior", UpdateNodesStatusGuard.class);
            dashboardStruct.bindGuard("DashboardBehavior", UpdateJobStatusGuard.class);
            DashboardAgent dashboardAgent = new DashboardAgent(AgentNames.DASHBOARD.toString(), sd, dashboardStruct, 0.91);
            dashboardAgent.start();

            adminStruct.bindGuard("AdminBehavior", CheckConfigGuard.class);
            AdministratorAgent adminAgent = new AdministratorAgent(AgentNames.ADMINISTRATOR.toString(), as, adminStruct, 0.91);
            adminAgent.start();

            supervisorStruct.bindGuard("UpdateJobStatusBehavior", ReportJobStatusGuard.class);
            MainSupervisorAgent mainSupervisorAgent = new MainSupervisorAgent(AgentNames.MAIN_SUPERVISOR.toString(), mss, supervisorStruct, 0.91);
            mainSupervisorAgent.start();

            dispatcherStruct.bindGuard("DispatcherJobBehavior", AddJobGuard.class);
            dispatcherStruct.bindGuard("DispatcherNodeBehavior", RegisterNodeGuard.class);
            dispatcherStruct.bindGuard("DispatcherNodeBehavior", UnregisterNodeGuard.class);
            dispatcherStruct.bindGuard("DispatcherDispatchJobsBehavior", DispatchJobsGuard.class);
            DispatcherAgent dispatcherAgent = new DispatcherAgent(AgentNames.DISPATCHER.toString(), dispatcherState, dispatcherStruct, 0.91);
            dispatcherAgent.start();

            //Now sends a periodic event to check configuration every 1 seconds
            PeriodicDataBESA data = new PeriodicDataBESA(1000, 100, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA checkConfigEvent = new EventBESA(CheckConfigGuard.class.getName(), data);
            AgHandlerBESA ah = admLocal.getHandlerByAlias(AgentNames.ADMINISTRATOR.toString());
            ah.sendEvent(checkConfigEvent);

            //Send another periodic event to check if we need to dispatch jobs in the dispatcher
            PeriodicDataBESA dataDispatch = new PeriodicDataBESA(1000, 100, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA dispatchJobsEvent = new EventBESA(DispatchJobsGuard.class.getName(), dataDispatch);
            AgHandlerBESA ahd = admLocal.getHandlerByAlias(AgentNames.DISPATCHER.toString());
            ahd.sendEvent(dispatchJobsEvent);
        }
        catch(ExceptionBESA e) {
            ReportBESA.fatal(e);
        }
    }
}
