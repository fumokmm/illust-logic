# イラストロジック解析クラス
class Solver
    attr_accessor :result_data

    def initialize(row_nums, col_nums)
        @row_nums = row_nums
        @col_nums = col_nums

        @result_data_row_size = @col_nums.size
        @result_data_col_size = @row_nums.size
    end

    def check_row_complete(row_idx)
        row_num = @row_nums[row_idx]
        space_size = row_num.size - 1
        if row_num.sum + space_size == @result_data_col_size then
            r_cur = 0
            row_num.each do |r_num|
                r_num.times do |i|
                    @result_data[row_idx][r_cur] = 'o'
                    r_cur += 1
                end
                if r_cur < @result_data_col_size then
                    @result_data[row_idx][r_cur] = 'x'
                    r_cur += 1
                end
            end
        end
    end

    def solve
        # 空(nil)で初期化
        @result_data = Array.new(@result_data_row_size).map do |r|
            Array.new(@result_data_col_size)
        end

        # 行の完成チェック
        @result_data_row_size.times do |row_idx|
            check_row_complete(row_idx)
        end

        return @result_data
    end

end

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

