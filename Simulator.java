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

      while(!s.equals("quit") && PC != lastPC){
         if(s.equals("s")){
            System.out.printf("Instruction: 0x%08X\n", Memory.getInstruction(currPC));
            System.out.println("Last PC " + lastPC);
            long start_time = System.currentTimeMillis();

            System.out.println("Time/instruction " + (System.currentTimeMillis() - start_time));
            currPC += 4;
         }
         else if(s.equals("r")){
            long start_time = System.currentTimeMillis();
            System.out.println("Time/instruction " + (System.currentTimeMillis() - start_time));
         }
         s = scan.next();
      }
   }

   public int getNumberOfInstruction(){
      return 0;
   }
}
