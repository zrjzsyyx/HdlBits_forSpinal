package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Dff8p extends HdlBitsExercise {
  val io = new Bundle {
    val d = in Bits(8 bits)
    val q = out Bits(8 bits)
  }

  val syncDomain = ClockDomain(
    clock  = clockDomain.clock,
    reset  = clockDomain.reset,
    config = ClockDomainConfig(
      resetKind = SYNC,
      clockEdge = FALLING
    )
  )
  val area = new ClockingArea(syncDomain) {
    val q_reg = Reg(Bits(8 bits)) init(0x34)
    q_reg := io.d
  }
  io.q := area.q_reg
}
