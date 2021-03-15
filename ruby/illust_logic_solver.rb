# 列の数 [列 → 行]
col_nums = [
    [5], [3, 1], [1, 1, 1], [1, 2], [5]
]
# 行の数 [行 → 列]
row_nums = [
    [3, 1], [2, 2], [3, 1], [1, 2], [5]
]

result_data = [
    [' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', ' '],
    [' ', ' ', ' ', ' ', ' ']
]

# result_data = [
#     ['o', 'o', 'o', 'x', 'o'],
#     ['o', 'o', 'x', 'o', 'o'],
#     ['o', 'o', 'o', 'x', 'o'],
#     ['o', 'x', 'x', 'o', 'o'],
#     ['o', 'o', 'o', 'o', 'o']
# ]

# 結果出力クラス
class ResultPrinter
    def initialize(row_nums, col_nums, result_data)
        @row_nums = row_nums
        @col_nums = col_nums
        @result_data = result_data
        
        @row_nums_size = @row_nums.collect{|r| r.size}.max
        @row_nums.each{|r| while(r.size < @row_nums_size) do r.unshift nil end}
            
        @col_nums_size = @col_nums.collect{|c| c.size}.max
        @col_nums.each{|c| while(c.size < @col_nums_size) do c.unshift nil end}

        @result_data_row_size = @result_data.size
        @result_data_col_size = @result_data.first.size

        @px_size = 20
    end

    def normalize_nums
    end

    def exec_output
        File.open('result.html', 'w') do |f|
            f.puts <<-EOS
<!doctype html>
<html lang="ja">
<head>
    <title>結果</title>
    <style>
        td.fixed {
            width: #{@px_size}px;
            height: #{@px_size}px;
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

        (@result_data_row_size + @col_nums_size).times do |r|
            data_row = r - @col_nums_size
            f.puts <<-EOS
                <tr>
            EOS
        
            (@result_data_col_size + @row_nums_size).times do |c|
                data_col = c - @row_nums_size
        
                # 左上
                if r == 0 && c == 0 then
                    f.puts <<-EOS
                    <td class="fixed" rowspan="#{@col_nums_size}" colspan="#{@row_nums_size}"></td>
                    EOS
        
                elsif r < @col_nums_size && c < @row_nums_size then
                    # スキップ
                
                # 列の数を出力
                elsif r < @col_nums_size then
                    f.puts <<-EOS
                    <td class="fixed _">#{@col_nums[data_col][data_row + @col_nums_size]}</td>
                    EOS
        
                # 行の数を出力
                elsif c < @row_nums_size then
                    f.puts <<-EOS
                    <td class="fixed _">#{@row_nums[data_row][data_col + @row_nums_size]}</td>
                    EOS
        
                # データを出力
                else
                    f.puts <<-EOS
                    <td class="fixed #{@result_data[data_row][data_col]}"></td>
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
    end
end

# 結果出力
rp = ResultPrinter.new(row_nums, col_nums, result_data)
rp.exec_output
