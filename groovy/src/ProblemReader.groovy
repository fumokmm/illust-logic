import groovy.yaml.YamlSlurper

class ProblemReader {
    /** 問題ファイル */
    File file

    /** 行定義 */
    List<Defs> rowDefs

    /** 列定義 */
    List<Defs> columnDefs

    /**
     * 全定義
     * 　行と列の定義分すべて
     */
    List<Defs> allDefs

    ProblemReader(File file) {
        this.file = file
        // 読み込み実施
        this.read()
    }

    private ProblemReader read() {
        // 読み込み処理
        def ys = new YamlSlurper()
        def result = ys.parse(this.file)
        this.rowDefs = result.rows
        this.columnDefs = result.columns
        this.allDefs = this.rowDefs + this.columnDefs
        return this
    }

    List<Defs> getRowDefs() {
        return this.rowDefs
    }

    List<Defs> getColumnDefs() {
        return this.columnDefs
    }

    List<Defs> getAllDefs() {
        return this.allDefs
    }
}
