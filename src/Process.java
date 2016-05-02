/**
 * Created by JJ's Lappy on 5/1/2016.
 */
public class Process {

    int pid;
    int address;
    boolean write;

    public Process(int pid, int address, boolean write) {
        this.pid = pid;
        this.address = address;
        this.write = write;
    }
}
