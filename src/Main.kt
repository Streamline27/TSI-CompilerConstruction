import sun.tools.jstat.SyntaxException
import java.io.File

fun main(args : Array<String>){

    
    
    try {
        val code = File("code-lab1.txt").readText();
        
        val lexemeResultList = LexicalAnalyzer(code).analyze()
        println("Lab1 code has successfully passed lexical analysis")
        
        TableGenerator(lexemeResultList, outputFile = "lexeme-output.txt").execute()
    }
    catch (e : LexicalAnalyzer.IllegalLexemeException) {
        println(e.message)
    }

    
    try {
        val statementCode = File("code-lab2.txt").readText()
        
        val lexemeResultList = LexicalAnalyzer(statementCode).analyze()
        println("Lab2 code has successfully passed lexical analysis")

        SyntaxAnalyzer(lexemeResultList).analyze()
        println("Lab2 code has successfully passed syntax analysis")
        
        TableGenerator(lexemeResultList, outputFile = "statement-output.txt").execute()
        
    }
    catch (e : LexicalAnalyzer.IllegalLexemeException) {
        println(e.message)
    }
    catch (e : SyntaxAnalyzer.SyntaxException) {
        println(e.message)
    }
    
    

}

