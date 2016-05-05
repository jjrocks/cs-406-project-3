import java.util.*;
import java.lang.*;
import java.util.stream.Collectors;

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
		String listString = frames.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));

		curProcess++;

		if(curProcess >= memAccesses.size()) {
			throw new IllegalStateException("Tried to run execute after sequence was already completed.");
		}

		int proc_id = process.getPid();
		int page_num = process.getPageNumber();

		Frame frame = new Frame(proc_id, page_num, process.isWrite());

		int frameNum = frames.indexOf(frame);
		int numFrames = frames.size();

		if(frameNum > -1) {
			//no page fault
			if(process.isWrite()) {
				frames.get(frameNum).write = true;
			}
			return new Result(process, frameNum, false, false, false);
		}
		else if(numFrames < maxFrames) {
			//page fault, but there are empty frames
			frames.add(frame);
			return new Result(process, numFrames, true, false, false);
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

			boolean needWrite = frames.get(frameNumToReplace).write;
			frames.set(frameNumToReplace, frame);
			return new Result(process, frameNumToReplace, true, true, needWrite);
		}
	}

	private class Frame {
		public int procId;
		public int pageNum;
		public boolean write;

		public Frame (int pid, int pg_num, boolean w) {
			procId = pid;
			pageNum = pg_num;
			write = w;
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
        	str += write ? "W]" : "R]";
        	return str;
        }
	}
}