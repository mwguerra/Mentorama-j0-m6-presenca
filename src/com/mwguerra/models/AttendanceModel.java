package com.mwguerra.models;

import com.mwguerra.database.ClassroomDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceModel {
  public Integer id;
  public Calendar date;
  public Integer classId;
  public Integer studentId;
  public ClassroomDatabase database;

  public AttendanceModel(Integer id, Calendar date, ClassModel classInstance, StudentModel studentInstance) {
    this.id = id;
    this.date = date;
    this.classId = classInstance.getId();
    this.studentId = studentInstance.getId();
  }

  public AttendanceModel() {}

  public AttendanceModel setDatabase(ClassroomDatabase database) {
    this.database = database;
    return this;
  }

  public List<AttendanceModel> all() {
    return database.getAttendanceTable();
  }

  public List<AttendanceModel> allWhereClassIs(ClassModel classInstance) {
    return database.getAttendanceTable()
        .stream()
        .filter(attendanceItem -> attendanceItem.getClassId() == classInstance.getId())
        .collect(Collectors.toList());
  }

  public List<String> getDateStringList(List<AttendanceModel> attendance) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    return attendance
        .stream()
        .map(attendanceItem -> dateFormat.format(attendanceItem.getDate().getTime()))
        .sorted()
        .distinct()
        .collect(Collectors.toList());
  }

  public List<String> getStudentStringList(List<AttendanceModel> attendance) {
    return attendance
        .stream()
        .map(attendanceItem -> new StudentModel().findById(attendanceItem.studentId).getName())
        .distinct()
        .collect(Collectors.toList());
  }

  public List<AttendanceModel> allWhereStudentIs(StudentModel studentInstance) {
    return database.getAttendanceTable()
        .stream()
        .filter(attendanceItem -> attendanceItem.getStudentId() == studentInstance.getId())
        .collect(Collectors.toList());
  }

  public AttendanceModel findById(int attendanceId) {
    return database.getAttendanceTable()
        .stream()
        .filter(attendanceItem -> attendanceItem.getId() == attendanceId)
        .findFirst()
        .orElse(null);
  }

  public Integer getId() {
    return id;
  }

  public Calendar getDate() {
    return date;
  }

  public Integer getClassId() {
    return classId;
  }

  public Integer getStudentId() {
    return studentId;
  }

  public void save() {
    database.attendanceTable.add(this);
  }
}
