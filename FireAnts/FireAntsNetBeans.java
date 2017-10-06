/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fireantsnetbeans;

    import java.io.*;
    import java.util.*;
/**
 *
 * @author bwvick
 */
public class FireAntsNetBeans {
@SuppressWarnings("unchecked")
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           
      try {
          //open the assembly code file
          BufferedReader br = new BufferedReader(new FileReader(args[0]));
          StringBuilder sb = new StringBuilder();
          String line = br.readLine();
          
          ArrayList machineInstructions = new ArrayList();
          
          String[] rInstructions = {"add", "sub", "slt", "and", "nor"};
          String[] iInstructions = {"lwd", "swd", "beq"};
          
          
          //loop over each line of the file
          while (line !=null) {
              //determine instruction type
              
              //j type
              if (line.substring(0,1).equals("j")) {
                  machineInstructions.add(processJType(line));                                 
              }
              
              //r type
              else if (Arrays.asList(rInstructions).contains(line.substring(0,3))) {
                  machineInstructions.add(processRType(line));
              }
              
              //i type
              else if (Arrays.asList(iInstructions).contains(line.substring(0,3))) {
                  machineInstructions.add(processIType(line));
              }
                                 
              line=br.readLine();
          }
          
          //create binary .o file
          OutputOFile(machineInstructions, args[0].replace(".asm", ".o"));
          
          //create hex .psd file
          OutputPSDFile(machineInstructions, args[0].replace(".asm", ".psd"));
          
                        
      } catch (IOException e) {
          e.printStackTrace();
      }
   
      
    }
    
    private static String processJType(String instruction) {  
        
        return "J " + instruction;
    }
    
    private static String processRType(String instruction) {
        
        return "R " + instruction;
    }
    
    private static String processIType(String instruction) {
        
        return "I " + instruction;
    }
    
    private static void OutputOFile(ArrayList machineInstructions, String FileName)
    {     
        try {
            PrintWriter out = new PrintWriter(FileName);
            for (int i = 0; i < machineInstructions.size(); i++) {
                out.print(machineInstructions.get(i));
            }
            out.close();
            } catch (IOException e) {
          e.printStackTrace();
      }
    }
    
    private static void OutputPSDFile(ArrayList machineInstructions, String FileName) 
    {
        try {
        PrintWriter out = new PrintWriter(FileName);
        for (int i = 0; i < machineInstructions.size(); i++) {
            out.println(machineInstructions.get(i));
        }
        out.close();
        } catch (IOException e) {
      e.printStackTrace();
      } 
    }
}

