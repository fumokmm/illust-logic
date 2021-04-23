class Field {
    int rowSize
    int columnSize
    List<Defs> rowDefs
    List<Defs> columnDefs
    List<List<Cell>> cells
    List<Cell> flattenCells

    Field(int rowSize, int columnSize, List<Defs> rowDefs, List<Defs> columnDefs) {
        // 行・列サイズを設定
        this.rowSize = rowSize
        this.columnSize = columnSize

        // 行・列定義を設定
        this.rowDefs = rowDefs.collect { new Defs(it, 'row') }
        this.columnDefs = columnDefs.collect { new Defs(it, 'column') }

        // Defsにフィールドを設定
        this.rowDefs.each { defs ->
            defs.field = this
            defs.createCandidateList()
        }
        this.columnDefs.each { defs ->
            defs.field = this
            defs.createCandidateList()
        }

        // セルを作成
        this.createCells()
    }

    private void createCells() {
        this.cells = (0..<this.rowSize).collect { r ->
            (0..<this.columnSize).collect { c ->
                new Cell(0) // 未確定
            }
        }
        this.flattenCells = this.cells.flatten()

        // Defsに対応付ける
        this.rowDefs.eachWithIndex{ Defs defs, int i ->
            defs.correspondingCellList = this.cells[i]
        }
        this.columnDefs.eachWithIndex{ Defs defs, int i ->
            defs.correspondingCellList = this.cells.collect{ it[i] }
        }
    }

    Cell cellAt(int rowIdx, int columnIdx) {
        this.cells[rowIdx][columnIdx]
    }

    String display() {
        this.cells.collect { r ->
            r.collect{ cell -> cell.toDisplayString() }
        }.join('\n')
    }

    boolean isAllFixed() {
        // すべてのセルが未確定でなくなったら終了
        this.flattenCells.every{ it != Cell.UNDEF }
    }

    def getSummary() {
        this.flattenCells.groupBy{ it.value }.collectEntries{ entry -> [entry.key, entry.value.size() ] }
    }

}
