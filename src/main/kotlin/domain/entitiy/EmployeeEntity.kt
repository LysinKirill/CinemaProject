package domain.entitiy

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeEntity(val employeeUsername: String, val hashedPassword: String)