package mobigrid.simulation.state;

import BESA.ExceptionBESA;
import BESA.Kernell.Agent.StateBESA;
import BESA.Log.ReportBESA;
import mobigrid.common.JobDescription;
import mobigrid.common.MobileNodeDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author arturogarcia
 */
public class ProcessSimulationState extends StateBESA {

    private Map<Integer, MobilePhone> MobilePhones;
    private int SimulationTime;

    public ProcessSimulationState() {
        MobilePhones = new HashMap<>();
        SimulationTime = 0;
    }

    public void addMobilePhone(MobilePhone phone) {
        if(!MobilePhones.containsKey(phone.getPhoneId()))
            MobilePhones.put(phone.getPhoneId(), phone);
    }

    public void advanceSimulation() {
        SimulationTime++;
    }

    public MobileNodeDescription getMobilePhoneStatus(int phoneId) {
        MobilePhone phone = MobilePhones.getOrDefault(phoneId, null);
        return phone != null ? phone.getCurrentStatus() : null;
    }
}
