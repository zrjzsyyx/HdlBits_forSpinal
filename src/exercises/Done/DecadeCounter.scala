package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class DecadeCounter extends HdlBitsExercise {
  val io = new Bundle {
    val q = out UInt(4 bits)
  }

  val syncDomain = ClockDomain(
    clock = clockDomain.clock,
    reset = clockDomain.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )
  val area = new ClockingArea(syncDomain) {
    val counter = Reg(UInt(4 bits)) init(0)
    counter := counter + 1
    when(counter === 9) {
      counter := 0
    }
  }
  io.q := area.counter
}
