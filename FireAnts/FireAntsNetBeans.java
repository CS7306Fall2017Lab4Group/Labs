/*
 * FireAnts assembler program
 * CS 7306
 * Lab 2 lab team 4
 * Brad Vick, Jared Palmer, William Miller
 */
package fireantsnetbeans;

    import java.io.*;
    import java.util.*;

public class FireAntsNetBeans {
@SuppressWarnings("unchecked")
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           
      try {
          
          //open the assembly code file passed in
          BufferedReader br = new BufferedReader(new FileReader(args[0]));
          StringBuilder sb = new StringBuilder();
          String line = br.readLine();
          
          //new array list to hold the machine language instructions
          ArrayList machineInstructions = new ArrayList();
          
          //arrays for the different instructions 
          String[] rInstructions = {"add", "sub", "slt", "and", "nor"};
          String[] iInstructions = {"lwd", "swd", "beq"};
                    
          //loop over each line of the file
          while (line !=null) {
              //determine instruction type and parse that instruction
              
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
                            
              //read next line in source file
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
        
        int opcode = 2;
        int address = Integer.parseInt(instruction.substring(1).trim());
        return IntToBinary(address, 26) + IntToBinary(opcode, 6);
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
        int rd = 0;
        int rs = 0;
        int rt = 0;
        int opcode = 0;
        
        //determine opcode
        if (instruction.substring(0,3).equals("add"))
            opcode=10;
        else if ((instruction.substring(0,3).equals("sub")))
            opcode=20;
        else if ((instruction.substring(0,3).equals("slt")))
            opcode=30;
        else if ((instruction.substring(0,3).equals("and")))
            opcode=40;
        else if ((instruction.substring(0,3).equals("nor")))
            opcode=50;
        
        //split the arguments
        String[] instructionArguments = instruction.substring(3).split(",");
        rd = Integer.parseInt(instructionArguments[0].trim().substring(1));
        rs = Integer.parseInt(instructionArguments[1].trim().substring(1));
        rt = Integer.parseInt(instructionArguments[2].trim().substring(1));
        
         return "00000000000" + IntToBinary(rd, 5) + IntToBinary(rs, 5) + IntToBinary(rt, 5) + IntToBinary(opcode, 6);
                        
    }
    
    private static String parseIType(String instruction) {
        //i instruction binary format:        
        //offset(16), rs(5), rt(5), opcode(6)
        //possible instructions (spaces between arguments optional)
        //lwd $rt, offset($rs) - opcode=35
        //swd $rt, offset($rs) - opcode=43
        //beq $rt, $rs, offset - opcode=2
        int offset = 0;
        int rs = 0;
        int rt = 0;
        int opcode = 0;
        
                //determine opcode
        if (instruction.substring(0,3).equals("lwd"))
            opcode=35;
        else if ((instruction.substring(0,3).equals("swd")))
            opcode=43;
        else if ((instruction.substring(0,3).equals("beq")))
            opcode=2;
        
        if (instruction.contains("(")) {
            // lwd and swd have ()
            String[] instructionArguments = instruction.substring(3).split(",");            
            rt = Integer.parseInt(instructionArguments[0].trim().substring(1));
            String tempInstruction = instructionArguments[1].replace("(", ",").replace(")", ",");
            String[] OffsetRs = tempInstruction.split(",");
            offset = Integer.parseInt(OffsetRs[0].trim());
            rs = Integer.parseInt(OffsetRs[1].trim().substring(1));
                                       
        }
            else
            //beq no ()
        {
            //split the arguments
            String[] instructionArguments = instruction.substring(3).split(",");
            rt = Integer.parseInt(instructionArguments[0].trim().substring(1));
            rs = Integer.parseInt(instructionArguments[1].trim().substring(1));
            offset = Integer.parseInt(instructionArguments[2].trim());  
        }

        
         return IntToBinary(offset, 16) + IntToBinary(rs, 5) + IntToBinary(rt, 5) + IntToBinary(opcode, 6);
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
            out.println(BinaryToHex(machineInstructions.get(i).toString()));
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

