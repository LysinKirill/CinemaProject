package domain

import data.EmployeeDao
import domain.entitiy.EmployeeEntity
import java.security.MessageDigest

class AuthorizationControllerImpl(private val employeeDao: EmployeeDao) : AuthorizationContoller {


    override fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashedBytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return bytesToString(hashedBytes)
    }


    override fun bytesToString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    override fun registerUser(username: String, password: String) {
        val hashedPassword = hashPassword(password)
        employeeDao.addEmployee(EmployeeEntity(username, hashedPassword))
    }

    override fun login(username: String, password: String): Boolean {

        if (!employeeDao.getAllEmployees().any { employee -> employee.employeeUsername == username })
            return false
        val hashedPassword = hashPassword(password)
        val employee = employeeDao.getEmployee(username) ?: return false
        return employee.hashedPassword == hashedPassword
    }
}