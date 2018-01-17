class SyntaxAnalyzer(val lexemes : List<Lexeme>) {
    
    fun analyze() : Boolean{
        try {
            var remainingSentence = lexemes;
            while (remainingSentence.isNotEmpty()) {
                remainingSentence = nextIsOperator(remainingSentence)
            }
            return true
        }
        catch (e : SyntaxException) {
            return false
        }
    }
    
    
    fun nextIsIfStatement(sentence: List<Lexeme>) : List<Lexeme>{

        var remainingSentence = nextIsExpected("if",   sentence)
            remainingSentence = nextIsLogicalCondition(remainingSentence)
            remainingSentence = nextIsExpected("then", remainingSentence)
            remainingSentence = nextIsOperator(remainingSentence)
        
        return remainingSentence
    }
    
    private fun nextIsOperator(sentence: List<Lexeme>) : List<Lexeme>{
        try {
            return nextIsIfStatement(sentence)
        }
        catch (e: SyntaxException) {
            return nextIsAssignmentStatement(sentence)
        }
    }
    

    private fun nextIsAssignmentStatement(sentence: List<Lexeme>): List<Lexeme> {
        
        var remainingSentence = nextIsAnId(sentence);
            remainingSentence = nextIsExpected(":=", remainingSentence);
            remainingSentence = nextIsAnId(remainingSentence)
            remainingSentence = nextIsExpected(";", remainingSentence)
        
        return remainingSentence
    }

    private fun nextIsLogicalCondition(sentence: List<Lexeme>) : List<Lexeme>{
        
        var remainingSentence = nextIsValue(sentence)
            remainingSentence = nextIsExpected("<>", remainingSentence)
            remainingSentence = nextIsValue(remainingSentence)
        
        return remainingSentence
        
    }

    private fun nextIsValue(sentence: List<Lexeme>): List<Lexeme> {
        
        try {
            return nextIsFunctionExpression(sentence)
        }
        catch (e : SyntaxException) {
            return nextIsAnId(sentence)
        }
    }

    private fun nextIsFunctionExpression(sentence: List<Lexeme>): List<Lexeme> {
        var remainingSentence = nextIsAnId(sentence)
            remainingSentence = nextIsExpected("(", remainingSentence)
            remainingSentence = nextIsArguments(remainingSentence)
            remainingSentence = nextIsExpected(")", remainingSentence)
        
        return remainingSentence
    }

    private fun nextIsArguments(sentence: List<Lexeme>): List<Lexeme> {
        var remainingSentence = nextIsArgument(sentence)
        while (remainingSentence.head.value.equals(",")) {
            
            remainingSentence = nextIsExpected(",", remainingSentence)
            remainingSentence = nextIsArgument(remainingSentence)
        }
        return remainingSentence
    }

    private fun nextIsArgument(sentence: List<Lexeme>): List<Lexeme> {
        try {
            var remainingSentence = nextIsExpected("@", sentence);
            remainingSentence = nextIsAnId(remainingSentence);
            return remainingSentence
        }
        catch (e : SyntaxException) {
            val remainingSentence = nextIsAnId(sentence);
            return remainingSentence
        } 
    }

    private fun nextIsAnId(sentence: List<Lexeme>): List<Lexeme> {
        val lexeme = sentence.head;
        
        if (lexeme.kind.equals(Lexeme.Type.ID)) return sentence.tail
        else                                    throw  IsNotAnIdException(lexeme.value)
    }

    private fun nextIsExpected(word: String, lexemes: List<Lexeme>) : List<Lexeme>{
        val lexemeValue = lexemes.head.value;
        
        if (lexemeValue.equals(word)) return lexemes.tail
        else                          throw  UnexpectedLexemeException(lexemeValue, word)
    }
    
    val <T> List<T>.head : T get() = if (isNotEmpty()) first() else throw IllegasSyntaxException()
    val <T> List<T>.tail : List<T> get() = drop(1)

    open class SyntaxException(msg : String) : Throwable(msg)
    class UnexpectedLexemeException(lexeme: String, expected: String) : SyntaxException("Unexpected lemexe: $lexeme found when $expected was expected")
    class IsNotAnIdException(lexeme: String) : SyntaxException("Lemexe: $lexeme is not and ID when ID is expected")
    class IllegasSyntaxException() : SyntaxException("End of sentence while some other lexemes are expecred")
    
}