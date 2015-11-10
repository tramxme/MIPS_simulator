import java.io.*;
import java.util.*;

public class Memory {
   /*
   public static List<Integer> Instructions = new ArrayList<Integer>();
   public static int address = 0;

   public void addInstruction(Integer instruction, int lineNum) {
      Instructions.add(instruction);
      //address += 4;
   }
   */

   public static Map<Integer, Integer> Instructions = new TreeMap<Integer, Integer>();
   public static int address = 0x00400000;
   public static int lastPC = 0x00400000;

   public void addInstruction(Integer instruction, int lineNum, boolean data) {
      Instructions.put(address, instruction);
      if (data){
         lastPC = address - 4;
      }
      address += 4;
   }

   public static int getCurrentPC(){
      return address;
   }

   public static int getlastPC(){
      return lastPC;
   }

   public static Integer getInstruction(int PC) {
      return Instructions.get(PC);
   }
}
