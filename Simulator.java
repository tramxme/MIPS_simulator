import java.util.*;
import java.io.*;

public class Simulator {
   private static final String PROMPT = "Please enter s for single step, r for run or quit to exit\n";
   private static int PC = 0x00400000;
   private static int lastPC = Memory.getlastPC();

   public void run(){
      System.out.print(PROMPT);
      Scanner scan = new Scanner(System.in);
      String s = scan.next();
      int currPC = PC;
      Parser parser = new Parser();

      while(!s.equals("quit") && currPC < lastPC){
         if(s.equals("s")){
            System.out.printf("Instruction: 0x%08X\n", Memory.getInstruction(currPC));
            //System.out.println("Last PC " + lastPC);
            long start_time = System.currentTimeMillis();
            parser.parse(Memory.getInstruction(currPC));
            /*
             * ALU code goes here I guess...
             * The below if/else is to check the parser's work
             * Feel free to delete when ALU is implemented.
             */
            if (parser.type == Instruction.Type.RType) {
               System.out.println("RType instruction:");
               System.out.printf("Opcode: 0x%X Rs: 0x%X Rt: 0x%X Rd: 0x%X shamt: 0x%X funct: 0x%X\n",
                     parser.opcode, parser.rs, parser.rt, parser.rd, parser.shamt, parser.funct);
            }
            else if (parser.type == Instruction.Type.IType) {
               System.out.println("IType instruction:");
               System.out.printf("Opcode: 0x%X Rs: 0x%X Rt: 0x%X immediate: 0x%X\n",
                     parser.opcode, parser.rs, parser.rt, parser.immediate);
            }
            else {
               System.out.println("JType instruction:");
               System.out.printf("Opcode: 0x%X index: 0x%X\n",
                     parser.opcode, parser.index);
            }
            System.out.println("Time/instruction " + (System.currentTimeMillis() - start_time));
            currPC += 4;
         }
         else if(s.equals("r")){
            long start_time = System.currentTimeMillis();
            while (currPC != lastPC) {
               System.out.printf("Instruction: 0x%08X\n", Memory.getInstruction(currPC));
               parser.parse(Memory.getInstruction(currPC));
               /*
                * ALU code goes here too I guess...
                * The below if/else is to check the parser's work
                * Feel free to delete when ALU is implemented.
                */
               if (parser.type == Instruction.Type.RType) {
                  System.out.println("RType instruction:");
                  System.out.printf("Opcode: 0x%X Rs: 0x%X Rt: 0x%X Rd: 0x%X shamt: 0x%X funct: 0x%X\n",
                        parser.opcode, parser.rs, parser.rt, parser.rd, parser.shamt, parser.funct);
               }
               else if (parser.type == Instruction.Type.IType) {
                  System.out.println("IType instruction:");
                  System.out.printf("Opcode: 0x%X Rs: 0x%X Rt: 0x%X immediate: 0x%X\n",
                        parser.opcode, parser.rs, parser.rt, parser.immediate);
               }
               else {
                  System.out.println("JType instruction:");
                  System.out.printf("Opcode: 0x%X index: 0x%X\n",
                        parser.opcode, parser.index);
               }
               currPC += 4;
            }
            System.out.println("Time/instruction " + (System.currentTimeMillis() - start_time));
         }
         s = scan.next();
      }
      scan.close();
   }

   public int getNumberOfInstruction(){
      return 0;
   }
}
