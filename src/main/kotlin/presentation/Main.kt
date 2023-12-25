package presentation

import di.DI


fun main() {
    DI.authorizationMenuManager.handleInteractions()
    DI.consoleMenuManager.handleInteractions()
}
