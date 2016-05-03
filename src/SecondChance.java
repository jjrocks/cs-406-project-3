import java.util.ArrayList;

/**
 * Created by JJ's Lappy on 5/3/2016.
 */
public class SecondChance implements ReplacementAlgorithm {

    ArrayList<Process> processes;
    ArrayList<Frame> frames;
    int currentProcess = 0;
    int frameSize = 0;

    public SecondChance(ArrayList<Process> processes, int frameSize) {
        processes = new ArrayList<>();
        this.frameSize = frameSize;
    }

    @Override
    public Result execute(Process process, int timestamp) {
        Frame frame = new Frame(process);
        if (frames.size() < frameSize) {
            frames.add(frame);
            return null;
        } else {
            if (frames.contains(frame)) {
                frame.overwrite = false;
                return null;
            } else {
                boolean frameFound = false;
                while (!frameFound) {

                }
            }
        }
        return null;
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
