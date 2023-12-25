package data

import domain.entitiy.EmployeeEntity

interface EmployeeDao {
    fun getEmployee(username: String) : EmployeeEntity?
    fun addEmployee(employeeEntity: EmployeeEntity)
    fun getAllEmployees() : List<EmployeeEntity>
}