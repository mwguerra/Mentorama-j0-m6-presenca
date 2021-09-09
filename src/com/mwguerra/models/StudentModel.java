package com.mwguerra.models;

import com.mwguerra.database.ClassroomDatabase;

import java.util.Calendar;
import java.util.List;

public class StudentModel {
    public Integer id;
    public String name;
    public ClassroomDatabase database;

    public StudentModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public StudentModel() {}

    public StudentModel setDatabase(ClassroomDatabase database) {
        this.database = database;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ClassModel> classes() {
        return new ClassStudentPivotModel(this)
                .classesWhereStudentIs(this);
    }

    public List<StudentModel> all() {
        return database.getStudentTable();
    }

    public List<StudentModel> allWhereClassIs(ClassModel classInstance) {
        return new ClassStudentPivotModel()
                .setDatabase(database)
                .studentsWhereClassIs(classInstance);
    }

    public StudentModel findById(int studentId) {
        return database.getStudentTable()
                .stream()
                .filter(studentItem -> studentItem.getId() == studentId)
                .findFirst()
                .orElse(null);
    }

    public void enrollToClass(ClassModel classInstance) {
        ClassStudentPivotModel classStudentPivot = new ClassStudentPivotModel();
        classStudentPivot.setDatabase(database);
        classStudentPivot.id = database.getSafeId();
        classStudentPivot.studentId = this.getId();
        classStudentPivot.classId = classInstance.getId();
        classStudentPivot.save();
    }

    public void attendsToClassAtDate(ClassModel classInstance, Calendar date) {
        AttendanceModel attendance = new AttendanceModel();
        attendance.setDatabase(database);
        attendance.id = database.getSafeId();
        attendance.date = date;
        attendance.classId = classInstance.getId();
        attendance.studentId = this.getId();
        attendance.save();
    }

    public void save() {
        database.studentTable.add(this);
    }
}
