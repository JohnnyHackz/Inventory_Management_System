package model;

/**
 * The InHouse class describes a part that is produced in-house.
 */
public class InHouse extends Part{

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * This is the machineId getter.
     * @return The machineId of the part.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * This is the machineId setter.
     * @param machineId The machineId to set.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
