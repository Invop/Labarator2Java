package com.automatedworkspace.inventorymanagement.statistics;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigManager {

	private static final String JSON_FILE_PATH = "src/com/automatedworkspace/files/config.json";
	private static final String JSON_IN_FILE_PATH = "src/com/automatedworkspace/files/in.json";

	public static void writeConfig(Config config) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new FileWriter(JSON_FILE_PATH)) {
			gson.toJson(config, writer);
		}

	}

	public static Config readConfig() throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(JSON_FILE_PATH)) {
			return gson.fromJson(reader, Config.class);
		}
	}

	public static void writeIn(DeliveryConfig config) throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new FileWriter(JSON_IN_FILE_PATH)) {
			gson.toJson(config, writer);
		}
	}

	public static DeliveryConfig readIn() throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(JSON_IN_FILE_PATH)) {
			return gson.fromJson(reader, DeliveryConfig.class);
		}
	}
}