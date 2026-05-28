package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Lemmings1 extends HdlBitsExercise {
  val io = new Bundle {
    val areset     = in Bool()
    val bump_left  = in Bool()
    val bump_right = in Bool()
    val walk_left  = out Bool()
    val walk_right = out Bool()
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = io.areset,
    config = ClockDomainConfig(resetKind = ASYNC)
  )) {
    val state = Reg(Bool()) init(False)  // False=WL(walk left), True=WR(walk right)

    when(state) {  // WR
      when(io.bump_right) { state := False }
    } otherwise {   // WL
      when(io.bump_left)  { state := True }
    }

    io.walk_left  := !state
    io.walk_right := state
  }
}
