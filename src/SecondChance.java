import java.util.ArrayList;

/**
 * Created by JJ's Lappy on 5/3/2016.
 */
public class SecondChance implements ReplacementAlgorithm {

    ArrayList<Process> processes;
    ArrayList<Frame> frames;
    int currentProcess = 0;
    int frameSize = 0;

    public SecondChance(int frameSize) {
        processes = new ArrayList<>();
        this.frameSize = frameSize;
    }

    @Override
    public Result execute(Process process, int timestamp) {
        Frame frame = new Frame(process);
        boolean pageFault = true;
        boolean isReplacement = false;
        boolean writeToMemory = false;
        if (frames.contains(frame)) {
            pageFault = false;
            frame.overwrite = false;
        } else {
            if (frames.size() < frameSize) {
                frames.add(frame);
            } else {
                boolean frameFound = false;
                while (!frameFound) {

                    currentProcess = currentProcess++ % frameSize;
                }
            }
        }
        return new Result(process, frames.indexOf(frame), pageFault, isReplacement, writeToMemory);
    }

    private class Frame {
        Process process;
        boolean overwrite = false;

        public Frame(Process process) {
            this.process = process;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Frame frame = (Frame) o;

            return process.equals(frame.process);

        }

        @Override
        public int hashCode() {
            return process.hashCode();
        }
    }
}
