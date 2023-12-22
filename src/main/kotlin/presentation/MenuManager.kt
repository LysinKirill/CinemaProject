package presentation

interface MenuManager {
    fun showOptions()
    fun getRequest() : MenuOption?
    fun processRequest(request: MenuOption?)
    fun handleInteractions()
}