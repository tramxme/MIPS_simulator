import java.util.*;
import java.io.*;

public class Driver {
   public static void main(String[] args){
      File file = new File(args[0]);
      Scanner scanner = null;
      int lineNum = 0, InsNum = 0;
      ValidateAndWrite vaw;
      Label label;
      boolean valid = true;
      Memory mem = new Memory();
      boolean getData = false;
      boolean firstData = false;

      try {
         scanner = new Scanner(file);

         /* START FIRST PASS - Checking for labels */
         while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //Remove leading and ending spaces
            line = line.replaceAll("^\\s+|\\s+$","");

            //Strip comment
            if(line.contains("#")){
               line = line.replace(line.substring(line.indexOf("#"), line.length()), "");
            }

            //Only count line with either label or command
            if(line.length() > 0) {
               if(line.contains(".data")){
                  getData = true;
               }

               if(line.contains(":")){
                  String labelName = line.substring(0, line.indexOf(":"));
                  label = new Label(labelName, lineNum);

                  if(getData && (line.contains(".word") || line.contains(".bytes"))){
                     if(!firstData) {
                        mem.setFirstData(lineNum);
                        firstData = true;
                     }

                     String[] params = line.split("\\s+");
                     String val = params[2].trim();
                     int data = 0;
                     if(val.contains("0x")){
                        val = val.replace("0x", "");
                        data = Integer.parseInt(val, 16);
                     }
                     else {
                        data = Integer.parseInt(val);
                     }
                     mem.addData(data, lineNum);
                  }
               }
            }

            String temp = line.replace(line.substring(0,line.indexOf(":") + 1), "");
            temp = temp.replaceAll("^\\s+|\\s+$", "");
            if(temp.length() > 0) {
               lineNum++;
            }
         }
         /* END FIRST PASS */


         /* CREATE FILE TO WRITE BINARY CODE TO */
         scanner = new Scanner(file);
         vaw = new ValidateAndWrite(mem);

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

            if(command.contains(".data")){
               break;
            }

            if(command.length() > 0){
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
         System.out.println("Text Segment: " );
         for(Map.Entry<Integer, Integer> s : Memory.Instructions.entrySet()) {
            System.out.printf("Address 0x%8x - instruction 0x%8X\n", s.getKey(), s.getValue());
         }

         Simulator s = new Simulator();
         s.run();

      }catch(FileNotFoundException ex){
         System.out.println("File not found");
      }

   }
}
