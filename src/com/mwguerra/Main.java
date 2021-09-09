package com.mwguerra;

import com.mwguerra.database.ClassroomDatabase;
import com.mwguerra.database.DatabaseSeeder;
import com.mwguerra.models.AttendanceModel;
import com.mwguerra.models.ClassModel;
import com.mwguerra.models.StudentModel;
import com.mwguerra.utils.TableFormatter;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
  private static final ClassroomDatabase db = new ClassroomDatabase();

  public static void main(String[] args) {
    int option;

    do {
      option = mainMenu();

      switch (option) {
        case 1:
          createClass();
          break;
        case 2:
          createStudent();
          break;
        case 3:
          createAttendance();
          break;
        case 4:
          showClassAttendance();
          break;
        case 9:
          seeder();
          break;
        case 0:
        default:
          break;
      }
    } while(option != 0);
  }

  public static int mainMenu() {
    System.out.println("");
    System.out.println("----------------------------------------");
    System.out.println("-- MAIN MENU ---------------------------");
    System.out.println("----------------------------------------");
    System.out.println("-- 1. Cadastro de turma");
    System.out.println("-- 2. Cadastro de estudante");
    System.out.println("-- 3. Registro de presença");
    System.out.println("-- 4. Consulta presença para turma");
    System.out.println("-- 9. >> ADD FAKE DATA (2 turmas e 5 alunos)");
    System.out.println("-- 0. Sair do sistema");
    System.out.println("--");
    System.out.print("-- Digite a opção desejada: ");

    Scanner scanner = new Scanner(System.in);
    return scanner.nextInt();
  }

  private static void createClass() {
    System.out.println("\n");
    System.out.println("#################################");
    System.out.println("## NOVA TURMA");

    System.out.print("## Nome: ");
    Scanner scanner = new Scanner(System.in);
    String className = scanner.nextLine();

    ClassModel classInstance = new ClassModel(db.getSafeId(), className);
    classInstance.setDatabase(db).save();
  }

  private static void createStudent() {
    System.out.println("\n");
    System.out.println("#################################");
    System.out.println("## NOVO ALUNO");

    System.out.print("## Nome: ");
    Scanner scanner1 = new Scanner(System.in);
    String studentName = scanner1.nextLine();

    ClassModel classInstance = new ClassModel();

    String[] classIds = multipleClassSelectionPrompt(classInstance);

    StudentModel student = new StudentModel(db.getSafeId(), studentName);
    student.setDatabase(db).save();

    int totalClassesAttached = 0;
    for (String strId: classIds) {
      int classId = Integer.parseInt(strId.trim());

      student.enrollToClass(classInstance.findById(classId));

      totalClassesAttached++;
    }

    System.out.print("## Estudante inscrito em " + totalClassesAttached + " turmas.");
  }

  private static String[] multipleClassSelectionPrompt(ClassModel classInstance) {
    System.out.println("## Lista de turmas");

    List<ClassModel> classes = classInstance.setDatabase(db).all();

    for (ClassModel classItem: classes) {
      System.out.println("## [" + classItem.getId() + "] " + classItem.getName());
    }

    System.out.print("## Inscrição em turmas (separadas por vírgulas): ");
    Scanner scanner2 = new Scanner(System.in);
    String csv = scanner2.nextLine();

    return csv.split(",");
  }

  private static void createAttendance() {
    System.out.println("\n");
    System.out.println("#################################");
    System.out.println("## NOVA CHAMADA");

    ClassModel classInstance = new ClassModel().setDatabase(db);

    int classId = classSelectionPrompt();

    StudentModel student = new StudentModel();
    ClassModel classItem = classInstance.findById(classId);
    List<StudentModel> students = student.setDatabase(db).allWhereClassIs(classItem);

    int totalStudents = students.size();
    int totalAttendees = 0;

    System.out.println("## Turma " + classItem.getName() + " selecionada (" + totalStudents + ").");
    for (StudentModel s: students) {
      System.out.print("## " + s.getName() + " está presente [S/N]? ");
      Scanner scanner2 = new Scanner(System.in);
      String sn = scanner2.nextLine();

      if (sn.toLowerCase().charAt(0) == "s".charAt(0)) {
        totalAttendees++;

        s.attendsToClassAtDate(classItem, new GregorianCalendar());
      }
    }

    System.out.println("## Fim da chamada. " + totalAttendees + "/" + totalStudents + " presentes.");
  }

  private static int classSelectionPrompt() {
    showClassList();

    System.out.print("## Código da turma para chamada: ");
    Scanner scanner1 = new Scanner(System.in);
    return scanner1.nextInt();
  }

  private static void showClassAttendance() {
    System.out.println("\n");
    System.out.println("#################################");
    System.out.println("## RELATÓRIO DE PRESENÇA");

    showClassList();

    ClassModel classInstance = new ClassModel().setDatabase(db);

    System.out.print("## Selecione a turma para visualizar as presenças: ");
    Scanner scanner1 = new Scanner(System.in);
    int classId = scanner1.nextInt();
    ClassModel classItem = classInstance.findById(classId);

    // Cabeçalho com datas
    AttendanceModel attendance = new AttendanceModel();
    List<AttendanceModel> allForClass = attendance.setDatabase(db).allWhereClassIs(classItem);

    List<List<String>> table = new ArrayList<>();
    List<String> headers = new ArrayList<>();
    headers.add("");
    headers.addAll(attendance.getDateStringList(allForClass));
    int totalColumns = headers.size();

    table.add(headers);

    // Linhas com alunos e presença
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    for (AttendanceModel item: allForClass) {

      StudentModel student = new StudentModel()
          .setDatabase(db)
          .findById(item.getStudentId());

      String dateString = dateFormat.format(item.getDate().getTime());

      int columnToFill = headers.indexOf(dateString);

      boolean hasStudent = table
          .stream()
          .anyMatch(row -> row.contains(student.getName()));

      if (!hasStudent) {
        String[] rowData = new String[totalColumns];

        rowData[0] = student.name;
        for (int i = 1; i < rowData.length; i++) {
          rowData[i] = "Ausente";
        }

        List<String> newRow = new ArrayList<>(Arrays.asList(rowData));

        table.add(newRow);
      }

      for (List<String> row: table) {
        if (Objects.equals(row.get(0), student.getName())) {
          row.set(columnToFill, "Presente");
        }
      }
    }

    TableFormatter tableFormatter = new TableFormatter();

    tableFormatter
        .fitContents()
        .alignLeft()
        .addAll(table)
        .build();
  }

  private static void showClassList() {
    System.out.println("## Lista de turmas");

    ClassModel classInstance = new ClassModel();
    List<ClassModel> classes = classInstance.setDatabase(db).all();

    for (ClassModel classItem: classes) {
      System.out.println("## [" + classItem.getId() + "] " + classItem.getName());
    }
  }

  private static void seeder() {
    DatabaseSeeder dbSeeder = new DatabaseSeeder();
    dbSeeder.setDatabase(db).handle();
  }
}
