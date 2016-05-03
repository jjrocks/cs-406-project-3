/**
 * Created by JJ's Lappy on 5/1/2016.
 */
public class Process {

    private int pid;
    private int address;
    private boolean write;

    public Process(int pid, int address, boolean write) {
        this.pid = pid;
        this.address = address;
        this.write = write;
    }
}
