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
   public static Map<Integer, Integer> Data = new TreeMap<Integer, Integer>();

   public static int address = 0x00400000;
   public static int lastPC = 0x00400000;
   public static int dataAddress = 0x10010000;
   public static int firstData;

   public void addInstruction(Integer instruction, int lineNum) {
      Instructions.put(address, instruction);
      address += 4;
   }

   public void addData(Integer data, int lineNum) {
      Data.put(dataAddress, data);
      dataAddress += 4;
   }
   
   public static void addDataToMem(Integer data, int address) {
	      Data.put(address, data);
   }

   public static int getCurrentPC(){
      return address;
   }

   public static int getCurrentDataAddr(){
      return dataAddress;
   }

   public static int getlastPC(){
      lastPC = address - 4;
      return lastPC;
   }

   public static Integer getInstruction(int PC) {
      return Instructions.get(PC);
   }
   
   public static Integer getData(int address) {
	      return Data.get(address);
	   }

   public void setFirstData(int lineNum){
      firstData = lineNum;
   }
}
