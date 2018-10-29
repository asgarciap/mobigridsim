package mobigrid.gridserver.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;

import java.util.List;

/**
 * Administrator Agent State.
 * Contains the start time of the simulation
 * and the name of the configuration file
 * used to check if it's time to add a
 * mobile node or a job intto the grid simulator
 * @author arturogarcia
 */
public class AdministratorState extends StateBESA {

    private String ConfigFileName;
    private long StartTime;
    private int TotalNodes;
    private int TotalJobs;

    public AdministratorState(String configFileName) {
        StartTime = System.currentTimeMillis()/1000;
        ConfigFileName = configFileName;
        TotalJobs = 0;
        TotalNodes = 0;
    }

    // Returns next Mobile Node to add into the simulator.
    public MobileNodeDescription getNextNodeToAdd() {

        long currentTime = System.currentTimeMillis()/1000;

        if(currentTime - StartTime >= 0 && TotalNodes == 0) {
            TotalNodes++;
            return new MobileNodeDescription(1,100, 4000, 200000);
        }
        if(currentTime - StartTime >= 10 && TotalNodes == 1) {
            TotalNodes++;
            return new MobileNodeDescription(2,80, 2000, 100000);
        }
        if(currentTime - StartTime >= 15 && TotalNodes == 2) {
            TotalNodes++;
            return new MobileNodeDescription(3,90, 4000, 100000);
        }

        if(currentTime - StartTime >= 30 && TotalNodes == 3) {
            TotalNodes++;
            return new MobileNodeDescription(4,70, 3000, 200000);
        }

        return null;
    }

    public JobDescription getNextJobToAdd() {
        long currentTime = System.currentTimeMillis()*1000;

        if(currentTime - StartTime >= 0 && TotalJobs == 0) {
            TotalJobs++;
            return new JobDescription("Job1", 1000f,4000f,500f);
        }

        if(currentTime - StartTime >= 10 && TotalJobs == 1) {
            TotalJobs++;
            return new JobDescription("Job2",200f,4000f,500f);
        }

        if(currentTime - StartTime >= 20 && TotalJobs == 2) {
            TotalJobs++;
            return new JobDescription("Job3",2000f,4000f,500f);
        }

        if(currentTime - StartTime >= 30 && TotalJobs >= 3 && TotalJobs <= 22) {
            TotalJobs++;
            return new JobDescription("Job"+TotalJobs,1500f,4000f,500f);
        }

        return null;
    }
}
