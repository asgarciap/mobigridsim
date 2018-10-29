package mobigrid.gridserver.behavior;

import BESA.Kernell.Agent.Event.EventBESA;
import BESA.Kernell.Agent.GuardBESA;

/**
 * Guard executed when a new grid job needs to be
 * added to the simulation.
 * This guard triggers the dispatcher algorithm to
 * select the best mobile node to execute this job
 * and then dispatch it in order to start
 * the execution of the job in the grid.
 * @author arturogarcia
 */
public class AddJobGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA eventBESA) {

    }
}
