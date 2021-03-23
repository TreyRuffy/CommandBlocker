package me.treyruffy.commandblocker.json;

public class RemoveCommand {

	private final String command;
	private final Boolean confirmation;
	
	private RemoveCommand(String command, Boolean confirmation) {
		this.command = command;
		this.confirmation = confirmation;
	}
	
	public String getCommand() {
		return command;
	}
	
	public Boolean getConfirmation() {
		return confirmation;
	}
}
