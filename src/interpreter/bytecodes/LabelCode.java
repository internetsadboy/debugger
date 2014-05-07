package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class LabelCode extends ByteCode{
    String label;
    
    @Override
    public void execute(VirtualMachine vm) {}

    @Override
    public void init(Vector<String> args) {
        label = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("LABEL" + " " + label);
    }
}