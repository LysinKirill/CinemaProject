package presentation

import di.DI


fun main() {
    try {
        DI.authorizationMenuManager.handleInteractions()
        DI.consoleMenuManager.handleInteractions()
    } catch (ex: Exception) {
        println("Unexpected exception has occurred: ${ex.message}")
    }

}
