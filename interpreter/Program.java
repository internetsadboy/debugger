package interpreter;

import java.util.*;
import interpreter.bytecodes.*;

public class Program {
    private static ArrayList<ByteCode> byteCodeList;
    private static HashMap<String,Integer> addrTable = new HashMap<>();
    
    public Program(){
        byteCodeList = new ArrayList<>();
    }
    
    public static void addByteCode(ByteCode byteCode){
        byteCodeList.add(byteCode);
    }
    
    public static void hashAddress(Vector<String> label, int position){
        addrTable.put(label.get(0), position);
    }
    
    public static void changeAddr(){
        ByteCode byteCode;
        
        for(int i = 0; i < byteCodeList.size() ; i++ ) {
            byteCode = byteCodeList.get(i);
            
            if(byteCode instanceof FalseBranchCode){
                ((FalseBranchCode) byteCode).setAddr(addrTable.get(((FalseBranchCode) byteCode).getLabel()));
            }
            if(byteCode instanceof CallCode){
                ((CallCode) byteCode).setAddr(addrTable.get(((CallCode) byteCode).getLabel()));
            }
            if(byteCode instanceof GoToCode){
                ((GoToCode) byteCode).setAddr(addrTable.get(((GoToCode) byteCode).getLabel()));
            }
        }
    }
    
    public ByteCode getCode(int pc){
        return byteCodeList.get(pc);
    }

    // check is bp is valid before the dvm sets it
    public boolean isValidBrk(int breakP){
        ByteCode temp;
        for(int i = 0 ; i < byteCodeList.size() ; i++){
            temp = byteCodeList.get(i);
            if(temp instanceof LineCode){
                int line = ((LineCode)temp).getLineNum();
                if(breakP == line)
                    return true;
            }
        }
        return false;
    }
}
