package hdl_bits

import spinal.core._

/** Unified Verilog generator for all HDL Bits exercises. Usage: sbt "runMain
  * hdl_bits.Gen StepOne"
  *
  * Add new exercises to the `exercises` map below.
  */
object Gen extends App {
  val registry: Map[String, () => Component] = Map(
    "Mux2to1" -> (() => new hdl_bits.exercises.Mux2to1()),
    "Mux256to1" -> (() => new hdl_bits.exercises.Mux256to1()),
    "Fadd" -> (() => new hdl_bits.exercises.Fadd()),
    "Adder" -> (() => new hdl_bits.exercises.Adder()),
    "Dff8" -> (() => new hdl_bits.exercises.Dff8()),
    "Dff8p" -> (() => new hdl_bits.exercises.Dff8p()),
    "Muxdiff" -> (() => new hdl_bits.exercises.Muxdiff()),
    "Muxdiff2" -> (() => new hdl_bits.exercises.Muxdiff2()),
    "EdgeDetect" -> (() => new hdl_bits.exercises.EdgeDetect()),
    "Counter" -> (() => new hdl_bits.exercises.Counter()),
    "DecadeCounter" -> (() => new hdl_bits.exercises.DecadeCounter()),
    "CounterBCD" -> (() => new hdl_bits.exercises.CounterBCD()),
    "_12hClock" -> (() => new hdl_bits.exercises._12hClock()),
    "ShiftRegister4" -> (() => new hdl_bits.exercises.ShiftRegister4()),
    "Lfsr5" -> (() => new hdl_bits.exercises.Lfsr5()),
    "LFSR32" -> (() => new hdl_bits.exercises.LFSR32()),
    "LUT" -> (() => new hdl_bits.exercises.LUT()),
    "Rule90" -> (() => new hdl_bits.exercises.Rule90()),
    "GameOfLife" -> (() => new hdl_bits.exercises.GameOfLife()),
    "SimpleFSM" -> (() => new hdl_bits.exercises.SimpleFSM()),
    "FsmCombo" -> (() => new hdl_bits.exercises.FsmCombo()),
    "Fsm3onehot" -> (() => new hdl_bits.exercises.Fsm3onehot()),
    "Lemmings1" -> (() => new hdl_bits.exercises.Lemmings1()),
    "Ps2Parser" -> (() => new hdl_bits.exercises.Ps2Parser()),
    "SerialRx" -> (() => new hdl_bits.exercises.SerialRx()),
    "SerialRxFsm" -> (() => new hdl_bits.exercises.SerialRxFsm()),
    "SerialRxParity" -> (() => new hdl_bits.exercises.SerialRxParity()),
    "Hdlc" -> (() => new hdl_bits.exercises.Hdlc())
  )

  if (args.length != 1 || !registry.contains(args(0))) {
    println(s"Usage: sbt 'runMain hdl_bits.Gen <exercise>'")
    println(s"Available: ${registry.keys.toList.sorted.mkString(", ")}")
    sys.exit(1)
  }

  val name = args(0)
  println(s"Generating Verilog for: $name")
  SpinalConfig(targetDirectory = "gen").generateVerilog(
    registry(name)()
  )
  println(s"Done → gen/top_module.v")
}
