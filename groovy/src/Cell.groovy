import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString
@EqualsAndHashCode
class Cell {
    static Cell OFF = new Cell(-1)
    static Cell ON = new Cell(1)
    static Cell UNDEF = new Cell(0)

    /**
     * セルの値
     * -1: 入らない, 0: 未確定, 1以上: 確定
     */
    int value = 0

    Cell(int value) {
        this.value = value
    }

    public void setValue(int value) {
        this.value = value
    }
    public int getValue() {
        return this.value
    }


    /** 指定サイズのセルリストを取得 */
    static List<Cell> createCellList(int size, int defaultValue) {
        (0..<size).collect { new Cell(defaultValue) }
    }

    /** 表示用文字列取得 */
    String toDisplayString() {
        switch (value) {
            case { it < 0 }: '×'; break
            case { it == 0 }: '□'; break
            case { it > 0 }: '■'; break
        }
    }

    void on() { this.value = 1 }
    void off() { this.value = -1 }
    void undef() { this.value = 0 }
}
