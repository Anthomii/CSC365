import java.util.*;
import java.io.*;


public class schoolsearch {

   public static class Teacher {
      String LName, FName, classroom;

      public Teacher(String LName, String FName, String classroom) {
         this.LName = LName;
         this.FName = FName;
         this.classroom = classroom;
      }
   }
   public static Teacher createTeacher(String line) {
      String[] s = line.split("[\\s,]+", 3);
      return new Teacher(s[0], s[1], s[2]);
   }

   public static class Student {
      String[] attributes;
      ArrayList<Teacher> teachers;

      public Student(String[] attributes, ArrayList<Teacher> teachers) {
         this.attributes = attributes;
         this.teachers = teachers;
      }
   }

   public static Student createStudent(String line, ArrayList<Teacher> teachers) {
      String[] attributes = line.split("[\\s,]+", 6);
      ArrayList<Teacher> teacher_match = new ArrayList<Teacher>();

      for (Teacher t : teachers) {
         if(attributes[3].equals(t.classroom))
            teacher_match.add(t);
      }
      return new Student(attributes, teacher_match);
   }

   public static void printStudent(Student s, boolean[] printFlags, boolean teacher_flag) {
      boolean temp_flag = false;
      for (int i = 0; i < 6; i++) {
         if (printFlags[i] && i == 0){
            System.out.print(s.attributes[i]);
            temp_flag = true;
         }
         else if (printFlags[i] && i > 0){
            System.out.print(", " + s.attributes[i]);
            temp_flag = true;
         }
      }
      //prints teachers if needed
      if (teacher_flag) {
         for (Teacher t : s.teachers) {
            if (temp_flag) {
               System.out.print(", " + t.LName + ", " + t.FName);
            }
            else {
               System.out.print(t.LName + ", " + t.FName);
            }
         }
      }
      System.out.println("");
   }

   public static void search(ArrayList<Student> students, int key, String name, boolean[] printFlags, boolean teacher_flag) {
      boolean notFound = true;

      for (Student s: students) {
         if (s.attributes[key].equals(name)){
            notFound = false;
            printStudent(s, printFlags, teacher_flag);
         }
      }
      if (notFound)
         System.out.println("search not found");
   }

   public static void searchTeachers(ArrayList<Student> students, String name) {
      boolean notFound = true;

      for (Student s: students) {
         for (Teacher t : s.teachers) {
            if (t.LName.equals(name)){
               notFound = false;
               System.out.println(s.attributes[0] + ", " + s.attributes[1]);
            }
         }
      }
      if (notFound)
         System.out.println("search not found");
   }
   //prints all teachers in a grade/classroom
   public static void printTeachers(ArrayList<Student> students, int key, String target) {
      boolean notFound = true;
      ArrayList<String> teacher_match = new ArrayList<String>();

      for (Student s: students) {
         if (s.attributes[key].equals(target)) {
            notFound = false;
            for (Teacher t : s.teachers) {
               if (!(teacher_match.contains(t.LName))) {
                  teacher_match.add(t.LName);
                  System.out.println(t.LName + ", " + t.FName);
               }
            }
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
            search(results, 5, Double.toString(low), new boolean[]{true, true, false, false, true, true}, true);
         }
         else {
            search(results, 5, Double.toString(high), new boolean[]{true, true, false, false, true, true}, true);
         }
      }
   }

