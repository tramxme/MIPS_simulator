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
      Registers regs = new Registers();
      int count = 0;

      while(!s.equals("quit") && currPC <= lastPC){
         if(s.equals("s")){
            //System.out.printf("Instruction: 0x%08X\n", Memory.getInstruction(currPC));
            //System.out.printf("PC: %d\n", currPC);
            	//System.out.println("Last PC " + lastPC);
            long start_time = System.nanoTime();
            parser.parse(Memory.getInstruction(currPC));
            
            currPC = ALU.runCom(parser, regs, lastPC, currPC);
            regs.printregs();
            /*
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
            */
            System.out.println("Time/instruction (ns/instruction): " + (System.nanoTime() - start_time));
            currPC += 4;
            count++;
         }
         else if(s.equals("r")){
            long start_time = System.nanoTime();
            while (currPC <= lastPC) {
            
               //System.out.printf("Instruction: 0x%08X\n", Memory.getInstruction(currPC));
               //System.out.printf("PC: %d\n", currPC);
               parser.parse(Memory.getInstruction(currPC));
               
               currPC = ALU.runCom(parser, regs, lastPC, currPC);
               //System.out.printf("PC: %d\n", currPC);
               regs.printregs();
               /*
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
               */
               currPC += 4;
               count++;
            }
            System.out.println("Total instructions: " + count + "\nTime/instruction (ns/instruction) " + (System.nanoTime() - start_time)/count);
         }
         s = scan.next();
      }
      scan.close();
   }
}
