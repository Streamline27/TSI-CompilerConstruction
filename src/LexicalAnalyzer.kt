class LexicalAnalyzer(input: String) {


    private var input : String = input;

    private var position : Int = 0;

    private val keywordSet = KeywordSet();

    fun analyze() : List<Lexeme> {


        val lexemeList = ArrayList<Lexeme>();

        while (position < input.length) {

            val c = input[position];

            when {
                c.isDigit()          -> lexemeList += processNumber()
                c.isLetter()         -> lexemeList += processKeyword()
                c == '<'             -> lexemeList += processLeftArrow()
                c == ':'             -> lexemeList += processSemicolon()
                c in delimiterList() -> lexemeList += processDelimiter()
                c.isWhitespace()     -> position++
                else                 -> lexemeList += processUnrecognized();
            }
        }

        return lexemeList
    }

    private fun delimiterList() = arrayOf('+', '-', '.', '(', ')', '@', ':', ';', ',')

    private fun processKeyword() : Lexeme {

        var lexeme = "";
        var c = input[position];

        while (c.isLetter())  {
            lexeme += c;

            if (input.length == position) break;

            position++;
            c = input[position];
        }


        when {
            keywordSet.contains(lexeme) -> return Lexeme(lexeme, Lexeme.Type.KEYWORD)
            else                        -> return Lexeme(lexeme, Lexeme.Type.ID)
        }
    }

    private fun processNumber() : Lexeme {

        var lexeme = "";
        var c = input[position];

        while (c.isDigit())  {
            lexeme += c;

            if (input.length == position) break;

            position++;
            c = input[position];
        }

        return Lexeme(lexeme, Lexeme.Type.NUMBER)
    }

    private fun processLeftArrow() : Lexeme {

        var lexeme = ""
        lexeme += input[position];

        position++
        if (input.length != position) {

            val c = input[position];
            if (c == '>') {
                lexeme += c
                position ++
            };
        }
        
        return Lexeme(lexeme, Lexeme.Type.KEYWORD)

    }

    private fun processSemicolon() : Lexeme {

        var lexeme = ""
        lexeme += input[position];

        position++
        if (input.length != position) {

            val c = input[position];
            if (c == '=') {
                lexeme += c
                position++
            };
        }
        return Lexeme(lexeme, Lexeme.Type.KEYWORD)
    }


    private fun processDelimiter() : Lexeme {

        return Lexeme(input[position++].toString(), Lexeme.Type.KEYWORD)
    }

    private fun processUnrecognized() : Lexeme {

//        return Lexeme(input[position++].toString(), Lexeme.Type.ERROR)
        throw IllegalLexemeException(input[position++].toString())
    }

    open class IllegalLexemeException(lexeme : String) : Throwable("Can not process symbol '$lexeme'")
}