   public static void averageGPA(ArrayList<Student> students, String name, int key, int flag) {
      boolean notFound = true;
      double numStudents = 0;
      double currSum = 0;

      for (Student s: students) {
         if (s.attributes[key].equals(name)){
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
         //Average GPA in Grade
         if (flag == 1) {
            System.out.println("Grade: " + name + " Average GPA: " + Double.toString(avg));
         }
         //averageGPA in Bus
         else {
            System.out.println("Bus: " + name + " Average GPA: " + Double.toString(avg));
         }
      }
   }

   public static void teacherByGPA(ArrayList<Student> students, Teacher t) {
      int numStudents = 0;
      double currSum = 0;
      for (Student s: students) {
         if (s.teachers.contains(t)) {
            currSum += Double.parseDouble(s.attributes[5]);
            numStudents++;
         }
      }
      double avg = Math.round((currSum/numStudents) * 100.0) / 100.0;
      System.out.println("Average GPA: " + Double.toString(avg) + "      " + t.LName + ", " + t.FName);
   }

   public static void analyzeGPA(ArrayList<Student> students, int key, int flag, ArrayList<Teacher> teachers) {
      ArrayList<String> match = new ArrayList<String>();
      //Average GPA in Grades/Buses
      if (flag != 0) {
         for (Student s: students) {
            if (!match.contains(s.attributes[key])) {
               match.add(s.attributes[key]);
            }
         }
         Collections.sort(match);
         for (String s : match) {
            averageGPA(students, s, key, flag);
         }
      }
      else {
         for (Teacher t : teachers) {
            teacherByGPA(students, t);
         }
      }
   }

   public static void displayInfo(ArrayList<Student> students, int printKey) {
      TreeMap<String, Integer> map = new TreeMap<String, Integer>();

      for (Student s: students) {
         String currGrade = s.attributes[printKey];
         if (map.containsKey(currGrade)) {
            map.put(currGrade, map.get(currGrade) + 1);
         }
         else {
            map.put(currGrade, 1);
         }
      }
      //Info
      if (printKey == 2) {
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
      //Enrollments
      else {
         System.out.println("<Classroom>: <Number of Students>");
         for (String key : map.keySet()) {
            if (map.containsKey(key)) {
               System.out.println(key + ": " + map.get(key));
            }
            else {
               System.out.println(key + ": 0");
            }
         }
      }
   }

   public static void searchStudents(String input, ArrayList<Student> students, ArrayList<Teacher> teachers) {
      String[] splitInput = input.split("[\\s:]+");
      System.out.println("\n----------RESULTS----------");
      //S[tudent]: <lastname>
      if ((splitInput[0].equals("S") || splitInput[0].equals("Student"))) {
         //B[us]
         if (splitInput.length == 3 && (splitInput[2].equals("B") || splitInput[2].equals("Bus"))) {
            search(students, 0, splitInput[1], new boolean[]{true, true, false, false, true, false}, false);
         }
         else if (splitInput.length == 2) {
            search(students, 0, splitInput[1], new boolean[]{true, true, true, true, false, false}, true);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //T[eacher]: <lastname>
      else if ((splitInput[0].equals("T") || splitInput[0].equals("Teacher"))) {
         if (splitInput.length == 2) {
            searchTeachers(students, splitInput[1]);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //G[rade]: <Number>
      else if ((splitInput[0].equals("G") || splitInput[0].equals("Grade"))) {
         if (splitInput.length == 2) {
            search(students, 2, splitInput[1], new boolean[]{true, true, false, false, false, false}, false);
         }
         //H[igh]
         else if (splitInput.length == 3 && (splitInput[2].equals("H") || splitInput[2].equals("High"))) {
            searchGPA(students, splitInput[1], 1);
         }
         //L[ow]
         else if (splitInput.length == 3 && (splitInput[2].equals("L") || splitInput[2].equals("Low"))) {
            searchGPA(students, splitInput[1], 0);
         }
         //T[eacher]
         else if (splitInput.length == 3 && (splitInput[2].equals("T") || splitInput[2].equals("Teacher"))) {
            printTeachers(students, 2, splitInput[1]);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //B[us]: <Number>
      else if ((splitInput[0].equals("B") || splitInput[0].equals("Bus"))) {
         if (splitInput.length == 2) {
            search(students, 4, splitInput[1], new boolean[]{true, true, true, true, false, false}, false);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //A[verage]: <Number>
      else if ((splitInput[0].equals("A") || splitInput[0].equals("Average"))) {
         if (splitInput.length == 2) {
            averageGPA(students, splitInput[1], 2, 1);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //I[nfo]
      else if ((splitInput[0].equals("I") || splitInput[0].equals("Info"))) {
         if (splitInput.length == 1) {
            displayInfo(students, 2);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //C[lass] <Number>
      else if ((splitInput[0].equals("C") || splitInput[0].equals("Class"))) {
         //S[tudent]
         if (splitInput.length == 3 && (splitInput[2].equals("S") || splitInput[2].equals("Student"))) {
            search(students, 3, splitInput[1], new boolean[]{true, true, false, false, false, false}, false);
         }
         //T[eacher]
         else if (splitInput.length == 3 && (splitInput[2].equals("T") || splitInput[2].equals("Teacher"))) {
            printTeachers(students, 3, splitInput[1]);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //E[nrollments]
      else if ((splitInput[0].equals("E") || splitInput[0].equals("Enrollments"))) {
         if (splitInput.length == 1) {
            displayInfo(students, 3);
         }
         else {
            System.out.println("INVALID COMMAND. Please try again :(");
         }
      }
      //GPA:
      else if (splitInput[0].equals("GPA")) {
         //G[rade]
         if (splitInput.length == 2 && (splitInput[1].equals("G") || splitInput[1].equals("Grade"))) {
            analyzeGPA(students, 2, 1, teachers);
         }
         //T[eacher]
         else if (splitInput.length == 2 && (splitInput[1].equals("T") || splitInput[1].equals("Teacher"))) {
            analyzeGPA(students, -1, 0, teachers);
         }
         //B[us]
         else if (splitInput.length == 2 && (splitInput[1].equals("B") || splitInput[1].equals("Bus"))) {
            analyzeGPA(students, 4, 2, teachers);
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
         File file = new File("./list.txt");
         File teacher_file = new File("./teachers.txt");
         Scanner input = new Scanner(file);
         Scanner teacher_input = new Scanner(teacher_file);
         ArrayList<Student> students = new ArrayList<Student>();
         ArrayList<Teacher> teachers = new ArrayList<Teacher>();
         String next;
         String prompt = "S[tudent]: <lastname> [B[us]]\nT[eacher]: <lastname>\nB[us]: <number>\nG[rade]: <number> [H[igh]|L[ow]|T[eacher]]\nA[verage]: <number>\nC[lass]: <Number> <S[tudent]|T[eacher]>\nE[nrollments]\nGPA: <G[rade]|T[eacher]|B[us]>\nI[nfo]\nQ[uit]\n\nPlease select an option (Last names must be typed in ALL CAPS): ";

         //create teacher objects
         while (teacher_input.hasNextLine()) {
            Teacher t = createTeacher(teacher_input.nextLine());
            teachers.add(t);
         }
         teacher_input.close();

         //Create student objects
         while (input.hasNextLine()) {
            Student s = createStudent(input.nextLine(), teachers);
            students.add(s);
         }
         input.close();

         System.out.print(prompt);
         Scanner userInput = new Scanner(System.in);
         while (!(next=userInput.nextLine()).equals("Q") && !next.equals("Quit")) {
            searchStudents(next, students, teachers);
            System.out.print("---------------------------\n\n" + prompt);
         }


      }

      catch (FileNotFoundException e) {
         System.out.println(e.getMessage());
      }

   }
}
