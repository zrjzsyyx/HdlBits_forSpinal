package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Hdlc extends HdlBitsExercise {
  val io = new Bundle {
    val clk = in Bool ()
    val reset = in Bool ()
    val in_sig = in Bool ()
    val disc = out Bool ()
    val flag = out Bool ()
    val err = out Bool ()
  }

  val syncArea = new ClockingArea(
    ClockDomain(
      clock = io.clk,
      reset = io.reset,
      config = ClockDomainConfig(resetKind = SYNC)
    )
  ) {
    // States tracking consecutive 1s since last 0
    val IDLE = U"000" // 01
    val S5 = U"001" // 011111
    val DISC = U"010" // 11111 + 0  → discard
    val FLAG = U"011" // 111111 + 0 → flag
    val ERR = U"100" // 1111111+  → error
    val WAIT = U"101"

    val cnt = Reg(UInt(3 bits)) init (0)
    val state = Reg(UInt(3 bits)) init (IDLE)

    switch(state) {
      is(IDLE) {
        when(io.in_sig) {
          state := S5
        }
        cnt := 0
      }
      is(S5) {
        when(io.in_sig) {
          cnt := cnt + 1
        } otherwise {
          state := IDLE
          cnt := 0
        }
        when(cnt === 4) {
          when(io.in_sig) {
            state := WAIT
          } otherwise {
            state := DISC
          }
        }
      }
      is(WAIT) {
        when(io.in_sig) {
          state := ERR
        } otherwise {
          state := FLAG
        }
      }
      is(DISC) {
        when(!io.in_sig) {
          state := IDLE
        } otherwise {
          state := S5
          cnt := 0
        } // 防止立刻衔接下一个识别信号时丢拍
      }
      is(FLAG) {
        when(!io.in_sig) {
          state := IDLE
        } otherwise {
          state := S5
          cnt := 0
        }
      }
      is(ERR) {
        when(!io.in_sig) {
          state := IDLE
        }
      }
    }

    io.disc := (state === DISC)
    io.flag := (state === FLAG)
    io.err := (state === ERR)
  }
}
