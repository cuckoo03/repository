package com.designpattern.scala.ch15

import java.util.Properties
import java.io.FileInputStream
import java.io.IOException

/**
 * @author cuckoo03
 */
class Database {

}

object Database {
  def getProperties(dbName: String): Properties = {
    def fileName = dbName + ".txt"
    val prop = new Properties()
    try {
    	prop.load(new FileInputStream(fileName))
    } catch {
      case e: IOException => {
        println(s"Warning: $fileName is not found")
      }
    }
    return prop
  }
}