package search

import search.CommandName.*
import search.CommandParser.*

enum class CommandName(val value: String, val desc: String) {
    FIND("1", "1. Find a person"),
    PRINT_ALL("2", "2. Print all people"),
    EXIT("0", "0. Exit")
}

abstract class SearchAppCommand(val short: String, val desc: String) : Command()

class FindCommand : SearchAppCommand(FIND.value, FIND.desc)
class PrintAllCommand : SearchAppCommand(PRINT_ALL.value, PRINT_ALL.desc)
class ExitCommand : SearchAppCommand(EXIT.value, EXIT.desc)

class Menu {
    private val commands: Map<String, SearchAppCommand> by lazy {
        val cmds = mutableMapOf<String, SearchAppCommand>()
        listOf(FindCommand(), PrintAllCommand(), ExitCommand()).forEach {
            cmds[it.short] = it
        }
        cmds
    }

    fun print() {
        println("\n=== Menu ===")
        for (cmd in commands.values) {
            println(cmd.desc)
        }
    }
}

class SearchCommandExecutor(private val searchEngine: SearchEngine) : CommandExecutor {

    override fun execute(command: Command): String {
        return when (command) {
            is FindCommand -> {
                println("Select a matching strategy: ${SearchEngine.STRATEGY.values().joinToString { it.name }}")
                val strategy = SearchEngine.STRATEGY.valueOf(readLine()!!)

                println("Enter a name or email to search ${strategy.name.lowercase()} suitable people.")
                val res = searchEngine.find(readLine()!!, strategy)
                if (res.isEmpty()) {
                    "No matching people found."
                } else {
                    res.joinToString("\n")
                }
            }
            is PrintAllCommand -> {
                val sb = StringBuilder("=== List of people ===\n")
                sb.append(searchEngine.getAllEntity().joinToString("\n"))
                sb.toString()
            }
            is ExitCommand -> {
                ""
            }
            else -> throw RuntimeException()
        }
    }

}

class SearchCommandParser : CommandParser {

    override fun parse(rawCommand: String): ParsingResult {
        return when (rawCommand) {
            FIND.value -> ParsingResult(FindCommand())
            PRINT_ALL.value -> ParsingResult(PrintAllCommand())
            EXIT.value -> ParsingResult(ExitCommand())
            else -> ParsingResult("Incorrect option! Try again.")
        }
    }

}

