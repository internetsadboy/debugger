package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class WriteCode extends ByteCode{
    @Override
    public void execute(VirtualMachine vm) {
        System.out.println(vm.write()); 
    }

    @Override
    public void init(Vector<String> args) {}
    
    @Override
    public String toString(){
        return ("WRITE");
    }
}