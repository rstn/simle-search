package search

import java.lang.IllegalArgumentException


abstract class Command {

    private val params = mutableMapOf<String, Any>()

    protected fun addValue(paramName: String, value: Any) {
        params[paramName] = value
    }

    protected fun addStringValue(paramName: String, value: String) {
        if (value.isBlank()) throw IllegalArgumentException("Value of parameter must be not null or not empty")
        addValue(paramName, value)
    }

    protected fun getValue(paramName: String): Any {
        return params[paramName]!!
    }

    protected fun getStringValue(paramName: String): String {
        return getValue(paramName) as String
    }

    protected fun getIntValue(paramName: String): Int {
        return getValue(paramName) as Int
    }
}

interface CommandParser {

    fun parse(rawCommand: String): ParsingResult

    class ParsingResult private constructor(
        private val errorMessage: String?, private val command: Command? = null,
        val haveError: Boolean = true,
    ) {
        init {
            if (command != null && (!errorMessage.isNullOrBlank() || haveError))
                throw IllegalArgumentException("Command is created, but parsing process have some error")
            if (command == null && (errorMessage.isNullOrBlank() || !haveError))
                throw IllegalArgumentException("Command is not created, but parsing process have not error")
        }

        constructor(command: Command) : this(null, command, false)
        constructor(messageError: String) : this(messageError, null)

        fun getCommand(): Command = command!!
        fun getErrorMessage(): String = errorMessage!!
    }
}

interface CommandExecutor {

    fun execute(command: Command): String
}