/**
 * Created by JJ's Lappy on 5/1/2016.
 */
public class Process {

    private int pid;
    private int address;
    private int pageNumber;
    private boolean write;

    public Process(int pid, int address, boolean write) {
        this.pid = pid;
        this.address = address;
        this.write = write;
        this.pageNumber = address/FrameLoader.PAGE_SIZE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Process process = (Process) o;
        return pid == process.pid && process.pageNumber == pageNumber;

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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    @Override
    public int hashCode() {
        return pid;
    }
}
