import java.io.File

class TableGenerator(val lexemes: List<Lexeme>, val outputFile : String) {

    private val FORMAT = "%-7s |  %-17s | \n"

    private val keywords     = lexemes.filter { it.kind == Lexeme.Type.KEYWORD } .toSet()
    private val literals     = lexemes.filter { it.kind == Lexeme.Type.NUMBER } .toSet()
    private val identifiers  = lexemes.filter { it.kind == Lexeme.Type.ID } .toSet()
    private val dictionary = KeywordSet();

    fun execute() {
        
        File(outputFile).printWriter().use { out ->
            
            out.println("\nDICTIONARY")
            out.printf(FORMAT, "index", "keyword")
            dictionary.forEachIndexed{ index, keyword -> out.printf(FORMAT, index, keyword) }
//
//            out.println("\nKEYWORDS")
//            out.printf(FORMAT, "index", "keyword")
//            keywords.forEachIndexed{ index, lexeme -> out.printf(FORMAT, index, lexeme.value) }

            out.println("\nLITERALS")
            out.printf(FORMAT, "index", "literal")
            literals.forEachIndexed{ index, lexeme -> out.printf(FORMAT, index, lexeme.value) }

            out.println("\nIDENTIFIERS")
            out.printf(FORMAT, "index", "identifiers")
            identifiers.forEachIndexed{ index, lexeme -> out.printf(FORMAT, index, lexeme.value) }
            
            out.println("\nRESULTS")
            out.printf("%-15s |  %-7s | \n", "lexeme", "kind")
            lexemes.forEach{ result -> out.println(formatOutput(result.value, result.kind)) }
            
            

            if (TetradHolder.tetradList.isNotEmpty()) {

                out.println("\nTetrads")

                TetradHolder.tetradList.forEach {
                    out.println(it)
                }
            }

        }
    }

    fun formatOutput(lexeme: String, kind: Lexeme.Type): String {
        
        val KEYWORD_FORMAT = "%-15s |  %-7s |  keyword id: %-7s"
        val DEFAULT_FORMAT = "%-15s |  %-7s |"

        when {
            kind == Lexeme.Type.KEYWORD  ->  return String.format(KEYWORD_FORMAT, lexeme, kind.name, dictionary.indexOf(lexeme));
            else                         ->  return String.format(DEFAULT_FORMAT, lexeme, kind.name)
        }

    }

}