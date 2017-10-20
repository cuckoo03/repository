package com.designpattern.scala.ch19

/**
 * @author TC
 */
class DayState extends State {
  override def doClock(context: Context, hour: Int): Unit = {
    if (hour < 9 || 17 <= hour)
      context.changeState(NightState.getInstance())
  }

  override def doUse(context: Context): Unit = {
    context.callSecurityCenter("금고사용(주간)")
  }

  override def doAlarm(context: Context): Unit = {
    context.callSecurityCenter("비상벨(주간)")
  }

  override def doPhone(context: Context): Unit = {
    context.recordLog("일반통화(주간)")
  }

  override def toString(): String = "[주간]"
}

object DayState {
  private def singleton: DayState = new DayState()

  def getInstance(): State = singleton
}