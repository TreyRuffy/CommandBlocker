package me.treyruffy.commandblocker;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class Config {

	public static List<Component> getAdventureMessages(String category, String message) {
		MethodInterface mi = Universal.get().getMethods();
		return getAdventureMessages(category, message, mi.getMessagesConfig());
	}

	public static List<Component> getAdventureMessages(String category, String message, Object fileConfiguration) {
		MethodInterface mi = Universal.get().getMethods();
		List<Component> componentList = new ArrayList<>();
		for (String individualMessage : mi.getOldMessages(category, message, fileConfiguration)) {
			Component a = LegacyComponentSerializer.legacy(mi.getChatComponentChar()).deserialize(individualMessage);
			componentList.add(MiniMessage.get().parse(MiniMessage.get().serialize(a)));
		}
		return componentList;
	}

	public static Component getAdventureMessage(String category, String message) {
		Component result = null;
		for (Component component : getAdventureMessages(category, message)) {
			result = component;
			break;
		}
		return result;
	}

}
