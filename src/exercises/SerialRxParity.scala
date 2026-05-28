package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise
import scala.annotation.switch

/** Parity checker BlackBox — TFF that counts number of 1-bits. */
class Parity extends BlackBox {
  val io = new Bundle {
    val clk = in Bool ()
    val reset = in Bool ()
    val in_sig = in Bool ()
    val odd = out Bool ()
  }
  noIoPrefix()
  io.in_sig.setName("in")
  setDefinitionName("parity")
}

class SerialRxParity extends HdlBitsExercise {
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
    // 1. 将状态常量定义提前，方便后面的组合逻辑引用
    val IDLE = U"00"
    val COUNT = U"01"
    val STOP = U"10"
    val WAITSTOP = U"11"

    val parity = new Parity()
    parity.io.clk := clockDomain.clock

    val state = Reg(UInt(2 bits)) init (IDLE)
    val cnt = Reg(UInt(4 bits)) init (0)
    val done_reg = Reg(Bool()) init (False)
    val data_reg = Reg(UInt(8 bits)) init (0)

    // 【核心修复】2. 删除 Reg 类型的 reset_odd，直接使用组合逻辑赋值复位
    // 只要系统处于复位状态，或者状态机处于 IDLE，就保持 parity 模块清零
    parity.io.reset := (state === IDLE) || clockDomain.isResetActive

    // 【核心修复】3. 增加组合逻辑保护：只在 COUNT 状态接收数据和校验位时，才将 in_sig 送入 parity
    parity.io.in_sig := False
    when(state === COUNT) {
      parity.io.in_sig := io.in_sig
    }

    io.done := done_reg
    io.out_byte := data_reg

    switch(state) {
      is(IDLE) {
        when(!io.in_sig) {
          state := COUNT
          cnt := 0
        }
        // 删除了此处原有的 reset_odd := io.in_sig
      }

      is(COUNT) {
        cnt := cnt + 1
        when(cnt(3) === False) {
          // cnt 为 0~7 时，正好对应 8 位数据
          data_reg(cnt(2 downto 0)) := io.in_sig
        }
        when(cnt === 8) {
          // cnt 为 8 时，接收的是第 9 位（奇偶校验位），下个周期进入 STOP
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

    // 只有在 STOP 状态、停止位正确且奇偶校验通过时，拉高 done
    done_reg := (state === STOP) && io.in_sig && parity.io.odd

  }
}
