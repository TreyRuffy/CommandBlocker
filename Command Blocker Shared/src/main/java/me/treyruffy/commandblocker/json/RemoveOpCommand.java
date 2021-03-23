package me.treyruffy.commandblocker.json;

public class RemoveOpCommand {

	private final String command;
	private final Boolean confirmation;
	
	private RemoveOpCommand(String command, Boolean confirmation) {
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
