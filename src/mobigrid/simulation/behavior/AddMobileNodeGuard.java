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


        DownloaderState downloaderState = new DownloaderState(node.getId());
        StructBESA downStruct = new StructBESA();
        downStruct.addBehavior("DownloaderBehavior");

        ExecutorState executorState = new ExecutorState(node.getId());
        StructBESA execStruct = new StructBESA();
        execStruct.addBehavior("ExecutorBehavior");

        SupervisorState supervisorState = new SupervisorState(node.getId());
        StructBESA supStruct = new StructBESA();
        supStruct.addBehavior("SupervisorBehavior");
        supStruct.addBehavior("SupervisorDownloaderBehavior");
        supStruct.addBehavior("SupervisorExecutorBehavior");
        supStruct.addBehavior("SupervisorCheckJobsBehavior");

        ProcessSimulationState processSimulationState = new ProcessSimulationState(se.getMobilePhoneById(node.getId()));
        StructBESA processStruct = new StructBESA();
        processStruct.addBehavior("ProcessSimulationBehavior");

        try {
            //now we need to create agents related to this mobile node

            //Downloader Agent
            downStruct.bindGuard("DownloaderBehavior", DownloadDataGuard.class);
            DownloaderAgent downloaderAgent = new DownloaderAgent(AgentNames.DOWNLOADER.toString()+node.getId(),downloaderState,downStruct,0.91);
            downloaderAgent.start();

            //Executor Agent
            execStruct.bindGuard("ExecutorBehavior", ExecuteJobGuard.class);
            ExecutorAgent executorAgent = new ExecutorAgent(AgentNames.EXECUTOR.toString()+node.getId(),executorState,execStruct,0.91);
            executorAgent.start();

            //Supervisor Agent
            supStruct.bindGuard("SupervisorBehavior", DispatchJobGuard.class);
            supStruct.bindGuard("SupervisorDownloaderBehavior", DataDownloadedGuard.class);
            supStruct.bindGuard("SupervisorExecutorBehavior", JobExecutedGuard.class);
            supStruct.bindGuard("SupervisorCheckJobsBehavior", CheckJobsGuard.class);
            SupervisorAgent supervisorAgent = new SupervisorAgent(AgentNames.SUPERVISOR.toString()+node.getId(), supervisorState, supStruct, 0.91);
            supervisorAgent.start();

            //Process Simulation Agent
            processStruct.bindGuard("ProcessSimulationBehavior", ProcessSimulateExecuteProgramGuard.class);
            processStruct.bindGuard("ProcessSimulationBehavior", ProcessSimulateDownloadDataGuard.class);
            ProcessSimulationAgent processSimulationAgent = new ProcessSimulationAgent(AgentNames.PROCESS_SIMULATION.toString()+node.getId(), processSimulationState, processStruct, 0.91);
            processSimulationAgent.start();

            //Now, start a periodic event in the supervisor agent.
            PeriodicDataBESA data = new PeriodicDataBESA(1000, 100, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA checkJobsEvent = new EventBESA(CheckJobsGuard.class.getName(), data);
            ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString()+node.getId());
            ah.sendEvent(checkJobsEvent);
        }catch(ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
    }
}
