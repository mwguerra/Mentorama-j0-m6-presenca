package com.mwguerra.database;

import com.mwguerra.models.AttendanceModel;
import com.mwguerra.models.ClassModel;
import com.mwguerra.models.ClassStudentPivotModel;
import com.mwguerra.models.StudentModel;

import java.util.Arrays;
import java.util.Calendar;

public class DatabaseSeeder {
  private ClassroomDatabase database;

  public DatabaseSeeder setDatabase(ClassroomDatabase database) {
    this.database = database;
    return this;
  }

  public StudentModel createStudent(String name) {
    StudentModel student = new StudentModel();
    student.setDatabase(database);
    student.id = database.getSafeId();
    student.name = name;
    student.save();

    return student;
  }

  public ClassModel createClass(String name) {
    ClassModel classInstance = new ClassModel();
    classInstance.setDatabase(database);
    classInstance.id = database.getSafeId();
    classInstance.name = name;
    classInstance.save();

    return classInstance;
  }

  public void handle() {
    ClassStudentPivotModel classStudentPivot = new ClassStudentPivotModel().setDatabase(database);
    AttendanceModel attendance = new AttendanceModel().setDatabase(database);
    Calendar date;

    ClassModel class1 = createClass("Navegação Astronômica");
    ClassModel class2 = createClass("Termodinâmica Aplicada III");

    // João -> turmas 1 e 2
    StudentModel student1 = createStudent("João da Silva");
    class1.assignStudent(student1);
    class2.assignStudent(student1);

    student1.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 15));
    student1.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 26));

    student1.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 17));
    student1.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 19));
    student1.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 23));

    // Ana -> turma 1
    StudentModel student2 = createStudent("Ana Alice Silva");
    class1.assignStudent(student2);

    student2.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 15));
    student2.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 19));

    // Carlos -> turma 1
    StudentModel student3 = createStudent("Carlos Moura");
    class1.assignStudent(student3);

    student3.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 15));
    student3.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 19));
    student3.attendsToClassAtDate(class1, getCalendarDate(2021, 3, 26));

    // Fernanda -> turma 2
    StudentModel student4 = createStudent("Fernanda Silva");
    class2.assignStudent(student4);

    student4.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 17));
    student4.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 23));

    // Julia -> turma 2
    StudentModel student5 = createStudent("Julia Tiberius");
    class2.assignStudent(student5);

    student5.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 17));
    student5.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 19));
    student5.attendsToClassAtDate(class2, getCalendarDate(2021, 3, 23));

    System.out.println(" Class >> " + Arrays.toString(class1.all().stream().map(c -> "(" + c.id + ") " + c.name).toArray()));
    System.out.println(" Student >> " + Arrays.toString(student1.all().stream().map(c -> "(" + c.id + ") " + c.name).toArray()));
    System.out.println(" Pivot >> " + Arrays.toString(classStudentPivot.all().stream().map(c -> "(" + c.id + ") s:" + c.studentId + "/c:" + c.classId).toArray()));
    System.out.println(" Attendance >> " + Arrays.toString(attendance.all().stream().map(c -> "(" + c.id + ") " + c.date.getTime()).toArray()));
  }

  private Calendar getCalendarDate(int year, int month, int day) {
    Calendar date;
    date = Calendar.getInstance();
    date.set(year, month, day);

    return date;
  }
}
