package com.selectuser.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.selectuser.Employee;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EmployeeDao {

    @Query("SELECT * FROM employee")
    LiveData<List<Employee>> getAllLive();

    @Query("SELECT * FROM employee")
    List<Employee> getAll();


    @Query("SELECT * FROM employee WHERE id = :id")
    Employee getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Employee employee);

    @Insert(onConflict = REPLACE)
    void insert(List<Employee> employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("DELETE FROM employee WHERE id = :id")
    void delete(long id);

//    @Query("SELECT EXISTS(SELECT * FROM employee WHERE id = :id)")
//    boolean isRowIsExist(long id);
}
