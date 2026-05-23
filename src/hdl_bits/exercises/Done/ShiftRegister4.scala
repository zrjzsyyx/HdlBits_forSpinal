package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class ShiftRegister4 extends HdlBitsExercise {
  val io = new Bundle {
    val areset = in Bool()
    val load   = in Bool()
    val ena    = in Bool()
    val data   = in Bits(4 bits)
    val q      = out Bits(4 bits)
  }

  val asyncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = io.areset,
    config = ClockDomainConfig(resetKind = ASYNC)
  )) {
    val shift_reg = Reg(Bits(4 bits)) init(0)

    when(io.load) {
      shift_reg := io.data
    } elsewhen(io.ena) {
      shift_reg := B"0" ## shift_reg(3 downto 1)
    }
  }

  io.q := asyncArea.shift_reg
}
