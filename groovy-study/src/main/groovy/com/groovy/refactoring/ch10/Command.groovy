package com.groovy.refactoring.ch10

import groovy.transform.TypeChecked;

@TypeChecked
abstract class Command {
	def static final Command FORWARD = new Forward()
	def static final Command BACKWARD = new Backward()
	def static final Command TURN_RIGHT = new Right()
	def static final Command TURN_LEFT = new Left()
	def static final Map<String, Command> commandNameMap = new HashMap<String, Command>()
	static {
		commandNameMap[FORWARD.name] = FORWARD
		commandNameMap[BACKWARD.name]= BACKWARD 
		commandNameMap[TURN_RIGHT.name]= TURN_RIGHT
		commandNameMap[TURN_LEFT.name]= TURN_LEFT
	}
	private final String name
	protected Command(String name) {
		this.name = name
	}
	def String getName() {
		return name
	}
	def static Command parseCommand(String name) throws InvalidCommandException {
		if (!commandNameMap.containsKey(name)) 
			throw new InvalidCommandException(name)
		return commandNameMap[name]
	}
	abstract void execute(Robot robot)
	
	private static class Forward extends Command {
		Forward() {
			super("forward")
		}
		@Override
		def void execute(Robot robot) {
			robot.forward()
		}
	}
	private static class Backward extends Command {
		Backward() {
			super("backward")
		}
		@Override
		def void execute(Robot robot) {
			robot.backward()
		}
	}
	private static class Right extends Command {
		Right() {
			super("right")
		}
		@Override
		def void execute(Robot robot) {
			robot.right()
		}
	}
	private static class Left extends Command {
		Left() {
			super("left")
		}
		@Override
		def void execute(Robot robot) {
			robot.left()
		}
	}
}
