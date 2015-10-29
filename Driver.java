import java.util.*;
import java.io.*;

public class Driver {
   public static void main(String[] args){
      File file = new File(args[0]);
      Scanner scanner = null;
      Integer lineNum = 0;
      Validate validate;
      Label label = new Label();
      boolean valid = true;
      ToBin toBin;
      
      try {
         scanner = new Scanner(file);

         //First pass - Checking for labels
         while(scanner.hasNextLine()) {
            lineNum++;
            String line = scanner.nextLine();
            if(line.contains(":")){
               String labelName = line.substring(0, line.indexOf(":"));
               label.addLabel(labelName, lineNum);
            }
         }

         System.out.println(label.labelTable);

         //Second pass - Generate object code
         lineNum = 0;
         scanner = new Scanner(file);
         if (args.length > 1){
        	 toBin = new ToBin(args[1]);
         }
         else {
        	 toBin = new ToBin();
         }
         //Checking each line
         while(scanner.hasNextLine()&& valid){
            lineNum++;
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
               System.out.println("command: " + command);
               validate = new Validate(command);
               if(!(valid = validate.CheckSyntax())){
                  System.out.println("Line " + lineNum + ": this instruction is not valid");
               }
               else{
            	   toBin.writeToBin(command);
               }
            }
         }
         toBin.closeFile(valid);
      }catch(FileNotFoundException ex){
        System.out.println("File not found");
      }

   }
}
