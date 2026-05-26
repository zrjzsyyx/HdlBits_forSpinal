package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class SimpleFSM extends HdlBitsExercise {
  val io = new Bundle {
    val reset = in Bool()
    val j     = in Bool()
    val k     = in Bool()
    val out_sig   = out Bool()
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = io.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )) {
    val state = Reg(Bool()) init(False)  // False=OFF, True=ON

    when(state) {
      when(io.k) { state := False }
    } otherwise {
      when(io.j) { state := True }
    }

    io.out_sig := state
  }
}
