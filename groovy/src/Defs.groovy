import groovy.transform.ToString

@ToString
class Defs {
    Field field
    List<Integer> nums
    List<List<Cell>> candidateList
    List<Cell> correspondingCellList
    String direction  // "row" or "column"

    Defs(List<Integer> list, direction) {
        this.nums = list
        this.direction = direction
    }

    void setField(Field field) {
        this.field = field
    }

    int getFieldSize() {
        this.direction == "row" ? this.field.columnSize : this.field.rowSize
    }

    private void createCandidateList() {
        if (this.nums.size() == 0) {
            this.candidateList = [(0..<this.fieldSize).collect{ new Cell(-1) }]
            return
        }

        // [2, 1, 3] =>合計6, スペースの最小数=2 (3 - 1)
        int n = this.fieldSize - (this.nums.sum() + (this.nums.size() - 1))
        // スペース数の配列数 = size() + 1
        int m = this.nums.size() + 1

        def spaceSizeList = createSpaceSizeList(n, m)
        this.candidateList = toCandidateList(spaceSizeList)
    }

    /**
     * [ (sf), 2, (s1), 1, (s2), 3 , (se)]
     *  2 + 1 + 3 = 6;  10 - 6 = 4
     * sf + s2 + s3 + se = 4 ( ただし、sfとseは0以上, s1, s2,…は1以上 ）
     * スペース4つを足して4になる順列の組み合わせ
     * ベース: [0, 1, 1, 0] ←ベース。ここで既に2使ってしまったので、4-2=2；あと2を4つの位置に振り分けるパターンとなる
     *
     * [0] ->
     *   [0, 0] ->
     *      [0, 0, 0] -> [0, 0, 0, 2]
     *   [0, 1] ->
     *      [0, 1, 0] -> [0, 1, 0, 1]
     *      [0, 1, 1] -> [0, 1, 1, 0]
     *   [0, 2] -> [0, 2, 0, 0]
     * [1] ->
     * [2] -> [2, 0, 0, 0]
     *
     * [1, 1, -1, 2, -1,  3, 3, 3, -1, -1
     * [1, 1, -1, 2, -1, -1, 3, 3, 3, -1]
     * [1, 1, -1, 2, -1, -1, -1, 3, 3, 3]
     *
     * @param defs
     * @param spaceSizeList
     * @return
     */
    private List<Cell> toCandidateList(List<Integer> spaceSizeList) {
        spaceSizeList.collect { spc ->
            def cellList = Cell.createCellList(this.fieldSize, -1)
            assert this.size() + 1 == spc.size()
            int idx = 0
            for (int i in 0..<this.size()) {
                spc[i].times { cellList[idx++] = Cell.OFF }
                this.nums[i].times { cellList[idx++] = new Cell(i + 1) }
            }
            spc[-1].times { cellList[idx++] = Cell.OFF }
            cellList
        }
    }

    private static List<Integer> createSpaceSizeList(n, m) {
        // [0, 1, 1, ... , 1, 0] 最初と最後だけ0であとは全て1
        def baseSpace = (0..<m).collect { 1 }
        baseSpace[0] = 0
        baseSpace[-1] = 0

        def result = []
        _spaceSize(result, [], n, m)
        //println "result=${result}"
        result = result.collect {
            [it, baseSpace].transpose().collect { it.sum() }
        }
        result
    }

    // https://detail.chiebukuro.yahoo.co.jp/qa/question_detail/q14155893174
    // 全て足してnになるm個の整数
    static void _spaceSize(result, list, n, m) {
        def makeTmpList = { def l = []; l.addAll(list); l }
        def remain = { n - (list.sum() ?: 0) }

        // サイズがmになったら結果をそのまま返却
        if (list.size() == m) {
            result << makeTmpList()
            return
        }
        // サイズがm-1になったら、nになるように調整して返却
        if (list.size() == m - 1) {
            def tmpList = makeTmpList()
            tmpList << remain()
            _spaceSize(result, tmpList, n, m)
            return
        }
        // サイズがm-2以下の場合、0～n(ただし、最大nを超えないよう考慮)を選択して再起呼び出し
        if (list.size() <= m - 2) {
            for (i in 0..remain()) {
                def tmpList = makeTmpList()
                tmpList << i
                _spaceSize(result, tmpList, n, m)
            }
            return
        }

        // それ以外はエラー
        throw new Exception("listのサイズが大きすぎます: ${list.size()}")
    }

    /**
     * 候補の置き換え
     * @param newList 新しい候補リスト
     */
    void setCandidateList(List<List<Cell>> newList) {
        this.candidateList = newList
    }

    int size() { return this.nums.size() }
}
