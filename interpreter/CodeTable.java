package interpreter;

public class CodeTable {

    private static java.util.HashMap<String, String> codeTable = new java.util.HashMap<>();

    public static String get(String code) {
        return codeTable.get(code);
    }

    public static void init() {
        codeTable.put("HALT", "HaltCode");
        codeTable.put("POP", "PopCode");
        codeTable.put("FALSEBRANCH", "FalseBranchCode");
        codeTable.put("GOTO", "GoToCode");
        codeTable.put("STORE", "StoreCode");
        codeTable.put("LOAD", "LoadCode");
        codeTable.put("LIT", "LitCode");
        codeTable.put("ARGS", "ArgsCode");
        codeTable.put("CALL", "CallCode");
        codeTable.put("RETURN", "ReturnCode");
        codeTable.put("BOP", "BopCode");
        codeTable.put("READ", "ReadCode");
        codeTable.put("WRITE", "WriteCode");
        codeTable.put("LABEL", "LabelCode");
        codeTable.put("DUMP", "DumpCode");
        codeTable.put("LINE", "LineCode");
        codeTable.put("FORMAL", "FormalCode");
        codeTable.put("FUNCTION", "FunctionCode");
    }
    
    public static void debugInit(){
        codeTable.put("HALT", "HaltCode");
        codeTable.put("POP", "DebugPopCode");
        codeTable.put("FALSEBRANCH", "FalseBranchCode");
        codeTable.put("GOTO", "GoToCode");
        codeTable.put("STORE", "StoreCode");
        codeTable.put("LOAD", "LoadCode");
        codeTable.put("LIT", "DebugLitCode");
        codeTable.put("ARGS", "ArgsCode");
        codeTable.put("CALL", "DebugCallCode");
        codeTable.put("RETURN", "DebugReturnCode");
        codeTable.put("BOP", "BopCode");
        codeTable.put("READ", "ReadCode");
        codeTable.put("WRITE", "WriteCode");
        codeTable.put("LABEL", "LabelCode");
        codeTable.put("LINE", "LineCode");
        codeTable.put("FORMAL", "FormalCode");
        codeTable.put("FUNCTION", "FunctionCode");
    }
}
