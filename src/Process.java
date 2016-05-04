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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Process process = (Process) o;
        return pid == process.pid && address/FrameLoader.PAGE_SIZE == address/FrameLoader.PAGE_SIZE;

    }

    public int getPid() {
        return pid;
    }

    public int getAddress() {
        return address;
    }

    public boolean isWrite() {
        return write;
    }

    @Override
    public int hashCode() {
        return pid;
    }
}
