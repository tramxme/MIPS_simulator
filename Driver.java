import java.util.*;
import java.io.*;

public class Driver {
   public static void main(String[] args){
      File file = new File(args[0]);
      Scanner scanner = null;
      int lineNum = 0, InsNum = 0;
      ValidateAndWrite vaw = new ValidateAndWrite();
      Label label;
      boolean valid = true;
      ToBin toBin;

      try {
         scanner = new Scanner(file);

         /* START FIRST PASS - Checking for labels */
         while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //Remove leading and ending spaces
            line = line.replaceAll("^\\s+|\\s+$","");

            if(line.contains("#")){
               line = line.replace(line.substring(line.indexOf("#"), line.length()), "");
            }

            //Only count line with either label or command
            if(line.length() > 0) {
               if(line.contains(":")){
                  String labelName = line.substring(0, line.indexOf(":"));
                  label = new Label(labelName, lineNum);
               }
               lineNum++;
            }

         }
         /* END FIRST PASS */


         /* CREATE FILE TO WRITE BINARY CODE TO */
         scanner = new Scanner(file);

         /* START SECOND PASS - Generate object code */
         lineNum = -1;
         while(scanner.hasNextLine()&& valid){
            String line = scanner.nextLine();
            String command = line.replaceAll("^\\s+|\\s+$","");

            //Strip comment
            if(line.contains("#")){
               command = command.replace(line.substring(line.indexOf("#"), line.length()), "");
            }

            //Strip label
            if(command.contains(":")){
               command = command.replace(command.substring(0, command.indexOf(":") + 1),"");
            }


            if(command.length() > 0 && !command.contains(".data")){
               lineNum++;
               System.out.println("Line " + lineNum + ": " + line);
               if (!vaw.CheckSyntax(command, lineNum)) {
                  System.out.println("Line " + lineNum + ": This instruction is not valid");
                  break;
               }
            }
         }
         /* END SECOND PASS */

         vaw.closeFile(true);

         //Print out the label Table with their line number * 4
         System.out.println(Label.labelTable);

         //Get the memory with all the instructions
         //Print the instructions in binary
         /*
            int j = 0;
            for(Integer i : Memory.Instructions){
            System.out.printf("%d - instruction 0x%8X\n", j++, i);
            }
            */
         for(Map.Entry<Integer, Integer> s : Memory.Instructions.entrySet()) {
            System.out.printf("line %d - Address 0x%8x - instruction 0x%8X\n", s.getKey(), s.getKey(), s.getValue());
         }

         Simulator s = new Simulator();
         s.run();

      }catch(FileNotFoundException ex){
         System.out.println("File not found");
      }

   }
}
