package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class CallCode extends ByteCode {
   
    private String label;
    private Integer target;
    private Vector argValues;
    
    @Override
    public void execute(VirtualMachine vm) {
        vm.changeAddr(target);
        argValues = vm.getArgs();
    }

    @Override
    public void init(Vector<String> args) {
        label = args.get(0);
    }
    
    public String getLabel(){
        return label;
    }
    
    public void setAddr(Integer addr){
        target = addr;
    }
    
    @Override
    public String toString() {
        String returnLabel;
        String returnStr;
        int index = label.indexOf('<');
        if (index != -1)
            returnLabel = label.substring(0, index);
        else
            returnLabel = label;
        
        returnStr = "CALL "+label+"   "+returnLabel+"(";
        int i = 0;
        if (!argValues.isEmpty()) {
            returnStr += argValues.get(i);
        }
        ++i;
        for (; i < argValues.size(); ++i) {
            returnStr += "," + argValues.get(i);
        }
        
        returnStr += ")";
        return returnStr;
    }
}