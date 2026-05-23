package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class LUT extends HdlBitsExercise {
  val io = new Bundle {
    val enable = in Bool()
    val S      = in Bool()
    val A      = in Bool()
    val B      = in Bool()
    val C      = in Bool()
    val Z      = out Bool()
  }

  val cd = ClockDomain(clock = clockDomain.clock)
  val area = new ClockingArea(cd) {
    val q = Vec(Reg(Bool()), 8)

    when(io.enable) {
      q(0) := io.S
      for (i <- 1 until 8) {
        q(i) := q(i - 1)
      }
    }
  }

  val addr = (io.C ## io.B ## io.A).asUInt
  io.Z := area.q(addr)
}
