import groovy.transform.ToString

import java.text.DecimalFormat

def problemFileName = args[0]
def startTime = System.currentTimeMillis() // 開始時刻

println "問題: ${problemFileName}"

def reader = new ProblemReader(new File("./data/${problemFileName}")).read()
def field = new Field(reader.rowDefs.size(), reader.columnDefs.size(), reader.rowDefs, reader.columnDefs)
def analyzer = new Analyzer(field)

// 初期状態
def preSummary = null
int turn = 1
while (true) {
    // 確定状態から候補を絞り込み
    analyzer.filterCandidate()
    // 確定処理
    analyzer.fixCell()

    // 結果取得
    def currentSummary = field.summary

    // 解析後も状態に変化がない場合は解けない
    if (currentSummary == preSummary) {
        println 'impossible.'
        break
    }

    // 結果表示
    println "${turn}ターン目"
    println field.display()
    println "状態: ${currentSummary}"
    println ''

    // 終了判定
    // 解き終わった場合
    if (field.allFixed) {
        println 'finished.'
        break

    // 解析を続ける
    } else {
        preSummary = currentSummary
        turn++
    }
}

def endTime = System.currentTimeMillis() // 終了時刻
println "処理時間(秒): ${new DecimalFormat('#,##0.000').format((endTime - startTime) / 1000)}"