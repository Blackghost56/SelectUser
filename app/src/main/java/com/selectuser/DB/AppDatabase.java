package com.selectuser.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.selectuser.Employee;

@Database(entities = {Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
}
