package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.*;
import mobigrid.gridserver.behavior.AddJobGuard;
import mobigrid.gridserver.state.AdministratorState;
import mobigrid.mobilephone.behavior.DataDownloadedGuard;
import mobigrid.simulation.state.SimulationState;

/**
 * Periodic Guard to check simulator config and
 * sent job or node to the corresponding agent.
 * @author arturogarcia
 */
public class ProcessSimulationGuard extends PeriodicGuardBESA {

    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {

        ReportBESA.info("Avanzando simulacion de Downloads y Jobs");

        //Get the agent state
        SimulationState se = (SimulationState) this.getAgent().getState();


        AgHandlerBESA ah;

        se.processDownloads();
        se.processJobs();

        GridJobData data = se.getNextDataDownloaded();
        while(data != null) {
            EventBESA event = new EventBESA(DataDownloadedGuard.class.getName(), data);
            try {
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString());
                ah.sendEvent(event);
            }catch(ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
            data = se.getNextDataDownloaded();
        }

        AssignedJob job = se.getNextJobFinished();
        while(job != null) {

            //now we need no notify the dashboard than a new mobile node had been added
            EventBESA event = new EventBESA(ExecuteProgramSimulatedGuard.class.getName(), job);
            try {
                //get the dashboard agent handler
                ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SIMULATION.toString());
                //send to it the event
                ah.sendEvent(event);
            } catch (ExceptionBESA ex) {
                ReportBESA.error(ex);
            }
            job = se.getNextJobFinished();
        }

    }
}
