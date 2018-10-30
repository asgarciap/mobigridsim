package mobigrid.simulation.state;

import BESA.Kernell.Agent.StateBESA;
import mobigrid.common.MobileNodeDescription;
import mobigrid.dashboard.state.SimulationDashboard;
import mobigrid.mobilephone.SupervisorAgent;
import mobigrid.mobilephone.state.SupervisorState;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the current state of the simulation.
 * It has access to the mobile nodes
 * that are part of the grid.
 * @author arturogarcia
 */
public class SimulationState extends StateBESA {

    private List<MobilePhone> MobilePhones;

    public SimulationState() {
        MobilePhones = new ArrayList<>();
    }

    public void addMobileNode(MobileNodeDescription nodeDescription) {
        MobilePhone mp = new MobilePhone(nodeDescription);
        MobilePhones.add(mp);
    }

    public List<MobileNodeDescription> getMobileNodesStatus() {
        List<MobileNodeDescription> md = new ArrayList<>();
        for (MobilePhone mp : MobilePhones) {
            MobileNodeDescription mdn = mp.getCurrentStatus();
            md.add(mdn);
        }
        return md;
    }

    public MobileNodeDescription getMobileNodeStatus(int id) {
        MobileNodeDescription r = null;
        for (MobilePhone p: MobilePhones) {
            if(p.getPhoneId() == id) {
                r = new MobileNodeDescription(p.getCurrentStatus());
            }
        }
        return r;
    }

    public MobilePhone getMobilePhoneById(int id) {
        for(MobilePhone phone : MobilePhones) {
            if(phone.getPhoneId() == id)
                return phone;
        }
        return null;
    }
}
