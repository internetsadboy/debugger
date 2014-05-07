package interpreter;

import java.util.*;

public class RunTimeStack {

    Stack<Integer> framePointers;
    Vector<Integer> runStack;

    public RunTimeStack() {
        framePointers = new Stack<>();
        runStack = new Vector<>();
        framePointers.push(0);
    }

    public void dump() {
        Iterator<Integer> dumpFrames = framePointers.iterator();
        int i = 0;
        if ((framePointers.size()) == 1) {
            System.out.print("[");
            while (i < (runStack.size() - 1)) {
                System.out.print(runStack.get(i++) + ",");
            }
            System.out.print(runStack.get(i) + "]");
        } else {
            dumpFrames.next();
            while (dumpFrames.hasNext()) {
                System.out.print("[");
                int j = dumpFrames.next() - 1;
                while (i < j) {
                    System.out.print(runStack.get(i++) + ",");
                }
                System.out.print(runStack.get(j) + "]");
                i++;
            }
            System.out.print("[");
            while (i < (runStack.size() - 1)) {
                System.out.print(runStack.get(i++) + ",");
            }
            System.out.print(runStack.get(i) + "]");
        }
        System.out.println();
    }

    public int peek() {
        return runStack.get(runStack.size() - 1);
    }

    public int push(int value) {
        runStack.add(value);
        return value;
    }

    public Integer push(Integer value) {
        runStack.add(value);
        return value;
    }

    public int pop() {
        return runStack.remove(runStack.size() - 1);
    }

    public void newFrameAt(int offset) {
        framePointers.push(runStack.size() - offset);
    }

    public void popFrame() {
        int top = (runStack.size() - 1);
        Integer poppedTop = runStack.remove(top);
        top--;
        Integer frame = framePointers.pop();
        for (int i = top; i >= frame; i--) {
            runStack.remove(i);
        }
        runStack.add(poppedTop);
    }

    public int store(int offset) {
        int storedValue = runStack.remove(runStack.size() - 1);
        runStack.set(offset + framePointers.peek(), storedValue);
        return storedValue;
    }

    public int load(int offset) {
        runStack.add(runStack.get(offset + framePointers.peek()));
        return runStack.get(offset + framePointers.peek());
    }

    public Vector<Integer> getArgs() {
        int frameIndex = framePointers.peek();
        Vector<Integer> args = new Vector<>();

        while (frameIndex < runStack.size()) {
            int var = runStack.get(frameIndex);
            args.add(var);
            frameIndex++;
        }

        return args;
    }

    public String getValue(int position) {
        if (position == 0) {
            return runStack.get(position).toString();
        } else {
            return runStack.get(position - 1).toString();
        }
    }
}
