package presentation

interface MenuManager <T>{
    fun showOptions()
    fun getRequest() : T?
    fun processRequest(request: T?) : T?
    fun handleInteractions()
}