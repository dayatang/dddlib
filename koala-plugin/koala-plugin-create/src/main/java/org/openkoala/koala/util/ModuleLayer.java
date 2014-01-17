package org.openkoala.koala.util;

public enum ModuleLayer {
	
	infra("基础实施层"),
	bizModel("领域层"),
	applicationInterface("应用层接口"),
	applicationImpl("应用层实现"),
	war("视图层");
	
	private static String[] descriptions;
	
	private String description;
	
	private ModuleLayer(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static String[] getDescriptions() {
		if (descriptions == null) {
			descriptions = new String[values().length];
			for (int i = 0; i < values().length; i ++) {
				descriptions[i] = values()[i].getDescription();
			}
		}
		return descriptions;
	}
	
	public static String getDescriptionByLayer(String layer) {
		if (layer == null) {
			throw new RuntimeException();
		}
		
		for (ModuleLayer moduleLayer : values()) {
			if (layer.equals(moduleLayer.name())) {
				return moduleLayer.getDescription();
			}
		}
		
		return null;
	}
	
	public static String getLayerByDescription(String description) {
		if (description == null) {
			throw new RuntimeException();
		}
		
		for (ModuleLayer moduleLayer : values()) {
			if (description.equals(moduleLayer.getDescription())) {
				return moduleLayer.name();
			}
		}
		
		return null;
	}
	
}
