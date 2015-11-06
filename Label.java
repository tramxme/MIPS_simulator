import java.util.*;
import java.io.*;

public class Label {

   public static Map<String, Integer> labelTable = new HashMap<String, Integer>();

   public void addLabel(String name, Integer lineNum) {
      labelTable.put(name, lineNum);
   }
}
