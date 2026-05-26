package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Rule90 extends HdlBitsExercise {
  val io = new Bundle {
    val load = in Bool()
    val data = in Bits(512 bits)
    val q    = out Bits(512 bits)
  }

  val cd = ClockDomain(clock = clockDomain.clock)
  val area = new ClockingArea(cd) {
    val q_reg = Reg(Bits(512 bits))

    when(io.load) {
      q_reg := io.data
    } otherwise {
      val left  = q_reg(510 downto 0) ## B"0"   // q[i-1], left boundary = 0
      val right = B"0" ## q_reg(511 downto 1)   // q[i+1], right boundary = 0
      q_reg := left ^ right
    }
  }

  io.q := area.q_reg
}
