package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class GameOfLife extends HdlBitsExercise {
  val io = new Bundle {
    val load = in Bool()
    val data = in Bits(256 bits)
    val q    = out Bits(256 bits)
  }

  val cd = ClockDomain(clock = clockDomain.clock)
  val area = new ClockingArea(cd) {
    val q_reg = Reg(Bits(256 bits))

    val q_next = Bits(256 bits)
    for (r <- 0 until 16) {
      for (c <- 0 until 16) {
        val idx = r * 16 + c

        val rr = (rrr: Int) => (r + rrr) & 15
        val cc = (ccc: Int) => (c + ccc) & 15

        val n00 = q_reg(rr(-1) * 16 + cc(-1)).asUInt
        val n01 = q_reg(rr(-1) * 16 + cc( 0)).asUInt
        val n02 = q_reg(rr(-1) * 16 + cc( 1)).asUInt
        val n10 = q_reg(rr( 0) * 16 + cc(-1)).asUInt
        val n12 = q_reg(rr( 0) * 16 + cc( 1)).asUInt
        val n20 = q_reg(rr( 1) * 16 + cc(-1)).asUInt
        val n21 = q_reg(rr( 1) * 16 + cc( 0)).asUInt
        val n22 = q_reg(rr( 1) * 16 + cc( 1)).asUInt

        val sum = n00 +^ n01 +^ n02 +^ n10 +^ n12 +^ n20 +^ n21 +^ n22
        q_next(idx) := (sum === 3) || (sum === 2 && q_reg(idx))
      }
    }

    when(io.load) {
      q_reg := io.data
    } otherwise {
      q_reg := q_next
    }
  }

  io.q := area.q_reg
}
