package mobigrid.gridserver.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.AgentNames;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;
import mobigrid.gridserver.state.AdministratorState;
import mobigrid.simulation.behavior.AddMobileNodeGuard;
import mobigrid.simulation.behavior.RemoveMobileNodeGuard;

/**
 * Periodic Guard to check simulator config and
 * sent job or node to the corresponding agent.
 * @author arturogarcia
 */
public class CheckConfigGuard extends PeriodicGuardBESA {

    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        //Get the agent state
        AdministratorState as = (AdministratorState) this.getAgent().getState();

        AgHandlerBESA ah;

        //simulation step (1 second)
        as.advanceSimulationTime();

        //Check if it is time to add a new mobile node to the simulation
        for(MobileNodeDescription md : as.getNextNodesToAdd()) {
            //ok, it is time to add a new node...
            //create an event to send the data
            EventBESA event = new EventBESA(AddMobileNodeGuard.class.getName(), md);
            try {
                //get the handler of the simulation agent (there is only one of this)
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());

                //send the event. this will result in a new mobile node created in the simulation.
                ah.sendEvent(event);
            }catch(ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

        ReportBESA.info("Verificando si hay nodos para eliminar de la simulacion");
        //Check if it is time to remove an existing mobile node from the simulation
        for(MobileNodeDescription md : as.getNextNodesToRemove()) {
            ReportBESA.info("Nodo con Id: "+md.getId()+" marcando para eliminarse en tiempo: "+as.getSimulationTime());
            //ok, it is time to remove a node...
            //create an event to send the data
            EventBESA event = new EventBESA(RemoveMobileNodeGuard.class.getName(), md);
            try {
                //get the handler of the simulation agent (there is only one of this)
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());

                //send the event.
                ah.sendEvent(event);
            }catch(ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
        }

        if(as.getTotalNodes() >= 1) {
            //now check if it is time to send a new job the grid simulation
            for(JobDescription j : as.getNextJobsToAdd()) {
                ReportBESA.info("Enviando nuevo Job al Dispatcher. JobId: "+j.getName());
                //this is pretty much the same as the mobile node.
                EventBESA event = new EventBESA(AddJobGuard.class.getName(), j);
                try {
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DISPATCHER.toString());
                    ah.sendEvent(event);
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
            }
        }
    }
}
