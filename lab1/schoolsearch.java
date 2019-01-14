import java.util.*;
import java.io.*;


public class schoolsearch {

   public static class Student {
      String[] attributes;

      public Student(String[] attributes) {
         this.attributes = attributes;
      }
   }

   public static Student createStudent(String line) {
      String[] attributes = line.split(",", 8);
      return new Student(attributes);
   }

   public static void printStudent(Student s, boolean[] printFlags) {
      for (int i = 0; i < 8; i++) {
         if (printFlags[i] && i == 0){
            System.out.print(s.attributes[i]);
         }
         else if (printFlags[i] && i > 0){
            System.out.print(", " + s.attributes[i]);
         }
      }
      System.out.println("");
   }

   public static void search(ArrayList<Student> students, int key, String name, boolean[] printFlags) {
      boolean notFound = true;

      for (Student s: students) {
         if (s.attributes[key].equals(name)){
            notFound = false;
            printStudent(s, printFlags);
         }
      }
      if (notFound)
         System.out.println("search not found");
   }

   // searches for High/Low GPA
   public static void searchGPA(ArrayList<Student> students, String grade, int gpa) {
      ArrayList<Student> results = new ArrayList<Student>();
      boolean notFound = true;
      double low = 100;
      double high = -1;

      for (Student s: students) {
         if (s.attributes[2].equals(grade)){
            notFound = false;
            results.add(s);
            double currGPA = Double.parseDouble(s.attributes[5]);
            if (currGPA > high)
               high = currGPA;
            if (currGPA < low)
               low = currGPA;
         }
      }

      if (notFound) {
         System.out.println("search not found");
      }
      else {
         //Search by Low GPA
         if (gpa == 0) {
            search(results, 5, Double.toString(low), new boolean[]{true, true, false, false, true, true, true, true});
         }
         else {
            search(results, 5, Double.toString(high), new boolean[]{true, true, false, false, true, true, true, true});
         }
      }
   }

   public static void averageGPA(ArrayList<Student> students, String grade) {
      boolean notFound = true;
      double numStudents = 0;
      double currSum = 0;

      for (Student s: students) {
         if (s.attributes[2].equals(grade)){
            notFound = false;
            currSum += Double.parseDouble(s.attributes[5]);
            numStudents++;
         }
      }
      if (notFound) {
         System.out.println("search not found");
      }
      else {
         double avg = Math.round((currSum/numStudents) * 100.0) / 100.0;
         System.out.println("Grade: " + grade + " Average GPA: " + Double.toString(avg));
      }
   }

   public static void displayInfo(ArrayList<Student> students) {
      HashMap<String, Integer> map = new HashMap<String, Integer>();
      for (Student s: students) {
         String currGrade = s.attributes[2];
         if (map.containsKey(currGrade)) {
            map.put(currGrade, map.get(currGrade) + 1);
         }
         else {
            map.put(currGrade, 1);
         }
      }
      System.out.println("<Grade>: <Number of Students>");
      for (int i = 0; i < 7; i++) {
         if (map.containsKey(Integer.toString(i))) {
            System.out.println(i + ": " + map.get(Integer.toString(i)));
         }
         else {
            System.out.println(i + ": 0");
         }
      }
   }

   public static void searchStudents(String input, ArrayList<Student> students) {
      String[] splitInput = input.split("[\\s:]+");
      System.out.println("\n----------RESULTS----------");
      //S[tudent]: <lastname>
      if ((splitInput[0].equals("S") || splitInput[0].equals("Student"))) {
         //B[us]
         if (splitInput.length == 3 && (splitInput[2].equals("B") || splitInput[2].equals("Bus"))) {
            search(students, 0, splitInput[1], new boolean[]{true, true, false, false, true, false, false, false});
         }
         else if (splitInput.length == 2) {
            search(students, 0, splitInput[1], new boolean[]{true, true, true, true, false, false, true, true});
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //T[eacher]: <lastname>
      else if ((splitInput[0].equals("T") || splitInput[0].equals("Teacher"))) {
         if (splitInput.length == 2) {
            search(students, 6, splitInput[1], new boolean[]{true, true, false, false, false, false, false, false});
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //G[rade]: <Number>
      else if ((splitInput[0].equals("G") || splitInput[0].equals("Grade"))) {
         if (splitInput.length == 2) {
            search(students, 2, splitInput[1], new boolean[]{true, true, false, false, false, false, false, false});
         }
         //H[igh]
         else if (splitInput.length == 3 && (splitInput[2].equals("H") || splitInput[2].equals("High"))) {
            searchGPA(students, splitInput[1], 1);
         }
         //L[ow]
         else if (splitInput.length == 3 && (splitInput[2].equals("L") || splitInput[2].equals("Low"))) {
            searchGPA(students, splitInput[1], 0);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //B[us]: <Number>
      else if ((splitInput[0].equals("B") || splitInput[0].equals("Bus"))) {
         if (splitInput.length == 2) {
            search(students, 4, splitInput[1], new boolean[]{true, true, true, true, false, false, false, false});
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //A[verage]: <Number>
      else if ((splitInput[0].equals("A") || splitInput[0].equals("Average"))) {
         if (splitInput.length == 2) {
            averageGPA(students, splitInput[1]);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //I[nfo]
      else if ((splitInput[0].equals("I") || splitInput[0].equals("Info"))) {
         if (splitInput.length == 1) {
            displayInfo(students);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      else {
         System.out.println("INVALID COMMAND. Please try again :(");
      }
   }

   public static void main(String []args) throws FileNotFoundException {
      try {
         File file = new File("./students.txt");
         Scanner input = new Scanner(file);
         ArrayList<Student> students = new ArrayList<Student>();
         String next;
         String prompt = "S[tudent]: <lastname> [B[us]]\nT[eacher]: <lastname>\nB[us]: <number>\nG[rade]: <number> [H[igh]|L[ow]]\nA[verage]: <number>\nI[nfo]\nQ[uit]\n\nPlease select an option (Last names must be typed in ALL CAPS): ";

         while (input.hasNextLine()) {
            Student s = createStudent(input.nextLine());
            students.add(s);
         }
         System.out.print(prompt);
         Scanner userInput = new Scanner(System.in);
         while (!(next=userInput.nextLine()).equals("Q") && !next.equals("Quit")) {
            searchStudents(next, students);
            System.out.print("---------------------------\n\n" + prompt);
         }


      }

      catch (FileNotFoundException e) {
         System.out.println(e.getMessage());
      }

   }
}
