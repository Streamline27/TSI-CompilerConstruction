import java.io.File

fun main(args : Array<String>){
    
    lateinit var lexemeResultList : List<Lexeme>
    
//    val code = File("/Users/Vladislav/Development/Citadele/LexicalAnalyzer/code.txt").readText();
//
//    lexemeResultList = LexicalAnalyzer(code).analyze()
//    LexemeTableGenerator(lexemeResultList).execute()

    val statementCode = File("/Users/Vladislav/Development/Citadele/LexicalAnalyzer/statement-code.txt").readText();

    lexemeResultList = LexicalAnalyzer(statementCode).analyze()
    LexemeTableGenerator(lexemeResultList).execute()
    
    val hasValidSyntax = SyntaxAnalyzer(lexemeResultList).analyze()
    
    if (hasValidSyntax) println("Statement has valid syntax")
    else                println("Syntax is invalid")
    

}
