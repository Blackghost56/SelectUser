package com.selectuser.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.selectuser.Employee;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employee")
    List<Employee> getAll();

    @Query("SELECT * FROM employee")
    Flowable<List<Employee>> getAllRxFlowable();

    @Query("SELECT * FROM employee")
    Single<List<Employee>> getAllRxSingle();


    @Query("SELECT * FROM employee WHERE id = :id")
    Employee getById(long id);

    @Query("SELECT * FROM employee WHERE id = :id")
    Single<Employee> getByIdRx(long id);

//    @Insert(onConflict = REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Employee employee);

    @Insert(onConflict = REPLACE)
    void insert(List<Employee> employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("SELECT EXISTS(SELECT * FROM employee WHERE id = :id)")      //todo need test
    boolean isRowIsExist(long id);
}
