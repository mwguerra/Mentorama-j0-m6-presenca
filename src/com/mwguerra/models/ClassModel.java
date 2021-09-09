package com.mwguerra.models;

import com.mwguerra.database.ClassroomDatabase;

import java.util.List;

public class ClassModel {
    public Integer id;
    public String name;
    public ClassroomDatabase database;

    public ClassModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ClassModel() {}

    public ClassModel setDatabase(ClassroomDatabase database) {
        this.database = database;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StudentModel> students() {
        ClassStudentPivotModel pivot = new ClassStudentPivotModel(this);
        return pivot.studentsWhereClassIs(this);
    }

    public List<ClassModel> all() {
        return database.getClassTable();
    }

    public ClassModel findById(int classId) {
        List<ClassModel> classList = database.getClassTable();

        return classList
                .stream()
                .filter(classItem -> classItem.getId() == classId)
                .findFirst()
                .orElse(null);
    }

    public void assignStudent(StudentModel student) {
        ClassStudentPivotModel classStudentPivot = new ClassStudentPivotModel();
        classStudentPivot.setDatabase(database);
        classStudentPivot.id = database.getSafeId();
        classStudentPivot.studentId = student.getId();
        classStudentPivot.classId = this.getId();
        classStudentPivot.save();
    }

    public void save() {
        database.classTable.add(this);
    }
}
