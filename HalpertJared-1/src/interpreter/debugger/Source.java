package interpreter.debugger;

public class Source {
    
    private String sourceLine;
    private boolean isBreakpoint;
    
    public Source(){
      sourceLine = null;
      isBreakpoint = false;
    }
    
    public void setSourceLine(String sourceLine){
      this.sourceLine = sourceLine;
    }
    
    public String getSourceLine(){
        return this.sourceLine;
    }
    
    public void isBreakpoint(Boolean on){
        isBreakpoint = on;
    }
    
    public boolean getIsBreakpoint(){
        return isBreakpoint;
    }
    
}
