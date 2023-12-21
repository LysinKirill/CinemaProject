package presentation

fun main() {


    val chosenOption = readlnOrNull()?.toIntOrNull()

    val menuOption: MenuOptions? = MenuOptions.valueOf(readln())

    when(menuOption) {
        MenuOptions.SellTicket -> TODO()
        MenuOptions.ReturnTicket -> TODO()
        MenuOptions.ShowAvailableSeats -> TODO()
        MenuOptions.AddFilmInfo -> TODO()
        MenuOptions.EditFilmInfo -> TODO()
        MenuOptions.EditSessionInfo -> TODO()
        MenuOptions.MarkOccupiedSeat -> TODO()
        MenuOptions.Exit -> TODO()
        null -> TODO()
    }
}