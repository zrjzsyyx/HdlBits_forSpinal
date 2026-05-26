package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Fsm3onehot extends HdlBitsExercise {
  val io = new Bundle {
    val in_sig = in Bool ()
    val state = in UInt (4 bits)
    val next_state = out UInt (4 bits)
    val out_sig = out Bool ()
  }

  io.out_sig := io.state(3)  // D=4'b1000 → bit 3

  io.next_state(0) := (io.state(0) | io.state(2)) & ~io.in_sig  // A ← A/C when in=0
  io.next_state(1) := (io.state(0) | io.state(1) | io.state(3)) & io.in_sig  // B ← A/B/D when in=1
  io.next_state(2) := (io.state(1) | io.state(3)) & ~io.in_sig  // C ← B/D when in=0
  io.next_state(3) := io.state(2) & io.in_sig  // D ← C when in=1

}
