package com.selectuser;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Employee {

    @PrimaryKey
    public long id;                      // ID сотрудника, unsigned int

    public int version;                 // Версия данных метки (протокола)
    public String name;                 // Имя сотрудника
    public String surname;              // Фамилия сотрудника
    public long organizationId;          // ID организации, unsigned int
    public String organizationName;     // Наименование организации
    public String position;             // Должность сотрудника
    public int access;                  // Уровень доступа
    public int pin;                     // PIN код
}
