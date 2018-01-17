class SyntaxAnalyzer(val lexemes : List<Lexeme>) {
    
    fun analyze() {
        var remainingSentence = lexemes;
        
        while (remainingSentence.isNotEmpty()) remainingSentence = nextIsOperator(remainingSentence)
    }
    
    
    fun nextIsIfStatement(sentence: List<Lexeme>) : List<Lexeme>{

        var remainingSentence = nextIsExpected("if",   sentence)
            remainingSentence = nextIsLogicalCondition(remainingSentence)
            remainingSentence = nextIsExpected("then", remainingSentence)
        
        TetradHolder.createOperation("IF", TetradHolder.savedVariable)
        
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
        val to = sentence.head.value
        
        remainingSentence = nextIsExpected(":=", remainingSentence);
        
        remainingSentence = nextIsAnId(remainingSentence)
        val from = sentence.head.value
        
        remainingSentence = nextIsExpected(";", remainingSentence)
        
        TetradHolder.createOperation("ASSIGN", to, "", from)
        
        return remainingSentence
    }

    private fun nextIsLogicalCondition(sentence: List<Lexeme>) : List<Lexeme>{
        
        var remainingSentence = nextIsValue(sentence)
        val arg1 = TetradHolder.savedVariable
        
        remainingSentence = nextIsExpected("<>", remainingSentence)
        
        remainingSentence = nextIsValue(remainingSentence)
        val arg2 = TetradHolder.savedVariable
        
        val res = TetradHolder.allocateVariable()
        TetradHolder.createOperation("NOTEQ", arg1, arg2, res)
        TetradHolder.savedVariable = res
        
        return remainingSentence
        
    }

    private fun nextIsValue(sentence: List<Lexeme>): List<Lexeme> {
        
        try {
            return nextIsFunctionExpression(sentence)
        }
        catch (e : SyntaxException) {
            val remainingSentence = nextIsAnId(sentence)
            
            val value = sentence.head.value
            TetradHolder.savedVariable = value
            
            return remainingSentence
        }
    }

    private fun nextIsFunctionExpression(sentence: List<Lexeme>): List<Lexeme> {
        
        var remainingSentence = nextIsAnId(sentence)
            remainingSentence = nextIsExpected("(", remainingSentence)
            remainingSentence = nextIsArguments(remainingSentence)
            remainingSentence = nextIsExpected(")", remainingSentence)
        
        val funRes = TetradHolder.allocateVariable()
        TetradHolder.createOperation("CALL", sentence.head.value, TetradHolder.savedVariable, funRes)
        TetradHolder.savedVariable = funRes
        
        return remainingSentence
    }

    private fun nextIsArguments(sentence: List<Lexeme>): List<Lexeme> {
        
        val stash = TetradHolder.allocateVariable();
        
        var remainingSentence = nextIsArgument(sentence)
        val arg = TetradHolder.savedVariable
        
        TetradHolder.createOperation("STASH", arg, "", stash)
        
        while (remainingSentence.head.value.equals(",")) {
            
            remainingSentence = nextIsExpected(",", remainingSentence)
            remainingSentence = nextIsArgument(remainingSentence)
            
            val argNext = TetradHolder.savedVariable
            TetradHolder.createOperation("STASH", argNext, "", stash)
        }
        
        TetradHolder.savedVariable = stash
        return remainingSentence
    }

    private fun nextIsArgument(sentence: List<Lexeme>): List<Lexeme> {
        try {
            var remainingSentence = nextIsExpected("@", sentence);
            
            val addrOf = remainingSentence.head.value
            remainingSentence = nextIsAnId(remainingSentence);
            
            val res = TetradHolder.allocateVariable()
            TetradHolder.createOperation("ADDROF", addrOf, "", res)
            TetradHolder.savedVariable = res
            
            return remainingSentence
        }
        catch (e : SyntaxException) {
            val remainingSentence = nextIsAnId(sentence);
            TetradHolder.savedVariable = sentence.head.value
            
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
    
    val <T> List<T>.head : T get() = if (isNotEmpty()) first() else throw IllegalSyntaxException()
    val <T> List<T>.tail : List<T> get() = drop(1)

    open class SyntaxException(msg : String) : Throwable(msg)
    class UnexpectedLexemeException(lexeme: String, expected: String) : SyntaxException("Unexpected lemexe: $lexeme found when $expected was expected")
    class IsNotAnIdException(lexeme: String) : SyntaxException("Lemexe: $lexeme is not and ID when ID is expected")
    class IllegalSyntaxException : SyntaxException("End of sentence while some other lexemes are expecred")
    
}