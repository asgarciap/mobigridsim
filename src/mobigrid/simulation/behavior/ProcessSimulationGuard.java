package mobigrid.simulation.behavior;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.PeriodicGuardBESA;
import BESA.Kernell.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.*;
import mobigrid.gridserver.behavior.AddJobGuard;
import mobigrid.gridserver.state.AdministratorState;
import mobigrid.mobilephone.behavior.DataDownloadAbortedGuard;
import mobigrid.mobilephone.behavior.DataDownloadedGuard;
import mobigrid.mobilephone.behavior.JobPausedGuard;
import mobigrid.simulation.state.SimulationState;

import java.util.List;

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

        synchronized (se) {
            AgHandlerBESA ah;

            ReportBESA.info("Total Nodos: "+se.getTotalPhones()+" - Descargas activas: " + se.getTotalDownloads() + " - Jobs en ejecucion: " + se.getTotalJobs());

            ReportBESA.info("Eliminando Nodos desconectados y sus trabajos y descargas asociadas");
            List<GridJobData> erasedDownloads = se.cleanOrphanDownloads();
            List<AssignedJob> erasedJobs = se.cleanOrphanJobs();
            int erasedNodes = se.cleanDisconnectedPhones();
            ReportBESA.info("Descargas abortadas: "+erasedDownloads.size()+" - Jobs abortados: "+erasedJobs.size()+" - Nodos eliminados(desconectados): "+erasedNodes);

            se.processDownloads();
            se.processJobs();

            GridJobData data = se.getNextDataDownloaded();
            while (data != null) {
                ReportBESA.info("Descarga de datos finalizada. DataId: " + data.getDataId());
                EventBESA event = new EventBESA(DataDownloadedGuard.class.getName(), data);
                try {
                    ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString());
                    ah.sendEvent(event);
                } catch (ExceptionBESA ex) {
                    ReportBESA.error(ex);
                }
                data = se.getNextDataDownloaded();
            }

            AssignedJob job = se.getNextJobFinished();
            while (job != null) {
                ReportBESA.info("Ejecucion de Job finalizado. JobId: " + job.getJobDescription().getName());
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

            //Notifico al supervisor los jobs pausados para que sean reasignados
            if(se.isColaborationEnabled()) {
                ReportBESA.info("Notificando descargas no finalizadas para su reasignacion");
                for(GridJobData gridJobData : erasedDownloads) {
                    EventBESA event = new EventBESA(DataDownloadAbortedGuard.class.getName(), gridJobData);
                    try {
                        ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString());
                        ah.sendEvent(event);
                    } catch (ExceptionBESA ex) {
                        ReportBESA.error(ex);
                    }
                }

                ReportBESA.info("Notificando trabajos no finalizados para su reasignacion");
                for(AssignedJob assignedJob : erasedJobs) {
                    EventBESA event = new EventBESA(JobPausedGuard.class.getName(), assignedJob.getJobDescription());
                    try {
                        ah = getAgent().getAdmLocal().getHandlerByAlias(AgentNames.SUPERVISOR.toString());
                        ah.sendEvent(event);
                    } catch (ExceptionBESA ex) {
                        ReportBESA.error(ex);
                    }
                }
            }
        }
    }
}
