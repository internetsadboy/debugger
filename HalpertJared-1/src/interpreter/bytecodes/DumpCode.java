package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class DumpCode extends ByteCode {
    private boolean isOn;
    
    @Override
    public void init(Vector<String> args) {
        if (args.firstElement().equals("ON"))
            isOn = true;
        else
            isOn = false;
    }
    
    public void execute(VirtualMachine vm) {
        vm.setDumpOn(isOn);
    }
}