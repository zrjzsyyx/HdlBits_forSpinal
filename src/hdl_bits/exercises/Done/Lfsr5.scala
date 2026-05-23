package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Lfsr5 extends HdlBitsExercise {
  val io = new Bundle {
    val reset = in Bool()
    val q     = out Bits(5 bits)
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = io.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )) {
    val q_reg = Reg(Bits(5 bits)) init(1)

    q_reg := q_reg(0) ## q_reg(4) ## (q_reg(3) ^ q_reg(0)) ## q_reg(2) ## q_reg(1)
  }

  io.q := syncArea.q_reg
}
