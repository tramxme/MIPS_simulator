import java.util.*;
import java.io.*;

public class Label {
   public static Map<String, Integer> labelTable = new HashMap<String, Integer>();

   private String name;
   private int lineNum;

   public Label(String name, int lineNum) {
      this.name = name;
      this.lineNum = lineNum;
      labelTable.put(name, lineNum);
   }

   public static int getAddress(String name){
      return labelTable.get(name);
   }

   public static boolean hasLabel(String name) {
      return labelTable.containsKey(name);
   }
}
