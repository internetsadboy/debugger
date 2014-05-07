package interpreter.debugger.ui;

import interpreter.debugger.*;
import java.util.*;

public class DebugUI {

    private DebugVirtualMachine dvm;
    private Vector<Integer> listBreaks;
    private Vector<Source> src;
    private Scanner scanner;
    private int currentLine;
    private boolean firstPrint = true;
    private boolean innerFunc = false;
    private boolean wereSteppin = false;

    public DebugUI(DebugVirtualMachine dvm) {
      this.dvm = dvm;
      src = dvm.getLineCode();
      scanner = new Scanner(System.in);
      currentLine = 1;
      listBreaks = new Vector<Integer>();
    }

    public void dumpSource() {
        currentLine = dvm.getLine();
        int i = 0;
        System.out.println();
        for (Source line : src) {
            if (line.getIsBreakpoint()) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
            System.out.print(String.format("%2d", (src.indexOf(line) + 1)) + " ");
            if (currentLine == i + 1) {
                System.out.println(line.getSourceLine() + "         <------");
            } else {
                System.out.println(line.getSourceLine());
            }
            i++;
        }
        System.out.println();
        // Printing the Commands for the first and only time without being told
        if (firstPrint == true) {
          help();
          firstPrint = false;
        }
    }

    public void userCommand() {
        // command center
        System.out.println("type ? for help");
        System.out.print("> ");
        String thisLine = scanner.next();
        switch (thisLine) {
            case "sb":
                setBrk();
                userCommand();
                break;
            case "cb":
                clrBrk();
                userCommand();
                break;
            case "lb":
                listBrk();
                userCommand();
                break;
            case "df":
                displayFunc();
                System.out.println();
                userCommand();
                break;
            case "ce":
                dvm.executeProgram();
                break;
            case "qe":
                System.out.println("**** execution stopped ****");
                dvm.setIsRunning(false);
                break;
            case "dv":
                displayVars();
                userCommand();
                break;
            case "?":
                System.out.println();
                help();
                userCommand();
                break;
            case "so":
                dvm.setStep(true,false,false);
                wereSteppin = true;
                dvm.executeProgram();
                break;
            case "si":
                dvm.setStep(false,true,false);
                setInnerFunc(false);
                dvm.executeProgram();
                break;
            case "sv":
                dvm.setStep(false,false,true);
                //dvm.executeProgram();
                break;
            case "tr":
                dvm.setTrace(true);
                userCommand();
                break;
            case "to":
                dvm.setTrace(false);
                userCommand();
                break;
            case "ps":
                printStack();
                userCommand();
                break;
            default:
                System.out.println("\n**** ERROR: Invalid Command type \"?\" for list of Commands\n");
                userCommand();
                break;
        }
    }

    // Prints the command list
    public void help() {
        System.out.println("\n"
                + "------------------------- Commands -------------------------\n"
                + "     |?|    print help menu\n"
                + "     |sb|   set breakpoints   (e.g. sb 3 6)\n"
                + "     |cb|   clear breakpoints (e.g. cb 3 6)\n"
                + "     |lb|   list break points\n"
                + "     |df|   display current function\n"
                + "     |ce|   continue execution\n"
                + "     |qe|   quit execution\n"
                + "     |dv|   display vars in the current function\n"
                + "     |so|   step out of current function\n"
                + "     |si|   step into current function\n"
                + "     |sv|   step over current function\n" 
                + "     |tr|   set trace on\n" 
                + "     |to|   turn trace off\n"
                + "     |ps|   print call stack\n"
                + "------------------------------------------------------------\n\n");
    }

    // Sets all breakpoints, sends array of breakpoints to dvm
    // dvm deals with checking if valid breakpoint 
    void setBrk() {
        //Vector<Integer> breaks = new Vector<Integer>();
        String line = scanner.nextLine();
        String[] values = line.split("\\s+");
        int i = 1; 
        while(i < values.length) {
          listBreaks.add(Integer.parseInt(values[i]));
          i++;
        }
        if (!dvm.setBrks(listBreaks)) {
            System.out.println("\n**** One or more Breakpoint(s) are invalid. Try Again.\n\n"
                    + "Breakpoints can only be set at following instructions:\n"
                    + "  blocks, while, if, return, assign\n");
        } else {
            if(listBreaks.size() > 1) {
                System.out.print("Breakpoints set at: ");    
            } else {
                System.out.print("Breakpoint set at: ");
            }       
            for (int point : listBreaks) {
                System.out.print(point + " ");
            }
        }
        System.out.println("\n");
    }

