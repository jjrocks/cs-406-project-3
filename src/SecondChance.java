import java.util.ArrayList;

/**
 * Created by JJ's Lappy on 5/3/2016.
 */
public class SecondChance implements ReplacementAlgorithm {

    ArrayList<Frame> frames;
    int currentProcess = 0;
    int maxFrameSize = 0;

    public SecondChance(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        frames = new ArrayList<>();
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
            if(process.isWrite()) {
                frames.get(frames.indexOf(frame)).process.setWrite(true);
            }
        } else {
            if (frames.size() < maxFrameSize) {
                frames.add(frame);
                currentProcess = frames.indexOf(frame);
                currentProcess = (currentProcess + 1) % maxFrameSize;
            } else {
                boolean frameFound = false;
                isReplacement = true;
                while (!frameFound) {
                    if (frames.get(currentProcess).overwrite) {
                        writeToMemory = frames.get(currentProcess).process.isWrite();
                        frames.set(currentProcess, frame);
                        frameFound = true;
                    } else {
                        frames.get(currentProcess).overwrite = true;
                    }
                    currentProcess = (currentProcess + 1) % maxFrameSize;
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
