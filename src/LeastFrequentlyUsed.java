import java.util.*;

public class LeastFrequentlyUsed implements ReplacementAlgorithm {

    ArrayList<Frame> frames;
    int maxFrames = 0;

    public LeastFrequentlyUsed (int numFrames) {
        maxFrames = numFrames;
        frames = new ArrayList<>(maxFrames);
    }

    @Override
    public Result execute(Process process, int timeStep) {
        
        int proc_id = process.getPid();
        int page_num = process.getPageNumber();

        Frame frame = new Frame(proc_id, page_num, process.isWrite(), timeStep);

        int frameNum = frames.indexOf(frame);
        int numFrames = frames.size();

        if(frameNum > -1) {

            //no page fault
            if(process.isWrite()) {
                frames.get(frameNum).write = true;
            }
            frames.get(frameNum).timesUsed += 1;
            frames.get(frameNum).lastUsed = timeStep;
            return new Result(process, frameNum, false, false, false);

        }
        else if(numFrames < maxFrames) {

            //page fault, but there are empty frames
            frames.add(frame);
            return new Result(process, numFrames, true, false, false);

        } else {
                
            //page fault, so we gotta find what to replace
            int frameNumToReplace = -1;
            int leastOftenUsed = Integer.MAX_VALUE;
            int lastUsed = Integer.MAX_VALUE;
            for(int i=0; i<numFrames; i++) {
                Frame f = frames.get(i);
                if(f.timesUsed < leastOftenUsed || 
                    (f.timesUsed == leastOftenUsed && f.lastUsed < lastUsed)) {
                    frameNumToReplace=i;
                    leastOftenUsed = f.timesUsed;
                    lastUsed = f.lastUsed;
                }
            }

            boolean needWrite = frames.get(frameNumToReplace).write;
            frames.set(frameNumToReplace, frame);
            return new Result(process, frameNumToReplace, true, true, needWrite);
            
        }
    }

    private class Frame {
        public int procId;
        public int pageNum;
        public boolean write;
        public int timesUsed;
        public int lastUsed; //this is used to break ties for tiemsUsed

        public Frame (int pid, int pg_num, boolean w, int timeUsed) {
            procId = pid;
            pageNum = pg_num;
            write = w;
            timesUsed = 1;
            lastUsed = timeUsed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Frame frame = (Frame) o;

            if(this.procId == frame.procId && this.pageNum == frame.pageNum)
            {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            String str = "["+procId+","+pageNum+",";
            str += write ? "W]" : "R";
            str += ","+timesUsed+","+lastUsed+"]";
            return str;
        }
    }
}
