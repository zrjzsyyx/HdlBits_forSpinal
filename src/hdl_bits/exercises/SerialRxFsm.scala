package hdl_bits.exercises

import spinal.core._
import spinal.lib.fsm._
import hdl_bits.HdlBitsExercise

class SerialRxFsm extends HdlBitsExercise {
  val io = new Bundle {
    val clk    = in Bool()
    val in_sig = in Bool()
    val reset  = in Bool()
    val done   = out Bool()
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = io.clk,
    reset  = io.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )) {
    val cnt = Reg(UInt(3 bits))

    val fsm = new StateMachine {
      val done_reg = Reg(Bool()) init(False)

      val idle     = new State with EntryPoint
      val count    = new State
      val stop     = new State
      val waitstop = new State

      io.done := done_reg
      done_reg := isActive(stop) && io.in_sig

      idle.whenIsActive {
        when(!io.in_sig) {
          cnt := 0
          goto(count)
        }
      }

      count.whenIsActive {
        cnt := cnt + 1
        when(cnt === 7) {
          goto(stop)
        }
      }

      stop.whenIsActive {
        when(io.in_sig) {
          goto(idle)
        } otherwise {
          goto(waitstop)
        }
      }

      waitstop.whenIsActive {
        when(io.in_sig) {
          goto(idle)
        }
      }
    }
  }
}
