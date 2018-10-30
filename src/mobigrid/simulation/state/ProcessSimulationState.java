package mobigrid.simulation.state;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.JobDescription;

import java.util.Map;

/**
 * @author arturogarcia
 */
public class ProcessSimulationState extends StateBESA {

    private MobilePhone MobilePhone;

    public ProcessSimulationState(MobilePhone phone) {
        MobilePhone = phone;
    }

    public boolean executeJob(JobDescription jobDescription) {
        Program program = new Program(jobDescription.getName(), jobDescription.getProgramFileSize(), (int) jobDescription.getComputationalTime());

        //Assign input files references to the program instance
        for(Map.Entry<String, Float> inputFile : jobDescription.getInputFiles().entrySet()) {
            program.addInputData(inputFile.getKey(), inputFile.getValue());
        }

        try {
            MobilePhone.installProgram(program);
            MobilePhone.executeProgram(program.getName());
        }catch(Exception ex) {
            ReportBESA.error(ex);
            return false;
        }
        return true;
    }

    public int getPhoneId() {
        return MobilePhone.getPhoneId();
    }

    public boolean downloadData(String dataId, float dataSize) {
        if(!MobilePhone.isDataDownloaded(dataId)) {
            boolean downloaded = false;
            while(!downloaded) {
                try {
                    MobilePhone.downloadData(dataId, dataSize);
                    downloaded = true;
                } catch (Exception ex) {
                    if(MobilePhone.eraseNextDataBuffer() == 0.0)
                        return false;
                }
            }
        }
        return true;
    }
}
