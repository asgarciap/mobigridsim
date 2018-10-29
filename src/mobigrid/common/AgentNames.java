package mobigrid.common;

/**
 * Agents Names Enum.
 * @author arturogarcia
 */
public enum AgentNames {

    ADMINISTRATOR("ADMIN_AGENT"),
    DISPATCHER("DISPATCHER_AGENT"),
    MAIN_SUPERVISOR("MAIN_SUPERVISOR_AGENT"),
    DASHBOARD("DASHBOARD_AGENT"),
    SIMULATION("SIMULATION_AGENT"),
    REGISTER("REGISTER_AGENT"),
    SUPERVISOR("SUPERVISOR_AGENT"),
    DOWNLOADER("DOWNLOADER_AGENT"),
    EXECUTOR("EXECUTOR_AGENT");

    private final String Name;

    AgentNames(final String name) {
        this.Name = name;
    }
    public String toString() {
        return Name;
    }
}
