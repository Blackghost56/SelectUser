package com.selectuser;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//@Entity(primaryKeys = {"name", "surname"})
@Entity
public class Employee {

//    @PrimaryKey(autoGenerate = true)
//    public long dbId;

//    @Ignore
    public int version;                 // Версия данных метки (протокола)
    @PrimaryKey
    public int id;                      // ID сотрудника
//    @NonNull
    public String name;                 // Имя сотрудника
//    @NonNull
    public String surname;              // Фамилия сотрудника
    public int organizationId;          // ID организации
    public String organizationName;     // Наименование организации
    public String position;             // Должность сотрудника
    public int access;                  // Уровень доступа
}
