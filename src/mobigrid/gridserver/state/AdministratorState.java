package mobigrid.gridserver.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Administrator Agent State.
 * Contains the start time of the simulation
 * and the name of the configuration file
 * used to check if it's time to add a
 * mobile node or a job intto the grid simulator
 * @author arturogarcia
 */
public class AdministratorState extends StateBESA {

    private String NodesListFile;
    private String JobsListFile;
    private long SimulationTime; //Seconds
    private int TotalNodes;
    private int TotalJobs;

    public AdministratorState(String nodesListFile, String jobListFile) {
        SimulationTime = 0;
        NodesListFile = nodesListFile;
        JobsListFile = jobListFile;
        TotalJobs = 0;
        TotalNodes = 0;
    }

    //Advance simulation time by one second
    public void advanceSimulationTime() {
        SimulationTime++;
    }

    public long getSimulationTime() {
        return SimulationTime;
    }

    // Returns next Mobile Node List to add into the simulation
    public List<MobileNodeDescription> getNextNodesToAdd() {

        List<MobileNodeDescription> nodes = new ArrayList<>();
        try {
            //Opens config file and check if its time to add the mobile node into the simulation
            Scanner scanner = new Scanner(new File(NodesListFile));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //ignore comments
                if(line.startsWith("#")) continue;

                String[] nd = line.split(",");

                //ignore lines with wrong parameters
                if(nd.length != 7) continue;

                if(Integer.parseInt(nd[5]) == SimulationTime) {
                    MobileNodeDescription n = new MobileNodeDescription(
                            Integer.parseInt(nd[0]), //id
                            Float.parseFloat(nd[1]), //initialBatteryLevel
                            Float.parseFloat(nd[2]), //RAM (MB)
                            Float.parseFloat(nd[3]) // Disk Space (MB)
                    );

                    n.setDischargeRates(Float.parseFloat(nd[4]), 50f);

                    nodes.add(n);
                    TotalNodes++;
                }
            }
        }catch(FileNotFoundException ex) {
            //TODO Log Error
        }
        return nodes;
    }

    public List<JobDescription> getNextJobsToAdd() {
        List<JobDescription> jobs = new ArrayList<>();
        try {
            //Opens config file and check if its time to add the job into the simulation
            Scanner scanner = new Scanner(new File(JobsListFile));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //ignore comments
                if(line.startsWith("#")) continue;

                String[] jb = line.split(",");

                //ignore lines with wrong parameters
                if(jb.length != 5) continue;

                if(Integer.parseInt(jb[4]) == SimulationTime) {
                    JobDescription j = new JobDescription(
                            jb[0], //job id
                            Float.parseFloat(jb[1]), //program file size
                            Float.parseFloat(jb[2]), //Required RAM (MB)
                            Float.parseFloat(jb[3]) //Computational Time (MB)
                    );

                    jobs.add(j);
                    TotalJobs++;
                }
            }
        }catch(FileNotFoundException ex) {
            //TODO Log Error
        }
        return jobs;
    }

    public int getTotalNodes() {
        return TotalNodes;
    }

    public int getTotalJobs() {
        return TotalJobs;
    }
}
