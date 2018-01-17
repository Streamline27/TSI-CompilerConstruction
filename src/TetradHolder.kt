object TetradHolder {

    private var variableCount = 1
    
    val tetradList = arrayListOf<Tetrad>()
    lateinit var savedVariable : String

    fun allocateVariable() : String {
        return "T" + variableCount++;
    }
    
    fun createOperation(operation: String, arg1: String, arg2: String = "", result: String = "") {
        tetradList.add(Tetrad(operation, arg1, arg2, result))
    }

    
    data class Tetrad(val operator: String, val operand1: String, val operand2: String, val destination: String) {
        override fun toString(): String = "[$operator, $operand1, $operand2, $destination]"
    }
}