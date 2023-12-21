package data

import domain.entitiy.EmployeeEntity

interface EmployeeDao {
    fun getEmployee(employeeId: Int) : EmployeeEntity
    fun addEmployee(employeeEntity: EmployeeEntity)
    fun getAllEmployees() : List<EmployeeEntity>
}