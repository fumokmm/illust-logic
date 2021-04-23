import groovy.transform.ToString

@ToString
class Analyzer {
    private Field field

    Analyzer(Field field) {
        this.field = field
    }

    /**
     * 確定状態からのDefsの候補の絞り込み
     */
    void filterCandidate() {
        (this.field.rowDefs + this.field.columnDefs).each { Defs defs ->
            defs.correspondingCellList.eachWithIndex{ Cell cell, int i ->
                // 未確定の場合は飛ばす
                if (cell == Cell.UNDEF) return

                // 確定しているCellと一致する候補のみに絞り込む
                defs.candidateList = defs.candidateList.findAll {
                    //println "${it} ${it[i]} ${cell} :: ${it[i] == cell}"
                    return cell == Cell.ON ? it[i].value > 0 : it[i] == cell
                }
            }
        }
    }

    /**
     * セルを確定していく
     */
    void fixCell() {
        (this.field.rowDefs + this.field.columnDefs).each { Defs defs ->
            def first = defs.candidateList.first()

            // Defsの持つ候補が1つのみなら、その候補で全確定
            if (defs.candidateList.size() == 1) {
                for (int i in 0..<first.size()) {
                    if (first[i].value > 0) {
                        defs.correspondingCellList[i].on()
                    } else {
                        defs.correspondingCellList[i].off()
                    }
                }

                // Defsの持つ候補を複数ある場合、
                // 縦串で見たとき、すべて同じブロックとなっているなら、そこは確定
            } else if (defs.candidateList.size() > 1) {
                for (int i in 0..<first.size()) {
                    def uniqued = defs.candidateList*.getAt(i).unique()
                    if (uniqued.size() == 1) {
                        if (uniqued.first().value > 0) {
                            defs.correspondingCellList[i].on()
                        } else {
                            defs.correspondingCellList[i].off()
                        }
                    }
                }
            }
        }
    }

    /**
     * 候補の消し込み　(確定済みの内容をもとに、候補を消し込んでいく)
     */
    void reduceCandidate() {
        // 既に決定された内容とマッチしない候補を除外する
    }

}