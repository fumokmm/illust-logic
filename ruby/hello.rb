px_size = 20

# 列の数 [列 → 行]
column_nums = [
    [5], [3, 1], [1, 1, 1], [1, 1], [5]
]
# 行の数 [行 → 列]
row_nums = [
    [3, 1], [2, 2], [3, 1], [1, 2], [3, 1]
]

data_row_size = 5
data_column_size = 5

result_data = [
    ['o', 'o', 'o', 'x', 'o'],
    ['o', 'o', 'x', 'o', 'o'],
    ['o', 'o', 'o', 'x', 'o'],
    ['o', 'x', 'x', 'o', 'o'],
    ['o', 'o', 'o', 'x', 'o']
]

column_nums_len = column_nums.collect{|it| it.size}.max
column_nums.each{|it| while(it.size < column_nums_len) do it.unshift nil end}
# puts column_nums_len
row_nums_len = row_nums.collect{|it| it.size}.max
row_nums.each{|it| while(it.size < row_nums_len) do it.push nil end}
# puts row_nums_len

File.open('result.html', 'w') do |f|
    f.puts <<-EOS
<!doctype html>
<html lang="ja">
<head>
    <title>結果</title>
    <style>
        td.fixed {
            width: #{px_size}px;
            height: #{px_size}px;
            text-align: center;
            vertical-align: center;
        }
        td.fixed.o {
            background-color: black;
        }
        td.fixed.x {
            background-color: silver;
        }
        td.fixed._ {
            background-color: white;
        }
    </style>
</head>
<body>
    <p>結果です</p>
    <table border="1">
EOS

(data_row_size + column_nums_len).times do |r|
    data_row = r - column_nums_len
    f.puts <<-EOS
        <tr>
    EOS

    (data_column_size + row_nums_len).times do |c|
        data_col = c - row_nums_len

        # 左上
        if r == 0 && c == 0 then
            f.puts <<-EOS
                <td class="fixed" rowspan="#{column_nums_len}" colspan="#{row_nums_len}"></td>
            EOS

        elsif r < column_nums_len && c < row_nums_len then
            # スキップ
        
        # 列の数を出力
        elsif r < column_nums_len then
            f.puts <<-EOS
                <td class="fixed _">#{column_nums[data_col][data_row + column_nums_len]}</td>
            EOS

        # 行の数を出力
        elsif c < row_nums_len then
            f.puts <<-EOS
                <td class="fixed _">#{row_nums[data_row][data_col + row_nums_len]}</td>
            EOS

        # データを出力
        else
            f.puts <<-EOS
                <td class="fixed #{result_data[data_row][data_col]}"></td>
            EOS
        end
    end

    f.puts <<-EOS
        </tr>
    EOS
end

f.puts <<-EOS
    </table>
</body>
</html>
EOS

end
