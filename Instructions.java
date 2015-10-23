import java.util.*;
import java.io.*;

public class Instructions {

   public static Map<String, Integer> opCodes = new HashMap<String, Integer>();

   public Instructions(){
      AddOpCode();
   }

   public static void AddOpCode() {
      opCodes.put("add", 0x20);
   }
}
