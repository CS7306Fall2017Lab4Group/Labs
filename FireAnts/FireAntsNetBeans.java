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
                  machineInstructions.add(parseJType(line));                                 
              }
              
              //r type
              else if (Arrays.asList(rInstructions).contains(line.substring(0,3))) {
                  machineInstructions.add(parseRType(line));
              }
              
              //i type
              else if (Arrays.asList(iInstructions).contains(line.substring(0,3))) {
                  machineInstructions.add(parseIType(line));
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
    
    private static String parseJType(String instruction) {  
        //j instruction binary format:        
        //address(26), opcode(6)
        //possible instructions
        //j address
        
        return "J " + instruction;
    }
    
    private static String parseRType(String instruction) {
        //R instruction binary format:        
        //function(6), shamt(5), rd(5), rs(5), rt(5), opcode(6)
        //possible instructions (spaces between arguments optional)
        //function and shamt will always be all 0
        //add $rd, $rs, $rt - opcode=10
        //sub $rd, $rs, $rt - opcode=20
        //slt $rd, $rs, $rt - opcode=30
        //and $rd, $rs, $rt - opcode=40
        //nor $rd, $rs, $rt - opcode=50
        return "R " + instruction;
    }
    
    private static String parseIType(String instruction) {
        //i instruction binary format:        
        //offset(16), rs(5), rt(5), opcode(6)
        //possible instructions (spaces between arguments optional)
        //lwd $rt, offset($rs) - opcode=35
        //swd $rt, offset($rs) - opcode=43
        //beq $rt, $rs, offset - opcode=2
        
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
    
    private static String IntToBinary(int num, int bits) {
        //convert an interger to binary and pad to a specific number of bits
        String binString = Integer.toBinaryString(num);
        while (binString.length() < bits) {    //pad with 16 0's
            binString = "0" + binString;
        }
        return binString;
    }
    
    private static String BinaryToHex(String bin) {
        int decimal = Integer.parseInt(bin,2);
        String hexString = Integer.toString(decimal,16).toUpperCase();
        while (hexString.length() < 8) {
            hexString = "0" + hexString;
        }
        hexString = "0x" + hexString;
        
        return hexString;
    }
}

