package com.mwguerra.models;

import com.mwguerra.database.ClassroomDatabase;

import java.util.ArrayList;
import java.util.List;

public class ClassStudentPivotModel {
    public Integer id;
    public Integer classId;
    public Integer studentId;
    public ClassroomDatabase database;

    public ClassStudentPivotModel(ClassModel classInstance, StudentModel studentInstance) {
        this.classId = classInstance.getId();
        this.studentId = studentInstance.getId();
        this.id = Integer.parseInt(this.classId.toString() + this.studentId.toString());
    }

    ClassStudentPivotModel(ClassModel classInstance) {
        this.classId = classInstance.getId();
    }

    ClassStudentPivotModel(StudentModel studentInstance) {
        this.studentId = studentInstance.getId();
    }

    public ClassStudentPivotModel() {}

    public ClassStudentPivotModel setDatabase(ClassroomDatabase database) {
        this.database = database;
        return this;
    }

    public List<StudentModel> studentsWhereClassIs(ClassModel classInstance) {
        Integer id = classInstance.getId();
        StudentModel studentModel = new StudentModel().setDatabase(database);

        List<StudentModel> students = new ArrayList<>();
        for (ClassStudentPivotModel row: database.getClassStudentPivotTable()) {
            if (row.classId == id) {
                StudentModel student = studentModel.findById(row.studentId);
                students.add(student);
            }
        }

        return students;
    }

    public List<ClassModel> classesWhereStudentIs(StudentModel studentInstance) {
        Integer id = studentInstance.getId();
        ClassModel classModel = new ClassModel().setDatabase(database);

        List<ClassModel> classes = new ArrayList<>();
        for (ClassStudentPivotModel row: database.getClassStudentPivotTable()) {
            if (row.studentId == id) {
                ClassModel classInstance = classModel.findById(row.classId);
                classes.add(classInstance);
            }
        }

        return classes;
    }

    public List<ClassStudentPivotModel> all() {
        return database.getClassStudentPivotTable();
    }

    public void save() {
        database.classStudentPivotTable.add(this);
    }
}
