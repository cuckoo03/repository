package com.scala.pis.ch34

import scala.swing.SimpleSwingApplication
import scala.swing.MainFrame
import scala.swing.Button

/**
 * @author cuckoo03
 */
object FirstSwingApp extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "title"
		contents = new Button {
			text = "click"
		}
	}
}