package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class SerialRx extends HdlBitsExercise {
  val io = new Bundle {
    val clk = in Bool ()
    val in_sig = in Bool ()
    val reset = in Bool ()
    val done = out Bool ()
    val out_byte = out UInt (8 bits)
  }

  val syncArea = new ClockingArea(
    ClockDomain(
      clock = io.clk,
      reset = io.reset,
      config = ClockDomainConfig(resetKind = SYNC)
    )
  ) {
    val state = Reg(UInt(2 bits)) init (0)
    val cnt = Reg(UInt(3 bits))
    val done_reg = Reg(Bool()) init (False)
    val data_reg = Reg(UInt(8 bits))

    io.done := done_reg
    io.out_byte := data_reg

    val IDLE = U"00"
    val COUNT = U"01"
    val STOP = U"10"
    val WAITSTOP = U"11"

    done_reg := (state === STOP) && io.in_sig

    switch(state) {
      is(IDLE) {
        when(!io.in_sig) {
          state := COUNT
          cnt := 0
        }
      }
      is(COUNT) {
        data_reg(cnt) := io.in_sig
        cnt := cnt + 1
        when(cnt === 7) {
          state := STOP
        }
      }
      is(STOP) {
        when(io.in_sig) {
          state := IDLE
        } otherwise {
          state := WAITSTOP
        }
      }
      is(WAITSTOP) {
        when(io.in_sig) {
          state := IDLE
        }
      }
    }
  }
}