    // Sends vector of breakpoints to dvm
    // dvm deals with clearing breakpoints
    public void clrBrk() {
        Vector<Integer> clrdBrks = new Vector<Integer>();
        String line = scanner.nextLine();
        String[] values = line.split("\\s+");
        int i = 1; 
        while(i < values.length) {
          clrdBrks.add(Integer.parseInt(values[i]));
          listBreaks.remove((Object)Integer.parseInt(values[i]));
          i++;
        }
        dvm.clrBrks(clrdBrks);
        if(clrdBrks.size() > 1) {
          System.out.print("Cleared breakpoints at: ");    
        } else {
            System.out.print("Cleared breakpoint at: ");
        }
        for (int point : clrdBrks) {
            System.out.print(point + " ");
        }
        System.out.println("\n");
    }

    // list current break points
    public void listBrk() {
      if(listBreaks.size() > 1) {
        System.out.print("Breakpoints set at: ");  
      } else {
        System.out.print("Breakpoint set at: ");
      }
      for(int i = 0; i < listBreaks.size(); i++) {
        System.out.print(listBreaks.get(i)+" ");
      }
      System.out.println("\n");
    }

    // Gets function lines from dvm
    // Prints source lines
    // if not in any function(s) should print entire program
    // currFunc -1 (corrects the first line being absent)
    public void displayFunc() {
        currentLine = dvm.getLine();
        // Print the current function we are in
        Vector<Integer> currentFunc = dvm.displayFunc();
        if (currentFunc != null) {
            int start = currentFunc.get(0);
            int end = currentFunc.get(1);
            int j = start;
            for (int i = start-1; i < end; i++) {
                String f = dvm.getFuncName().toLowerCase();
                if(f.equals("fib")) {
                    if(i < 9) {
                      if(src.get(i).getIsBreakpoint()) {
                        System.out.print("* "+j+" "+src.get(i).getSourceLine());  
                      } else {
                        System.out.print("  "+j+" "+src.get(i).getSourceLine()); 
                      }
                    } else {
                      if(src.get(i).getIsBreakpoint()) {
                        System.out.print(" *"+j+" "+src.get(i).getSourceLine()); 
                      } else {
                        System.out.print("  "+j+" "+src.get(i).getSourceLine());  
                      }
                    }
                    if(currentLine == i+1) {
                        System.out.println("         <------");
                    } else {
                        System.out.println();
                    }
                    j++;
                } 
                // non fib
                else {
                    if(i < 10) {
                      if(src.get(i).getIsBreakpoint()) {
                        System.out.print("* "+j+" "+src.get(i).getSourceLine());    
                      } else {
                        System.out.print("  "+j+" "+src.get(i).getSourceLine()); 
                      } 
                    } else {
                      if(src.get(i).getIsBreakpoint()) {
                        System.out.print(" *"+j+" "+src.get(i).getSourceLine()); 
                      } else {
                        System.out.print("  "+j+" "+src.get(i).getSourceLine());  
                      }
                    }    
                    if(currentLine == i+1) {
                      System.out.println("         <------");
                    } else {
                      System.out.println();
                    }
                    j++;
                }
                
            }
        } else {
            dumpSource();
        }
    }

    // Print all the vars currently initialized
    // Goes to dvm to deal with getting values
    void displayVars() {
        //Call on dvm method displayVars
        String[][] variables = dvm.displayVars();
        System.out.println();
        for (int i = 0; i < variables.length; i++) {
            System.out.println("ID: " + variables[i][0] + "     Value: " + variables[i][1]);
        }
        System.out.println();
    }

    public void setInnerFunc(boolean b) {
        innerFunc = b;
    }

    public void printStack() {
        System.out.println();
        Vector<String> callStack = dvm.printStack();
        for (int i = callStack.size() - 1; i > 0; i--) {
            if (i == callStack.size() - 1) {
                System.out.println(callStack.get(0));
            }
            System.out.println(callStack.get(i));

        }
        System.out.println("\nPrinted the current Call Stack.");
    }
}
