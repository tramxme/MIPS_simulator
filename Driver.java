import java.util.*;
import java.io.*;

public class Driver {
   public static void main(String[] args){
      File file = new File(args[0]);
      Scanner scanner = null;
      Validate validate;

      try {
         scanner = new Scanner(file);

         //Checking each line
         while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String command = line;

            if(line.contains(":") && line.contains("#")){
               command = line.substring(line.indexOf(":") + 1, line.indexOf("#"));
            }
            else if(line.contains("#")) {
                command = line.substring(0, line.indexOf("#"));
            }
            else if(line.contains(":")){
                command = line.substring(line.indexOf(":") + 1, line.length());
            }

            if(command.length() > 0){
               validate = new Validate(command);
               System.out.println("command: " + command);
            }
         }
      }catch(FileNotFoundException ex){
        System.out.println("File not found");
      }

   }
}
