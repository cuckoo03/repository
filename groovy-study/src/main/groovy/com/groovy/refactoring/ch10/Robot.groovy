package com.groovy.refactoring.ch10

import groovy.transform.TypeChecked

@TypeChecked
class Robot {
	private final String name
	private final Position position = new Position(0, 0)
	private final Direction direction = new Direction(0, 1)
	Robot(String name) {
		this.name = name
	}
	def void execute(String commandSequence) {
		def tokenizer = new StringTokenizer(commandSequence)
		try {
			while (tokenizer.hasMoreTokens()) {
				def String token = tokenizer.nextToken()
				executeCommand(token)
			}
		} catch (InvalidCommandException e) {
			println "Invalid command:${e.message}"
		}
	}
	def void executeCommand(String commandString) {
		def command = Command.parseCommand(commandString)
		command.execute(this)
	}
	def void forward() {
		position.relativeMove(direction.x, direction.y)
	}
	def void backward() {
		position.relativeMove(direction.x, direction.y)
	}
	def void right() {
		direction.setDirection(direction.x, direction.y)
	}
	def void left() {
		direction.setDirection(direction.x, direction.y)
	}
	@Override
	def String toString() {
		return "Robot:$name, position($position.x, $position.y), direction($direction.x, $direction.y)"
	}
}
