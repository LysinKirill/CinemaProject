package presentation

import domain.AuthorizationContoller


class AuthorizationMenuManager(private val authorizationController: AuthorizationContoller) :
    MenuManager<AuthorizationMenuOption> {
    override fun showOptions() {
        print("Available authorization actions:\n")
        println("\t1: Login")
        println("\t2: Register")
    }

    override fun getRequest(): AuthorizationMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    override fun processRequest(request: AuthorizationMenuOption?): AuthorizationMenuOption? {
        when (request) {
            AuthorizationMenuOption.Login -> {

                val response = login()
                if (response) {
                    println("Authorization successful")
                    return AuthorizationMenuOption.LoggedIn
                } else {
                    println("Authorization failed")
                }
            }

            AuthorizationMenuOption.Register -> {
                val registerResult = register()
                if (registerResult)
                    println("Registration successful")
                else
                    println("Register failed")
            }

            else -> println("Incorrect option...")
        }

        return request
    }

    override fun handleInteractions() {
        do {
            showOptions()
            val request = getRequest()
            val response = processRequest(request)

        } while (response != AuthorizationMenuOption.LoggedIn)
        println("Entered the account")
    }


    fun login(): Boolean {

        print("Input the username: ")
        val username = readlnOrNull() ?: return false

        print("Input your password: ")
        val userPassword = readlnOrNull() ?: return false
        return authorizationController.login(username, userPassword)
    }

    fun register(): Boolean {
        print("Input the username: ")
        val username = readlnOrNull() ?: return false
        if (username.isBlank()) {
            println("Username cannot be blank")
            return false
        }

        print("Input your password: ")
        val userPassword = readlnOrNull() ?: return false
        if (username.isBlank()) {
            println("Password cannot be empty")
            return false
        }
        authorizationController.registerUser(username, userPassword)
        return true
    }

    private fun parseAction(userInput: String): AuthorizationMenuOption? {
        try {
            if (userInput[0].isDigit()) {
                val userInputInt = userInput.toInt()

                if (userInputInt == 1)
                    return AuthorizationMenuOption.Login
                if (userInputInt == 2)
                    return AuthorizationMenuOption.Register
            } else
                return AuthorizationMenuOption.valueOf(userInput)
        } catch (ex: Exception) {
            return null
        }
        return null
    }

}