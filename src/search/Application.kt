package search

class Application(storagePath: String) {
    private val searchEngine: SearchEngine = SearchEngineImpl(storagePath)
    private val commandParser: CommandParser = SearchCommandParser()
    private val commandExecutor: CommandExecutor = SearchCommandExecutor(searchEngine)
    private val menu = Menu()

    fun run() {
        var command: Command?
        do {
            menu.print()
            command = readAndExecuteCommand()
        } while (command !is ExitCommand)
    }

    private fun readAndExecuteCommand(): Command? {
        val parsingResult = commandParser.parse(readLine()!!)
        if (parsingResult.haveError) {
            println(parsingResult.getErrorMessage())
            return null
        } else {
            val resultOfCommand = commandExecutor.execute(parsingResult.getCommand())
            if (resultOfCommand.isNotBlank()) {
                println(resultOfCommand)
            }
        }
        return parsingResult.getCommand()
    }
}