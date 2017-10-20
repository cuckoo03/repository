package com.designpattern.scala.ch19

/**
 * @author TC
 */
class NightState extends State {
  def doClock(context: Context, hour: Int): Unit = {
    if (9 <= hour && hour < 17)
      context.changeState(DayState.getInstance())
  }

  def doUse(context: Context): Unit = {
    context.callSecurityCenter("비상:야간의 금고사용")
  }

  def doAlarm(context: Context): Unit = {
    context.callSecurityCenter("비상벨(야간)")
  }

  def doPhone(context: Context): Unit = {
    context.recordLog("야간의 통화 녹음")
  }

  override def toString(): String = return "[야간]"
}
object NightState {
  private def singleton: NightState = new NightState()

  def getInstance(): State = singleton
}