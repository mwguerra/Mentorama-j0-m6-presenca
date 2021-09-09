package com.mwguerra.database;

import com.mwguerra.models.AttendanceModel;
import com.mwguerra.models.ClassModel;
import com.mwguerra.models.ClassStudentPivotModel;
import com.mwguerra.models.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class ClassroomDatabase {
    private int id;

    public List<ClassModel> classTable = new ArrayList<>();
    public List<StudentModel> studentTable = new ArrayList<>();
    public List<ClassStudentPivotModel> classStudentPivotTable = new ArrayList<>();
    public List<AttendanceModel> attendanceTable = new ArrayList<>();

    public List<ClassModel> getClassTable() {
        return classTable;
    }

    public List<StudentModel> getStudentTable() {
        return studentTable;
    }

    public List<ClassStudentPivotModel> getClassStudentPivotTable() {
        return classStudentPivotTable;
    }

    public List<AttendanceModel> getAttendanceTable() {
        return attendanceTable;
    }

    public int getSafeId() {
        return id++;
    }

    // TABLE STRUCTURE
    // class
        // id
        // name
    // students
        // id
        // name
    // class_student
        // classId
        // studentId
    // attendance
        // id
        // date
        // classId
        // studentId
}
