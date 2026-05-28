package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class LFSR32 extends HdlBitsExercise {
  val io = new Bundle {
    val reset = in Bool()
    val q     = out Bits(32 bits)
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = io.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )) {
    val q_reg = Reg(Bits(32 bits)) init(1)

    val taps = Set(0, 1, 21) // 0-indexed taps: positions 1, 2, 22 (1-indexed, excl. MSB)
    val q_next = Bits(32 bits)
    q_next(31) := q_reg(0) // tap at position 32, XOR with 0 = passthrough
    for (i <- 0 until 31) {
      if (taps.contains(i)) {
        q_next(i) := q_reg(i + 1) ^ q_reg(0)
      } else {
        q_next(i) := q_reg(i + 1)
      }
    }
    q_reg := q_next
  }

  io.q := syncArea.q_reg
}
