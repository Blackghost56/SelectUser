package com.selectuser;

public class Employee {

    public enum ACCESS {SLAVE, MASTER}

    public int version;                 // Версия данных метки (протокола)
    public String organizationName;     // Наименование организации
    public int organizationId;          // ID организации
    public String name;                 // Имя сотрудника
    public String surname;              // Фамилия сотрудника
    public int id;                      // ID сотрудника
    public String position;             // Должность сотрудника
    public int access;                  // Уровень доступа


}
