import java.util.*;
import java.lang.*;

public class Optimal implements ReplacementAlgorithm {

	ArrayList<Process> memAccesses;
	ArrayList<Frame> frames;
	int maxFrames;
	int curProcess;

	public Optimal (ArrayList<Process> accesses, int numFrames) {
		memAccesses = accesses;
		frames = new ArrayList<Frame>(numFrames); 
		maxFrames = numFrames;
		curProcess = -1;
	}

	public Result execute(Process process, int timeStamp) {
		curProcess++;

		if(curProcess >= memAccesses.size()) {
			throw new IllegalStateException("Tried to run execute after sequence was already completed.");
		}

		int proc_id = process.getPid();
		int page_num = process.getPageNumber();

		Frame frame = new Frame(proc_id, page_num);

		int frameNum = frames.indexOf(frame);
		int numFrames = frames.size();

		if(frameNum > -1) {
			//no page fault
			return new Result(process, frameNum, false, false, false);
		}
		else if(numFrames < maxFrames) {
			//page fault, but there are empty frames
			frames.add(frame);
			return new Result(process, frameNum, true, false, false);
		}
		else {
			//page fault, and we must replace a frame
			int frameNumToReplace = -1;
			int farthestTimeUntilNextUse = -1;
			for(int i=0; i<numFrames; i++) {
				Frame f = frames.get(i);
				int nextUse = Integer.MAX_VALUE;
				for(int j=curProcess+1; j<memAccesses.size(); j++) {
					Process p = memAccesses.get(j);
					if(p.getPid() == f.procId && p.getPageNumber() == f.pageNum) {
						nextUse = j;
						break;
					}
				}
				if(nextUse > farthestTimeUntilNextUse) {
					frameNumToReplace = i;
					farthestTimeUntilNextUse = nextUse;
				}
			}
			frames.set(frameNumToReplace, frame);
			// TODO: This currently does not check for if the old frame needs to be written into memory.
			return new Result(process, frames.indexOf(frame), true, true, true);
		}
	}

	private class Frame {
		public int procId;
		public int pageNum;

		public Frame (int pid, int pg_num) {
			procId = pid;
			pageNum = pg_num;
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
	}
}