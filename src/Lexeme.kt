data class Lexeme(val value: String, val kind : Lexeme.Type) {

    enum class Type {
        ID,
        NUMBER,
        DELIM,
        DELIM2,
        NONE,
        ERROR,
        KEYWORD
    }



}