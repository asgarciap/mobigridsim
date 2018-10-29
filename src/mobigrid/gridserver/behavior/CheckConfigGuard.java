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

        //Check if it is time to add a new mobile node to the simulation
        MobileNodeDescription md = as.getNextNodeToAdd();
        while(md != null) {
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

            //check if there is another mobile node to sent..
            md = as.getNextNodeToAdd();
        }

        return;
        /*
        //now check if it is time to send a new job the grid simulation
        JobDescription j = as.getNextJobToAdd();

        while(j != null) {
            //this is pretty much the same as the mobile node.
            EventBESA event = new EventBESA(AddJobGuard.class.getName(), j);
            try {
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.DISPATCHER.toString());
                ah.sendEvent(event);
            }catch(ExceptionBESA ex) {
                ReportBESA.error(ex);
            }

            j = as.getNextJobToAdd();
        }*/
    }
}
