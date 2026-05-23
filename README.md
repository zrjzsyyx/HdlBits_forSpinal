# HdlBits for SpinalHDL

用 [SpinalHDL](https://github.com/SpinalHDL/SpinalHDL) 实现 [HDL Bits](https://hdlbits.01xz.net/) 题目，再将 Spinal 生成的 Verilog 提交到 HDL Bits 验证。

## 环境

- Scala 2.13 / sbt 1.11
- SpinalHDL 1.11.0
- Verilator 5.037（lint 用）
- JDK 17

## 项目结构

```
HdlBits/
├── build.sbt                    # sbt 构建配置
├── gen_hdlbits.sh               # 一键生成 + lint 脚本
├── src/hdl_bits/
│   ├── HdlBitsExercise.scala    # 基类（noIoPrefix, setDefinitionName）
│   ├── Gen.scala                # 习题注册表 + Verilog 生成器
│   └── exercises/
│       ├── Done/                # 已完成的习题
│       └── *.scala              # 进行中的习题
└── gen/hdl_bits/
    └── top_module.v             # 生成的 Verilog（自动）
```

## 使用

### 编译并生成 Verilog

```bash
cd HdlBits
./gen_hdlbits.sh <习题名>
```

例如：

```bash
./gen_hdlbits.sh Lfsr5
```

### 附加选项

```bash
./gen_hdlbits.sh Lfsr5 --lint    # 生成后跑 Verilator lint
./gen_hdlbits.sh Lfsr5 --show    # 生成后在终端打印 Verilog
./gen_hdlbits.sh Lfsr5 --lint --show
```

### 生成后在 HDL Bits 验证

打开 `gen/hdl_bits/top_module.v`，复制内容粘贴到 HDL Bits 网页编辑器，点击 Submit。

### 添加新习题

1. 在 `src/hdl_bits/exercises/` 下创建 `YourExercise.scala`，继承 `HdlBitsExercise`
2. 在 `src/hdl_bits/Gen.scala` 的 `registry` 中注册
3. 执行 `./gen_hdlbits.sh YourExercise --lint --show`

## 注意事项

- 端口名避免 `in`、`out`（Spinal 保留字），如需使用以 `_sig` 后缀代替，再通过 sed 重命名
- 无 reset 的电路用 `ClockDomain(clock = clockDomain.clock)` 避免生成 reset 端口
- 使用 `ClockingArea` + `ClockDomainConfig(resetKind = SYNC/ASYNC)` 控制复位类型

## 已完成

| 习题 | 类型 |
|------|------|
| Mux2to1 | 组合逻辑 - 2选1多路选择器 |
| Mux256to1 | 组合逻辑 - 256选1多路选择器 |
| Fadd | 组合逻辑 - 全加器 |
| Adder | 组合逻辑 - 4位加法器 |
| Dff8 | 时序逻辑 - 8 D触发器（同步复位）|
| Dff8p | 时序逻辑 - 8 D触发器（下降沿，复位到0x34）|
| Muxdiff | 时序逻辑 - 带寄存器的多路选择器 |
| Muxdiff2 | 时序逻辑 - 带寄存器的多路选择器变体 |
| EdgeDetect | 时序逻辑 - 边沿检测 |
| Counter | BCD计数器 + BlackBox |
| DecadeCounter | 0-9计数器 |
| CounterBCD | 4位BCD计数器 |
| \_12hClock | 12小时时钟（BCD，AM/PM）|
| ShiftRegister4 | 4位移位寄存器（异步复位）|
| Lfsr5 | 5位Galois LFSR |
| LFSR32 | 32位Galois LFSR |
| LUT | 8位移位寄存器 + 3输入LUT |
