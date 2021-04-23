puts Dir.pwd

require './illust_logic_solver'

# 行の数 [行 → 列]
row_nums = [
    [3, 1], [2, 2], [3, 1], [1, 2], [5]
]
# 列の数 [列 → 行]
col_nums = [
    [5], [3, 1], [1, 1, 1], [1, 2], [5]
]

###########
# 問題
###########

# 解析クラスで解析
solver = Solver.new(row_nums, col_nums)
result_data = solver.solve

# result_data = [
#     ['o', 'o', 'o', 'x', 'o'],
#     ['o', 'o', 'x', 'o', 'o'],
#     ['o', 'o', 'o', 'x', 'o'],
#     ['o', 'x', 'x', 'o', 'o'],
#     ['o', 'o', 'o', 'o', 'o']
# ]

# 結果出力
rp = ResultPrinter.new(row_nums, col_nums, result_data)
rp.exec_output

puts "finished."
